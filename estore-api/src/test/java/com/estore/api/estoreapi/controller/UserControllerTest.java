package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.DiscountDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;
import com.estore.api.estoreapi.persistence.UserDAO;

import net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response;

public class UserControllerTest {
    private UserController userController;
    private JerseyDAO mockJerseyDAO;
    private DiscountDAO mockDiscountDAO;
    private UserDAO mockUserDAO;

    /**
     * Before each test, create a new UserController object and inject a mock user,
     * jersey, and discount DAO
     */

    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        mockDiscountDAO = mock(DiscountDAO.class);
        mockJerseyDAO = mock(JerseyDAO.class);
        userController = new UserController(mockUserDAO, mockJerseyDAO, mockDiscountDAO);
    }

    @Test
    public void testGetUser() throws IOException {
        // Setup
        User user = new User(0, "jjb", new Integer[][]{}, "");
        when(mockUserDAO.getUser(user.getIndex())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getIndex());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserByName() throws IOException {
        // Setup
        User user = new User(0, "jjb", new Integer[][]{}, "");
        when(mockUserDAO.getUserByName(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUserByName(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws IOException {
        // Setup
        int index = 0;
        when(mockUserDAO.getUser(index)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(index);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetUserByNameNotFound() throws IOException {
        // Setup
        String username = "DNE";
        when(mockUserDAO.getUserByName(username)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUserByName(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void testGetUserException() throws IOException {
        // Setup
        int index = 0;
        doThrow(new IOException()).when(mockUserDAO).getUser(index);

        // Invoke
        ResponseEntity<User> response = userController.getUser(index);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUserByNameException() throws IOException {
        // Setup
        String username = "DNE";
        doThrow(new IOException()).when(mockUserDAO).getUserByName(username);

        // Invoke
        ResponseEntity<User> response = userController.getUserByName(username);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException {
        // Setup
        User user1 = new User(0, "dmb", new Integer[][]{}, "");
        User user2 = new User(1, "amp", new Integer[][]{}, "");
        User[] users = { user1, user2 };
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUsersException() throws IOException {
        // setup
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchUsers() throws IOException {
        // Setup
        String key = "m";
        User user1 = new User(0, "dmb", new Integer[][]{}, "");
        User user2 = new User(1, "amp", new Integer[][]{}, "");
        User[] users = { user1, user2 };
        when(mockUserDAO.findUsers(key)).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(key);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testSearchUsersNotFound() throws IOException {
        // Setup
        String key = "k";
        when(mockUserDAO.findUsers(key)).thenReturn(null);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(key);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testSearchUsersException() throws IOException {
        // setup
        String key = "k";
        doThrow(new IOException()).when(mockUserDAO).findUsers(key);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(key);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException {
        // Setup
        User user = new User(3, "dmb", new Integer[][]{}, "");
        when(mockUserDAO.createUser(user)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());

    }

    @Test
    public void testCreateUserConflict() throws IOException {
        // Setup
        User user = new User(1, "jjb", new Integer[][]{}, "");
        when(mockUserDAO.createUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void testUpdateUser() throws IOException {
        // Setup
        User user = new User(1, "jjb", new Integer[][]{}, "");
        User update = new User(1, "jjb7", new Integer[][]{}, "");
        when(mockUserDAO.updateUser(update)).thenReturn(update);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(update);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(update, response.getBody());

    }

    @Test
    public void testUpdateUserNotFound() throws IOException {
        // Setup
        User update = new User(1, "jjb7", new Integer[][]{}, "");
        when(mockUserDAO.updateUser(update)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(update);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    void testUpdateUserException() throws IOException {
        // Setup
        User user = new User(1, "jjb", new Integer[][]{}, "");
        doThrow(new IOException()).when(mockUserDAO).updateUser(user);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException {
        // Setup
        User user = new User(1, "jjb", new Integer[][]{}, "");
        when(mockUserDAO.deleteUser(user.getIndex())).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(user.getIndex());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        // Setup
        User user = new User(1, "jjb", new Integer[][]{}, "");
        when(mockUserDAO.deleteUser(user.getIndex())).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(user.getIndex());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserException() throws IOException {
        // Setup
        User user = new User(1, "jjb", new Integer[][]{}, "");
        doThrow(new IOException()).when(mockUserDAO).deleteUser(user.getIndex());

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(user.getIndex());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // @Test
    // public void testAddToCart() throws IOException {
    // // Setup
    // User user = new User(1, "jjb");
    // Jersey jersey = new Jersey(0, "dmb", 0, 0, "large", "red", 0);
    // int quantity = 1;

    // // Invoke
    // ResponseEntity<Integer> response = userController.addToCart(user, jersey);

    // // Analyze
    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // assertEquals(quantity, response.getBody());

    // }

    // @Test
    // public void testRemoveFromCart() throws IOException {
    // // Setup
    // User user = new User(1, "jjb");
    // Jersey jersey = new Jersey(0, "dmb", 0, 0, "large", "red", 0);
    // user.addToCart(jersey);

    // // Invoke
    // ResponseEntity<Jersey> response = userController.removeFromCart(user,
    // jersey);

    // // Analyze
    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // }

    // @Test
    // public void testRemoveFromCartotFound() throws IOException {
    // // Setup
    // User user = new User(1, "jjb");
    // Jersey jersey = new Jersey(0, "dmb", 0, 0, "large", "red", 0);

    // // Invoke
    // ResponseEntity<Jersey> response = userController.removeFromCart(user,
    // jersey);

    // // Analyze
    // assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    // }

}
