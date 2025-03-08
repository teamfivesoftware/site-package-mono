package com.software.teamfive.sitepackageapi.auth.services;

import com.software.teamfive.sitepackageapi.auth.models.Role;
import com.software.teamfive.sitepackageapi.auth.models.User;
import com.software.teamfive.sitepackageapi.auth.repos.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService service;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void registerTest() {
        User user = new User();
        when(this.userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = this.service.register("foo", "bar", "password", new Role(), true);
        User registeredUser = response.getBody();

        assertEquals(user, registeredUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(any());
    }

    @Test(expected = Exception.class)
    public void registerFailsOnNullName() {
        this.service.register(null, "", "", new Role(), false);
    }

    @Test(expected = Exception.class)
    public void registerFailsOnNullUsername() {

        this.service.register("", null, "", new Role(), false);
    }

    @Test(expected = Exception.class)
    public void registerFailsOnNullPassword() {
        this.service.register("", "", null, new Role(), false);

    }

    @Test(expected = Exception.class)
    public void registerFailsOnNullRole() {
        this.service.register("", "", "", null, false);

    }

    @Test
    public void idealLoginTest() throws Exception{

        String hashed = Base64.encodeBase64String("123".getBytes());
        User mockUser = new User("Foo", "username", hashed, new Role(), false, false, "1234");
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);


        ResponseEntity<User> response = this.service.login("username", "123");
        User user = response.getBody();

        assertEquals(mockUser, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.userRepository, times(1)).save(any(User.class));
        verify(this.userRepository, times(1)).findAll();

    }

    @Test
    public void loginUsernameIsCaseSensitive() throws Exception{

        String hashed = Base64.encodeBase64String("123".getBytes());
        User mockUser = new User("Foo", "username", hashed, new Role(), false, false, "1234");
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);


        ResponseEntity<User> response = this.service.login("Username", "123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    public void loginPasswordIsCaseSensitive() throws Exception{

        String hashed = Base64.encodeBase64String("fooBar".getBytes());
        User mockUser = new User("Foo", "username", hashed, new Role(), false, false, "1234");
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        ResponseEntity<User> response = this.service.login("username", "FooBar");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(this.userRepository, times(1)).findAll();

    }

    @Test(expected = Exception.class)
    public void loginFailsOnNullUsername() throws Exception {
        this.service.login(null, "a");
    }

    @Test(expected = Exception.class)
    public void loginFailsOnNullPassword() throws Exception{
        this.service.login("a", null);
    }

    @Test(expected = Exception.class)
    public void loginFailsOnEmptyUsername() throws Exception{
        this.service.login("", "a");
    }

    @Test(expected = Exception.class)
    public void loginFailsOnEmptyPassword() throws Exception {
        this.service.login("a", "");
    }

    @Test
    public void loginReturnsNoContentOnNullUserResult_BadPassword() throws Exception {
        User user = new User();
        user.setUsername("foo");
        String hashed = Base64.encodeBase64String("123".getBytes());
        user.setPasswordHash(hashed);

        when(this.userRepository.findAll()).thenReturn(List.of(user));

        ResponseEntity<User> result = this.service.login("foo", "1234");
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    public void loginReturnsNoContentOnNullUserResult_BadUsername() throws Exception {
        User user = new User();
        user.setUsername("foo");
        String hashed = Base64.encodeBase64String("123".getBytes());
        user.setPasswordHash(hashed);

        when(this.userRepository.findAll()).thenReturn(List.of(user));

        ResponseEntity<User> result = this.service.login("fooBar", "123");
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    public void loginReturnsUnauthorisedWhenLockedOut() throws Exception {
        User user = new User();
        user.setUsername("foo");
        String hashed = Base64.encodeBase64String("123".getBytes());
        user.setPasswordHash(hashed);
        user.setLockedOut(true);
        when(this.userRepository.findAll()).thenReturn(List.of(user));

        ResponseEntity<User> result = this.service.login("foo", "123");
        verify(this.userRepository, times(1)).findAll();
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    public void doesUserExistsFindsExistingUser() throws Exception {
        User user = new User();
        user.setUsername("FooBar123");
        when(this.userRepository.findAll()).thenReturn(List.of(user));

        boolean doesUserExist = this.service.doesUserExist("FooBar123");
        assertTrue(doesUserExist);
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    public void doesUserExistDoesNotFindNonExistentUser() throws Exception {
        User user = new User();
        user.setUsername("FooBar123");
        when(this.userRepository.findAll()).thenReturn(List.of(user));

        boolean doesUserExist = this.service.doesUserExist("OtherUsername");
        assertFalse(doesUserExist);
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    public void doesUserExistIsCaseSensitive() throws Exception {
        User user = new User();
        user.setUsername("FooBar123");
        when(this.userRepository.findAll()).thenReturn(List.of(user));

        boolean doesUserExist = this.service.doesUserExist("fooBar123");
        assertFalse(doesUserExist);
        verify(this.userRepository, times(1)).findAll();
    }

    @Test(expected = Exception.class)
    public void doesUserExistFailsOnNull() throws Exception {
        this.service.doesUserExist(null);
    }

    @Test(expected = Exception.class)
    public void doesUserExistFailsOnEmpty() throws Exception {
        this.service.doesUserExist("");
    }

    @Test
    public void suspendUserSuspendsRequestedUser() throws Exception {
        User unsus = new User();
        unsus.setUsername("unsus");
        unsus.setLockedOut(false);

        User sus = new User();
        sus.setLockedOut(true);
        sus.setUsername("unsus");

        when(this.userRepository.findAll()).thenReturn(List.of(unsus));
        when(this.userRepository.save(unsus)).thenReturn(sus);

        ResponseEntity<Boolean> success = this.service.suspendUser("unsus");
        boolean body = success.getBody();

        assertTrue(body);
        assertEquals(HttpStatus.OK, success.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
        verify(this.userRepository, times((1))).save(unsus);
    }

    @Test
    public void suspendUserReturnsNoContentOnNoUser() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("foobar");

        when(this.userRepository.findAll()).thenReturn(List.of(mockUser));

        ResponseEntity<Boolean> response = this.service.suspendUser("badUsername");
        boolean isSuccessful = response.getBody();

        assertFalse(isSuccessful);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    public void suspendUserIsCaseSensitive() throws Exception {
        User userOne = new User();
        userOne.setUsername("FooBar");

        when(this.userRepository.findAll()).thenReturn(List.of(userOne));

        ResponseEntity<Boolean> response = this.service.suspendUser("foobar");
        boolean isSuccessful = response.getBody();

        assertFalse(isSuccessful);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test(expected = Exception.class)
    public void suspendUserFailsOnNullUsername() throws Exception {
        this.service.suspendUser(null);
    }

    @Test(expected = Exception.class)
    public void suspendUserFailsOnEmptyUsername() throws Exception {
        this.service.suspendUser("");
    }

    @Test
    public void reinstateReinstatesCorrectUser() throws Exception {
        User lockedOut = new User();
        lockedOut.setUsername("lockedOut");
        lockedOut.setLockedOut(true);

        User unlocked = new User();
        unlocked.setLockedOut(false);
        unlocked.setUsername("lockedOut");

        when(this.userRepository.findAll()).thenReturn(List.of(lockedOut));
        when(this.userRepository.save(lockedOut)).thenReturn(unlocked);

        ResponseEntity<Boolean> success = this.service.reinstateUser("lockedOut");
        boolean body = success.getBody();

        assertTrue(body);
        assertEquals(HttpStatus.OK, success.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
        verify(this.userRepository, times((1))).save(lockedOut);
    }

    @Test
    public void reinstateReturnsNoContentOnNullUser() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("foobar");

        when(this.userRepository.findAll()).thenReturn(List.of(mockUser));

        ResponseEntity<Boolean> response = this.service.reinstateUser("badUsername");
        boolean isSuccessful = response.getBody();

        assertFalse(isSuccessful);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    public void reinstateIsCaseSensitive() throws Exception {
        User userOne = new User();
        userOne.setUsername("FooBar");

        when(this.userRepository.findAll()).thenReturn(List.of(userOne));

        ResponseEntity<Boolean> response = this.service.reinstateUser("foobar");
        boolean isSuccessful = response.getBody();

        assertFalse(isSuccessful);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test(expected = Exception.class)
    public void reinstateFailsOnNullUsername() throws Exception {
        this.service.reinstateUser(null);
    }

    @Test(expected = Exception.class)
    public void reinstateFailsOnEmptyUsername() throws Exception {
        this.service.reinstateUser("");
    }
}