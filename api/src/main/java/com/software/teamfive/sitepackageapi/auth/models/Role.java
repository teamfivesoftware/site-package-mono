package com.software.teamfive.sitepackageapi.auth.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.software.teamfive.sitepackageapi.util.ApiModel;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "auth_Role")
public class Role extends ApiModel<Role> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String roleName;
    private boolean isEnabled;
    private boolean isDeleted;

    @JsonCreator
    public Role(String roleName, boolean isEnabled) {
        this.roleName = roleName;
        this.isEnabled = isEnabled;
        this.isDeleted = false;
    }

    public Role() {
        // Hibernate constructor
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id && isEnabled == role.isEnabled && isDeleted == role.isDeleted && Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, isEnabled, isDeleted);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", isEnabled=" + isEnabled +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Override
    public Role notFound() {
        Role notFound = new Role();
        notFound.setEnabled(false);
        notFound.setDeleted(true);
        notFound.setRoleName("ROLE NOT FOUND");
        notFound.setId(-1);
        return notFound;
    }
}
