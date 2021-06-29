package com.example.demo.controllers;

import com.example.demo.Helpers;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    @Test
    public void testCreateUser() {
        Long id = 0L;
        String username = "alan";
        String password = "password";
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setConfirmPassword(password);
        String encodedPassword = "encodedPassword";
        Cart cart = Helpers.createCart(null);
        User user = Helpers.createUser(id, username, cart);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        ResponseEntity<User> response = userController.createUser(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = response.getBody();
        user.setPassword(encodedPassword);
        assertThat(responseUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void testCreateUserAlreadyExists() {
        Long id = 1L;
        String username = "alan";
        User user = Helpers.createUser(id, username, null);
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        ResponseEntity<User> response = userController.createUser(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserPasswordNotValid() {
        CreateUserRequest request = new CreateUserRequest();
        request.setPassword("short");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        ResponseEntity<User> response = userController.createUser(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        String username = "alan";
        User user = Helpers.createUser(id, username, null);
        Optional<User> optionalUser = Optional.ofNullable(user);
        when(userRepository.findById(id)).thenReturn(optionalUser);
        ResponseEntity<User> response = userController.findById(id);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = response.getBody();
        Assert.assertEquals(user, responseUser);
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<User> response = userController.findById(id);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindByUserName() {
        Long id = 1L;
        String username = "alan";
        User user = Helpers.createUser(id, username, null);
        when(userRepository.findByUsername(username)).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = response.getBody();
        Assert.assertEquals(user, responseUser);
    }

    @Test
    public void testFindByUserNameNotFound() {
        String username = "alan";
        when(userRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<User> response = userController.findByUserName(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
