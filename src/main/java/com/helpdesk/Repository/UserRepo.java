package com.helpdesk.Repository;

import com.helpdesk.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User>findByFirstNameContainingIgnoreCase(String firstName);


    Optional<User> findByEmail(String email);

    boolean existsByEmail(@NotBlank @Size(max = 50) @Email String email);
}
