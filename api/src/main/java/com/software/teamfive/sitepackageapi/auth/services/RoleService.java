package com.software.teamfive.sitepackageapi.auth.services;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import com.software.teamfive.sitepackageapi.auth.repos.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Create a new role
     * @param roleName The name to name the role
     * @return a created role.
     */
    public ResponseEntity<Role> createRole(String roleName) throws Exception {

        Objects.requireNonNull(roleName);

        if(roleName.isEmpty()) {
            throw new Exception("Role name cannot be empty!");
        }

        if (this.isRoleExisting(roleName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        else {
            return ResponseEntity.status(HttpStatus.OK).body(
              this.roleRepository.save(new Role(roleName, true))
            );
        }
    }

    /**
     * Checks if the role exists in the db already based on the name provided
     * Case-insensitive.
     * @param roleName The name to verify
     * @return true if it is already in the db, else false.
     */
    public boolean isRoleExisting(String roleName) throws Exception {
        Objects.requireNonNull(roleName);

        if(roleName.isEmpty()) {
            throw new Exception("Role name is empty");
        }

        return !this.roleRepository.findAll()
                .stream()
                .filter(r -> r.getRoleName().equals(roleName))
                .filter(r -> !r.isDeleted())
                .toList()
                .isEmpty();
    }

    /**
     * Checks if the role exists in the db already based on the name provided
     *
     * @param roleId The id to verify
     * @return true if it is already in the db, else false.
     */
    public boolean isRoleExisting(long roleId) {
        return !this.roleRepository.findAll()
                .stream()
                .filter(r -> r.getId() == roleId)
                .filter(r -> !r.isDeleted())
                .toList()
                .isEmpty();
    }

    /**
     * Returns a role object by name, if it exists.
     * @param roleName The name to look for
     * @return A role object for the name given
     */
    public ResponseEntity<Role> lookUp(String roleName) {
        Role foundRole = this.roleRepository.findAll()
                .stream()
                .filter(r -> r.getRoleName().equalsIgnoreCase(roleName))
                .filter(r -> !r.isDeleted())
                .findFirst()
                .orElse(null);

        if (foundRole == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Role().notFound());
        }
        else {
            return ResponseEntity.ok().body(foundRole);
        }
    }

    /**
     * Returns a role object by id, if it exists.
     * @param roleId The id to look for
     * @return A role object for the id given
     */
    public ResponseEntity<Role> lookUp(long roleId) {
        Role foundRole = this.roleRepository.findAll()
                .stream()
                .filter(r -> r.getId() == roleId)
                .filter(r -> !r.isDeleted())
                .findFirst()
                .orElse(null);

        if (foundRole == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Role().notFound());
        }
        else {
            return ResponseEntity.ok().body(foundRole);
        }
    }

    /**
     * Disable a role
     * @param roleId The id of the role to disable
     * @return true on success, else false
     */
    public ResponseEntity<Boolean> disableRole(long roleId) {
        if (!isRoleExisting(roleId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
        }

        Role foundRole = this.roleRepository.findAll()
                .stream()
                .filter(r -> r.getId() == roleId)
                .filter(r -> !r.isDeleted())
                .findFirst()
                .orElse(null);

        if (foundRole == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }

        else {
            foundRole.setEnabled(false);
            this.roleRepository.save(foundRole);
            return ResponseEntity.ok().body(true);
        }
    }

    /**
     * Disable a role
     * @param roleName The name of the role to disable
     * @return True on success, else false
     */
    public ResponseEntity<Boolean> disableRole(String roleName) {
        if (!isRoleExisting(roleName)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
        }

        Role foundRole = this.roleRepository.findAll()
                .stream()
                .filter(r -> r.getRoleName().equalsIgnoreCase(roleName))
                .filter(r -> !r.isDeleted())
                .findFirst()
                .orElse(null);

        if (foundRole == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }

        else {
            foundRole.setEnabled(false);
            this.roleRepository.save(foundRole);
            return ResponseEntity.ok().body(true);
        }
    }

    public ResponseEntity<Boolean> enableRole(long roleId) {
        if (!isRoleExisting(roleId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
        }
        else {
            Role foundRole = this.roleRepository.findAll()
                    .stream()
                    .filter(r -> !r.isDeleted())
                    .filter(r -> r.getId() == roleId)
                    .findFirst()
                    .orElse(null);

            if (foundRole == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
            }
            else {
                foundRole.setEnabled(true);
                this.roleRepository.save(foundRole);
                return ResponseEntity.ok().body(true);
            }
        }

    }

    public ResponseEntity<Boolean> enableRole(String roleName) {
        if (!isRoleExisting(roleName)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
        } else {
            Role foundRole = this.roleRepository.findAll()
                    .stream()
                    .filter(r -> !r.isDeleted())
                    .filter(r -> r.getRoleName().equalsIgnoreCase(roleName))
                    .findFirst()
                    .orElse(null);

            if (foundRole == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
            } else {
                foundRole.setEnabled(true);
                this.roleRepository.save(foundRole);
                return ResponseEntity.ok().body(true);
            }
        }
    }
}
