package com.helpdesk.repository;

import com.helpdesk.model.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCodeRepo extends JpaRepository<EmailCode,Integer> {

    Optional<EmailCode> findByEmail(String email);

}
