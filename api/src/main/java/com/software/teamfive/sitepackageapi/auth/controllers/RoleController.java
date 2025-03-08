package com.software.teamfive.sitepackageapi.auth.controllers;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import com.software.teamfive.sitepackageapi.auth.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/sp/api/roles")
public class RoleController {

    @Autowired
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService service) {
        this.roleService = service;
    }

    @GetMapping(path = "online")
    public ResponseEntity<String> isOnline() {
        return ResponseEntity.ok("Role Controller online");
    }

    @PostMapping(path = "create")
    public ResponseEntity<Role> createRole(@RequestParam String roleName) throws Exception {
        return this.roleService.createRole(roleName);
    }

    @GetMapping(path = "lookup/id")
    public ResponseEntity<Role> lookup(@RequestParam long roleId) throws Exception {
        return this.roleService.lookUp(roleId);
    }

    @GetMapping(path = "lookup/name")
    public ResponseEntity<Role> lookup(@RequestParam String roleName) throws Exception {
        return this.roleService.lookUp(roleName);
    }

    @PutMapping(path = "disable/name")
    public ResponseEntity<Boolean> disable(@RequestParam String name) throws Exception{
        return this.roleService.disableRole(name);
    }

    @PutMapping(path = "disable/id")
    public ResponseEntity<Boolean> disable(@RequestParam long id) throws Exception {
        return this.roleService.disableRole(id);
    }

    @PutMapping(path = "enable/id")
    public ResponseEntity<Boolean> enable(@RequestParam long id) throws Exception{
        return this.roleService.enableRole(id);
    }

    @PutMapping(path = "enable/name")
    public ResponseEntity<Boolean> enable(@RequestParam String name) throws Exception {
        return this.roleService.enableRole(name);
    }
}