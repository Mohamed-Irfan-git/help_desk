package com.helpdesk.security.response;

import com.helpdesk.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private Integer batchNo;
    private List<String> roles;
    private List<Question> questions;


    public UserInfoResponse(Long userId, String firstName, String lastName, Integer batchNo, String department, String email, List<String> roles) {
        this.userId = Math.toIntExact(userId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.batchNo = batchNo;
        this.department = department;
        this.roles = roles;

    }


//    private String jwtToken;


}
