package com.software.teamfive.sitepackageapi.auth.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import com.software.teamfive.sitepackageapi.auth.models.User;
import com.software.teamfive.sitepackageapi.auth.services.AuthService;
import com.software.teamfive.sitepackageapi.auth.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @Mock
    private AuthService authService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AuthController authController;

    private Role testRole;
    private User testUser;

    @BeforeEach
    void setUp() {
        testRole = new Role("User", true);
        testRole.setDeleted(false);
        testUser = new User("John Doe", "johndoe", "password", testRole, false, true, "123");
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(roleService.lookUp("User")).thenReturn(ResponseEntity.ok(testRole));
        when(authService.doesUserExist("johndoe")).thenReturn(false);
        when(authService.register("John Doe", "johndoe", "password", testRole, false))
                .thenReturn(ResponseEntity.ok(testUser));

        ResponseEntity<User> response = authController.registerUser("John Doe", "johndoe", "password", "User", false);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("johndoe", response.getBody().getUsername());
    }

    @Test
    void testRegisterUser_Conflict() throws Exception {
        when(roleService.lookUp("User")).thenReturn(ResponseEntity.ok(testRole));
        when(authService.doesUserExist("johndoe")).thenReturn(true);

        ResponseEntity<User> response = authController.registerUser("John Doe", "johndoe", "password", "User", false);
        assertEquals(409, response.getStatusCodeValue());
    }

    @Test
    void testLogin_Success() throws Exception {
        when(authService.login("johndoe", "password")).thenReturn(ResponseEntity.ok(testUser));
        ResponseEntity<User> response = authController.login("johndoe", "password");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("johndoe", response.getBody().getUsername());
    }

    @Test
    void testLogin_Failure() throws Exception {
        when(authService.login("johndoe", "wrongpassword")).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        ResponseEntity<User> response = authController.login("johndoe", "wrongpassword");
        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testSuspendUser_Success() throws Exception {
        when(authService.suspendUser("johndoe")).thenReturn(ResponseEntity.ok(true));
        ResponseEntity<Boolean> response = authController.suspendUser("johndoe");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void testSuspendUser_Failure() throws Exception {
        when(authService.suspendUser("johndoe")).thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).body(false));
        ResponseEntity<Boolean> response = authController.suspendUser("johndoe");
        assertEquals(409, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }

    @Test
    void testReinstateUser_Success() throws Exception {
        when(authService.reinstateUser("johndoe")).thenReturn(ResponseEntity.ok(true));
        ResponseEntity<Boolean> response = authController.reinstateUser("johndoe");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void testReinstateUser_Failure() throws Exception {
        when(authService.reinstateUser("johndoe")).thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).body(false));
        ResponseEntity<Boolean> response = authController.reinstateUser("johndoe");
        assertEquals(409, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }
}
