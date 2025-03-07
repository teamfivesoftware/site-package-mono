package com.software.teamfive.sitepackageapi.auth.repos;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
