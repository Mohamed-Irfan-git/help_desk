package com.helpdesk.controller;

import com.helpdesk.repository.QuestionRepo;
import com.helpdesk.repository.UserRepo;
import com.helpdesk.security.auth.AuthenticationService;
import com.helpdesk.security.request.EmailRequest;
import com.helpdesk.security.request.LoginRequest;
import com.helpdesk.security.request.PasswordResetRequest;
import com.helpdesk.security.request.RegisterRequest;
import com.helpdesk.security.response.MassageResponse;
import com.helpdesk.security.response.RegisterResponse;
import com.helpdesk.security.response.UserInfoResponse;
import com.helpdesk.security.services.UserDetailsImpl;
import com.helpdesk.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserRepo userRepository;
    private final QuestionRepo questionRepo;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
                @RequestBody RegisterRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MassageResponse("Error: Email is already taken!"));
        }

        RegisterResponse registerResponse = authenticationService.register(request);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, registerResponse.getJwtCookie().toString())
                .body(registerResponse.getUserInfo());
        }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse>  authenticate(@RequestBody LoginRequest request){
        return authenticationService.authenticate(request);
    }

    @PostMapping("/resetcode")
    public ResponseEntity<String> sendPasswordResetCode(@RequestBody EmailRequest request){

        String response = authenticationService.getResetCode(request.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest){

        String response = authenticationService.resetPassword(passwordResetRequest);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<UserInfoResponse> currentUser(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority())
                .toList();



        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getUserId(),userDetails.getFirstName(),userDetails.getLastName(),userDetails.getBatchNo(),userDetails.getDepartment(),userDetails.getEmail(),roles);
        List<Question> questions = new ArrayList<>();
        questions.addAll(questionRepo.findByUserId(userDetails.getUserId()));
        userInfoResponse.setQuestions(questions);

        return ResponseEntity.ok().body(userInfoResponse);
    }


    @PostMapping("/signout")
    public ResponseEntity<String> signout(){
        return authenticationService.signOut();
    }
}

