package com.learnify.repository;

import com.learnify.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Roles findByRoleName(String roleName);
}
