package com.helpdesk.Security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    String email;
    Integer code;
    String password;
}
