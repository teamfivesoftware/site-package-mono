package com.software.teamfive.sitepackageapi.auth.controllers;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import com.software.teamfive.sitepackageapi.auth.models.User;
import com.software.teamfive.sitepackageapi.auth.repos.UserRepository;
import com.software.teamfive.sitepackageapi.auth.services.AuthService;
import com.software.teamfive.sitepackageapi.auth.services.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles user authentication
 */

@RestController
@RequestMapping("/sp/api/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final RoleService roleService;

    @Autowired
    public AuthController(AuthService authService, RoleService roleService) {
        this.authService = authService;
        this.roleService = roleService;
    }

    @PostMapping(path = "register")
    public ResponseEntity<User> registerUser(@RequestParam String name,
                                             @RequestParam String username,
                                             @RequestParam String password,
                                             @RequestParam String roleName,
                                             @RequestParam boolean isSiteAdmin) throws Exception {
        Role role = this.roleService.lookUp(roleName).getBody();
        boolean doesUserExist = this.authService.doesUserExist(username);

        if (!doesUserExist) {
            return this.authService.register(name, username, password, role, isSiteAdmin);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping(path = "/login")
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password) throws Exception {
        return this.authService.login(username, password);
    }

    @PutMapping(path = "/suspend")
    public ResponseEntity<Boolean> suspendUser(@RequestParam String username) throws Exception {
        return this.authService.suspendUser(username);
    }

    @PutMapping(path = "/reinstate")
    public ResponseEntity<Boolean> reinstateUser(@RequestParam String username) throws Exception {
        return this.authService.reinstateUser(username);
    }

    // Elevate
    // Get Perms
}