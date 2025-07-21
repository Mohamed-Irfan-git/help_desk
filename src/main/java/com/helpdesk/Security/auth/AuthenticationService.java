package com.helpdesk.Security.auth;

import com.helpdesk.model.AppRole;
import com.helpdesk.model.EmailCode;
import com.helpdesk.model.Role;
import com.helpdesk.model.User;
import com.helpdesk.Repository.DepartmentRepo;
import com.helpdesk.Repository.EmailCodeRepo;
import com.helpdesk.Repository.RoleRepo;
import com.helpdesk.Repository.UserRepo;
import com.helpdesk.Security.config.JwtUtil;
import com.helpdesk.Security.request.LoginRequest;
import com.helpdesk.Security.request.PasswordResetRequest;
import com.helpdesk.Security.request.RegisterRequest;
import com.helpdesk.Security.response.RegisterResponse;
import com.helpdesk.Security.response.UserInfoResponse;
import com.helpdesk.Security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepo roleRepository;
    private final DepartmentRepo departmentRepo;
    private final JavaMailSender mailSender;
    private final EmailCodeRepo emailCodeRepo;

    public RegisterResponse register(RegisterRequest request) {
        // Encode the password before saving it
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Initialize a set of roles
        Set<String> strRoles = request.getRole(); // Can be null or have roles like ["admin"]
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Default role (ROLE_USER) when no role is provided
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            // Loop through the provided roles (e.g. ["admin", "seller"])
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    default:
                        // If role doesn't match known ones, default to user role
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }

        // Create the user with the roles
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setGender(request.getGender());
        user.setBatchNo(request.getBatchNo());
        user.setRoles(roles);

        if(request.getDepartmentId() == 1){
            user.setDepartment(departmentRepo.findById(1L).orElseThrow(() -> new RuntimeException("Department Not Found!!!")));
        } else if (request.getDepartmentId() == 2) {
            user.setDepartment(departmentRepo.findById(2L).orElseThrow(() -> new RuntimeException("Department Not Found!!!")));
        } else if (request.getDepartmentId()==3) {
            user.setDepartment(departmentRepo.findById(3L).orElseThrow(() -> new RuntimeException("Department Not Found!!!")));
        }else {
            throw new RuntimeException("Add Correct Department Id 1 : ICT , 2 : ET , 3 : BST");
        }


        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Generate JWT Token
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(UserDetailsImpl.build(user));
        // Return response with JWT token

        UserInfoResponse userInfoResponse = UserInfoResponse
                .builder()
                .userId(Math.toIntExact(savedUser.getUserId()))
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .batchNo(savedUser.getBatchNo())
                .email(savedUser.getEmail())
                .department(savedUser.getDepartment().getDepartmentName())
                .roles(savedUser.getRoles()
                        .stream()
                        .map(role -> role.getRoleName().name()) // assuming getRoleName() returns AppRole enum
                        .collect(Collectors.toList()))
                .build();


        return new RegisterResponse(userInfoResponse, jwtCookie);
    }
    public ResponseEntity<UserInfoResponse> authenticate(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()

        ));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(UserDetailsImpl.build(user));

        UserInfoResponse  userInfoResponse =  new UserInfoResponse();
//       userInfoResponse.setJwtToken(jwtCookie.toString());
        userInfoResponse.setUserId(Math.toIntExact(user.getUserId()));
        userInfoResponse.setEmail(user.getEmail());
        userInfoResponse.setFirstName(user.getFirstName());
        userInfoResponse.setLastName(user.getLastName());
        userInfoResponse.setBatchNo(user.getBatchNo());
        userInfoResponse.setDepartment(user.getDepartment().getDepartmentName());
        userInfoResponse.setRoles(
                user.getRoles().stream()
                        .map(role -> role.getRoleName().name()) // assuming getRoleName() returns AppRole enum
                        .collect(Collectors.toList())
        );

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(userInfoResponse);
    }

    public ResponseEntity<String> signOut() {
        ResponseCookie cookie = jwtService.getClearJwtCoockie();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body("User Signout Successfully");
    }

    public String getResetCode(String email) {

        int code = (int)(Math.random() * 90000) + 10000;

        String textMessage = String.format("Hi there,\n" +
                "\n" +
                "We're from HelpDesk! You recently requested to reset your password.\n" +
                "\n" +
                "Please use the following verification code to proceed:\n" +
                "\n" +
                "\uD83D\uDC49 Your verification code: %d\n" +
                "\n" +
                "Enter this code in the application to verify your identity.\n" +
                "\n" +
                "If you did not request this, you can safely ignore this email.\n" +
                "\n" +
                "Best regards,  \n" +
                "The HelpDesk Team",code);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User Not Registered"));

        Optional<EmailCode> dbEmailCode = emailCodeRepo.findByEmail(email);

        if(dbEmailCode.isEmpty()){
            EmailCode emailCode = new EmailCode();
            emailCode.setEmail(user.getEmail());
            emailCode.setCreatedAt(LocalDateTime.now());
            emailCode.setExpiresAt(emailCode.getCreatedAt().plusMinutes(10));
            emailCode.setCode(code);
            emailCodeRepo.save(emailCode);
        }else{
            EmailCode existingEmailCode = dbEmailCode.get();
            existingEmailCode.setCode(code);
            existingEmailCode.setCreatedAt(LocalDateTime.now());
            existingEmailCode.setExpiresAt(existingEmailCode.getCreatedAt().plusMinutes(10));

            emailCodeRepo.save(existingEmailCode);
        }


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("HelpDesk Verification Code");
        message.setText(textMessage);
        mailSender.send(message);


        return "your verification code sent to the email";
    }

    public String resetPassword(PasswordResetRequest passwordResetRequest){

        User user = userRepository.findByEmail(passwordResetRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("User Not Registered"));

        EmailCode emailCode = emailCodeRepo.findByEmail(passwordResetRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("User has not requested a code"));

        boolean equalityOfCode = Objects.equals(emailCode.getCode(), passwordResetRequest.getCode());
        boolean timeExpiryCheck =  LocalDateTime.now().isAfter(emailCode.getExpiresAt());
        if(!equalityOfCode){
            return "please provide valid reset code";
        }
        if(timeExpiryCheck){
            return  "your code is expired";
        }

        String encodedPass = passwordEncoder.encode(passwordResetRequest.getPassword());
        user.setPassword(encodedPass);
        userRepository.save(user);

        return "password reset successfully";
    }
}