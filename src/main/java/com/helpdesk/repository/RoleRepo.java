package com.helpdesk.repository;

import com.helpdesk.model.AppRole;
import com.helpdesk.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(AppRole appRole);
}
