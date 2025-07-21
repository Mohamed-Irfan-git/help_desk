package com.helpdesk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email(message = "Please provide a valid email address")
    @NotNull(message = "Email cannot be null")
    @Column(nullable = false)
    private String email;

    @NotNull(message = "Verification code cannot be null")
    @Column(nullable = false)
    private Integer code;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;


}
