package com.helpdesk.Security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "Z3s8D9fLrV1qW5mNtJxB7eKwY6uGh0PaMzRdTiLkVpSnCoXvUrEbHaGyNzMqJcKtZpWlXvOmCrYfBvTyLdRuGnHpWsKeJdXlNqMqTsBpErYwUeIrOpNvBsXqAjFmDcHsLkJrVsZxMfEdBpUqYrTgVnZmWxAoEuJrPtHgYdXoClMrNfBqStUkXiVwJzQtGkMnBjRpTwLuEwFoGpTxMnYcZoHeLdKsWqFxNyKrLzAbXe";

    @Value("${spring.helpdesk.app.jwtCookieName}")
    private String jwtCookie;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public<T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT token is null or empty");
        }
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //cookie based impls
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null) {
            return cookie.getValue();
        }else
            return null;
    }




    public ResponseCookie generateJwtCookie(UserDetails userDetails) {
        String token = generateToken(userDetails);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,token)
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(false)
                .build();

        return cookie;
    }

    public ResponseCookie getClearJwtCoockie(){

        ResponseCookie cookie = ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();

        return cookie;
    }

}
