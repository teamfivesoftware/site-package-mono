package com.software.teamfive.sitepackageapi.auth.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.software.teamfive.sitepackageapi.auth.models.Role;
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
public class RoleControllerTests {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role("User", false);
        testRole.setId(1L);
        testRole.setEnabled(true);
        testRole.setDeleted(false);
    }

    @Test
    void testCreateRole_Success() throws Exception {
        when(roleService.createRole("User")).thenReturn(ResponseEntity.ok(testRole));
        ResponseEntity<Role> response = roleController.createRole("User");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User", response.getBody().getRoleName());
    }

    @Test
    void testLookupById_Success() throws Exception {
        when(roleService.lookUp(1L)).thenReturn(ResponseEntity.ok(testRole));
        ResponseEntity<Role> response = roleController.lookup(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testLookupById_NotFound() throws Exception {
        when(roleService.lookUp(2L)).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        ResponseEntity<Role> response = roleController.lookup(2L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testLookupByName_Success() throws Exception {
        when(roleService.lookUp("User")).thenReturn(ResponseEntity.ok(testRole));
        ResponseEntity<Role> response = roleController.lookup("User");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User", response.getBody().getRoleName());
    }

    @Test
    void testLookupByName_NotFound() throws Exception {
        when(roleService.lookUp("Admin")).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        ResponseEntity<Role> response = roleController.lookup("Admin");
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDisableByName_Success() throws Exception {
        when(roleService.disableRole("User")).thenReturn(ResponseEntity.ok(true));
        ResponseEntity<Boolean> response = roleController.disable("User");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void testDisableById_Success() throws Exception {
        when(roleService.disableRole(1L)).thenReturn(ResponseEntity.ok(true));
        ResponseEntity<Boolean> response = roleController.disable(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void testEnableByName_Success() throws Exception {
        when(roleService.enableRole("User")).thenReturn(ResponseEntity.ok(true));
        ResponseEntity<Boolean> response = roleController.enable("User");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void testEnableById_Success() throws Exception {
        when(roleService.enableRole(1L)).thenReturn(ResponseEntity.ok(true));
        ResponseEntity<Boolean> response = roleController.enable(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }
}
