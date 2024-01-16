package com.api.requesttracker.repository;

import com.api.requesttracker.entity.ERole;
import com.api.requesttracker.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);
}
