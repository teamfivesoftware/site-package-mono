package com.software.teamfive.sitepackageapi.auth.repos;

import com.software.teamfive.sitepackageapi.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
