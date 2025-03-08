package com.software.teamfive.sitepackageapi.auth.services;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import com.software.teamfive.sitepackageapi.auth.repos.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Before
    public void setUp() {
        openMocks(this);
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
    public void isRoleExistingFindsExistingRole() {

    }
}
