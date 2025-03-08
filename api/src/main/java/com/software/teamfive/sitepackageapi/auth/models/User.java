package com.software.teamfive.sitepackageapi.auth.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "auth_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String username;
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    private boolean isLoggedIn;

    private long lastLongInTimeStamp;
    private long registrationTimeStamp;
    private boolean isDeleted;
    private boolean isLockedOut;
    private boolean isAdmin;
    private String adminPin;

    @JsonCreator
    public User(String name,
                String username,
                String passwordHash,
                Role role,
                boolean isDeleted,
                boolean isAdmin,
                String adminPin) {
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isDeleted = isDeleted;
        this.isAdmin = isAdmin;
        this.adminPin = adminPin;

        this.isLockedOut = false;
        this.isLoggedIn = false;
        this.lastLongInTimeStamp = 0;
        this.registrationTimeStamp = System.currentTimeMillis();
    }

    public User() {
        // Hibernate Constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public long getLastLongInTimeStamp() {
        return lastLongInTimeStamp;
    }

    public void setLastLongInTimeStamp(long lastLongInTimeStamp) {
        this.lastLongInTimeStamp = lastLongInTimeStamp;
    }

    public long getRegistrationTimeStamp() {
        return registrationTimeStamp;
    }

    public void setRegistrationTimeStamp(long registrationTimeStamp) {
        this.registrationTimeStamp = registrationTimeStamp;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isLockedOut() {
        return isLockedOut;
    }

    public void setLockedOut(boolean lockedOut) {
        isLockedOut = lockedOut;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getAdminPin() {
        return adminPin;
    }

    public void setAdminPin(String adminPin) {
        this.adminPin = adminPin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isLoggedIn == user.isLoggedIn && lastLongInTimeStamp == user.lastLongInTimeStamp && registrationTimeStamp == user.registrationTimeStamp && isDeleted == user.isDeleted && isLockedOut == user.isLockedOut && isAdmin == user.isAdmin && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(username, user.username) && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(role, user.role) && Objects.equals(adminPin, user.adminPin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, passwordHash, role, isLoggedIn, lastLongInTimeStamp, registrationTimeStamp, isDeleted, isLockedOut, isAdmin, adminPin);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role=" + role +
                ", isLoggedIn=" + isLoggedIn +
                ", lastLongInTimeStamp=" + lastLongInTimeStamp +
                ", registrationTimeStamp=" + registrationTimeStamp +
                ", isDeleted=" + isDeleted +
                ", isLockedOut=" + isLockedOut +
                ", isAdmin=" + isAdmin +
                ", adminPin='" + adminPin + '\'' +
                '}';
    }
}
