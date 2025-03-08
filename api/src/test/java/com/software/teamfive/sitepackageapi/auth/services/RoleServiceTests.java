package com.software.teamfive.sitepackageapi.auth.services;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import com.software.teamfive.sitepackageapi.auth.repos.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role activeRole;
    private Role deletedRole;

    @Before
    public void setUp() {
        openMocks(this);
        activeRole = new Role("Admin", false);
        deletedRole = new Role("User", true);

        activeRole.setDeleted(false);
        deletedRole.setDeleted(true);

        activeRole.setId(1L);
        deletedRole.setId(2L);
    }

    @Test
    public void idealCreateRoleTest() throws Exception {
        Role role1 = new Role();
        role1.setRoleName("Role One");

        Role role2 = new Role();
        role2.setRoleName("Role Two");

        role1.setDeleted(false);
        when(this.roleRepository.findAll()).thenReturn(List.of(role1));
        when(this.roleRepository.save(any(Role.class))).thenReturn(role2);

        ResponseEntity<Role> result = this.roleService.createRole("Role Two");
        Role roleResult = result.getBody();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(this.roleRepository, times(1)).save(any(Role.class));
        assertEquals(role2, roleResult);
    }

    @Test(expected = Exception.class)
    public void createRoleFailsOnNullRoleName() throws Exception {
        this.roleService.createRole(null);
    }

    @Test(expected = Exception.class)
    public void createRoleFailsOnEmptyRoleName() throws Exception {
        this.roleService.createRole("");
    }

    @Test
    public void createRoleChecksExistence() throws Exception {
        Role roleOne = new Role();
        roleOne.setRoleName("role 1");
        when(this.roleRepository.findAll()).thenReturn(List.of(roleOne));
        when(this.roleRepository.save(any(Role.class))).thenReturn(roleOne);

        ResponseEntity<Role> roleResult = this.roleService.createRole("role 1");

        verify(this.roleRepository, times(1)).findAll();
    }

    @Test
    public void isRoleExistingNameFindsExistingRole() throws Exception {
        Role roleOne = new Role();
        roleOne.setRoleName("role 1");
        when(this.roleRepository.findAll()).thenReturn(List.of(roleOne));
        boolean result = this.roleService.isRoleExisting("role 1");

        assertTrue(result);
        verify(this.roleRepository, times(1)).findAll();
    }

    @Test
    public void isRoleExistingNameDoesNotFindNonExistentRole() throws Exception {
        Role roleOne = new Role();
        roleOne.setRoleName("role 1");
        when(this.roleRepository.findAll()).thenReturn(List.of(roleOne));
        boolean result = this.roleService.isRoleExisting("role 2");

        assertFalse(result);
        verify(this.roleRepository, times(1)).findAll();
    }

    @Test(expected = Exception.class)
    public void isRoleExistingNameFailsOnNullRole() throws Exception {
        this.roleService.isRoleExisting(null);
    }

    @Test(expected = Exception.class)
    public void isRoleExistingNameFailsOnEmptyRole() throws Exception {
        this.roleService.isRoleExisting("");
    }

    @Test
    public void isRoleExistingIdFindsExistingRole() throws Exception {
        Role roleOne = new Role();
        roleOne.setRoleName("role 1");

        roleOne.setId(1);
        when(this.roleRepository.findAll()).thenReturn(List.of(roleOne));
        boolean result = this.roleService.isRoleExisting(1);

        assertTrue(result);
        verify(this.roleRepository, times(1)).findAll();
    }

    @Test
    public void isRoleExistingIdDoesNotFindNonExistentRole() throws Exception {
        Role roleOne = new Role();
        roleOne.setRoleName("role 1");

        roleOne.setId(1);
        when(this.roleRepository.findAll()).thenReturn(List.of(roleOne));
        boolean result = this.roleService.isRoleExisting(2);

        assertFalse(result);
        verify(this.roleRepository, times(1)).findAll();
    }

    @Test
    public void testLookUpByName_Found() {
        when(roleRepository.findAll()).thenReturn(List.of(activeRole, deletedRole));
        ResponseEntity<Role> response = roleService.lookUp("Admin");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Admin", response.getBody().getRoleName());
    }

    @Test
    public void lookUpByName_NotFound() {
        when(roleRepository.findAll()).thenReturn(List.of(activeRole));
        ResponseEntity<Role> response = roleService.lookUp("NonExistent");
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void lookupById_NotFound() {
        when(roleRepository.findAll()).thenReturn(List.of(activeRole, deletedRole));
        ResponseEntity<Role> response = roleService.lookUp(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testLookUpById_NotFound() {
        when(roleRepository.findAll()).thenReturn(List.of(activeRole));
        ResponseEntity<Role> response = roleService.lookUp(99L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testDisableRoleById_Success() {
        when(roleRepository.findAll()).thenReturn(List.of(activeRole));
        when(roleRepository.save(any(Role.class))).thenReturn(activeRole);
        ResponseEntity<Boolean> response = roleService.disableRole(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    public void testDisableRoleById_NotFound() {
        when(roleRepository.findAll()).thenReturn(List.of(activeRole));
        ResponseEntity<Boolean> response = roleService.disableRole(99L);
        assertEquals(204, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }

    @Test
    public void testEnableRoleById_Success() {
        activeRole.setEnabled(false);
        when(roleRepository.findAll()).thenReturn(List.of(activeRole));
        when(roleRepository.save(any(Role.class))).thenReturn(activeRole);
        ResponseEntity<Boolean> response = roleService.enableRole(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    public void testEnableRoleById_NotFound() {
        when(roleRepository.findAll()).thenReturn(List.of(activeRole));
        ResponseEntity<Boolean> response = roleService.enableRole(99L);
        assertEquals(204, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }
}