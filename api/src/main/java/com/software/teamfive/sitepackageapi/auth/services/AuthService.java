package com.software.teamfive.sitepackageapi.auth.services;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import com.software.teamfive.sitepackageapi.auth.models.User;
import com.software.teamfive.sitepackageapi.auth.repos.RoleRepository;
import com.software.teamfive.sitepackageapi.auth.repos.UserRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public ResponseEntity<User> register(String name,
                                         String username,
                                         String password,
                                         Role role,
                                         boolean isSiteAdmin) {

        Objects.requireNonNull(name);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        Objects.requireNonNull(role);

        User user = new User();
        user.setName(name);
        user.setUsername(name);
        user.setPasswordHash(Base64.encodeBase64String(password.getBytes()));

        user.setRole(role);
        user.setAdmin(isSiteAdmin);
        user.setUsername(username);
        User result = this.userRepository.save(user);
        return ResponseEntity.ok(result);
    }

    @Transactional()
    public ResponseEntity<User> login(String username, String password) throws Exception {

        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Username and/or password cannot be blank!");
        }

        String hashed = Base64.encodeBase64String(password.getBytes());

        User user = userRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .filter(u -> u.getPasswordHash().equals(hashed))
                .filter(u -> !u.isDeleted())
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        if (user.isLockedOut()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        user.setLoggedIn(true);
        user.setLastLongInTimeStamp(System.currentTimeMillis());
        User result = userRepository.save(user);
        return ResponseEntity.ok(result);
    }

    @Transactional(readOnly = true)
    public boolean doesUserExist(String username) throws Exception{
        Objects.requireNonNull(username);
        if (username.isEmpty()) {
            throw new Exception("Username provided cannot be empty!");
        }

        User user = this.userRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        return user != null;
    }

    @Transactional
    public ResponseEntity<Boolean> suspendUser(String username) throws Exception {
        Objects.requireNonNull(username);
        if (username.isEmpty()) {
            throw new Exception("Username provided cannot be empty!");
        }

        User toSuspend = this.userRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (toSuspend == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
        }

        toSuspend.setLockedOut(true);
        this.userRepository.save(toSuspend);
        return ResponseEntity.ok().body(true);
    }

    @Transactional
    public ResponseEntity<Boolean> reinstateUser(String username) throws Exception{
        Objects.requireNonNull(username);
        if (username.isEmpty()) {
            throw new Exception("Username provided cannot be empty!");
        }

        User toReinstate = this.userRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (toReinstate == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
        }

        toReinstate.setLockedOut(false);
        this.userRepository.save(toReinstate);
        return ResponseEntity.ok().body(true);
    }
}
