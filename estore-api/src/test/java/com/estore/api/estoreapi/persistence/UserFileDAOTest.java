package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Executable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
public class UserFileDAOTest {

    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[4];
        testUsers[0] = new User(11, "John", new Integer[][]{}, "");
        testUsers[1] = new User(12, "Bobby", new Integer[][]{}, "");
        testUsers[2] = new User(13, "John", new Integer[][]{}, "");
        testUsers[3] = new User(14, "admin", new Integer[][]{}, "");

        when(mockObjectMapper
                .readValue(new File(""), User[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("", mockObjectMapper);
    }

    @Test
    public void testFindUsers() throws IOException {

        User[] result = userFileDAO.findUsers("John");
        assertEquals(2, result.length);
        assertEquals(testUsers[0], result[0]);
        assertEquals(testUsers[2], result[1]);

    }

    @Test
    public void testFindUsersNotFound() throws IOException {

        User[] result = userFileDAO.findUsers("sadfasdfasdfasdfasdf");
        assertEquals(0, result.length);

    }

    @Test
    public void testFindJerseysEmptyString() throws IOException {

        User[] result = userFileDAO.findUsers("");
        assertEquals(4, result.length);
        assertEquals(testUsers[0], result[0]);
        assertEquals(testUsers[1], result[1]);
        assertEquals(testUsers[2], result[2]);
        assertEquals(testUsers[3], result[3]);

    }

    @Test
    public void testCreateUser() throws IOException {
        User user = new User(15, "Ellie", new Integer[][]{}, "");

        User result = userFileDAO.createUser(user);

        assertNotNull(result);
        User actual = userFileDAO.findUsers("*" + user.getUsername())[0];
        assertEquals(actual.getIndex(), user.getIndex());
        assertEquals(actual.getUsername(), user.getUsername());
    }

    @Test
    public void testCreateUserConflict() throws IOException {
        // setup
        User user0 = new User(11, "Ellie", new Integer[][]{}, "");
        User user = new User(11, "Ellie", new Integer[][]{}, "");
        userFileDAO.createUser(user0);

        User result = userFileDAO.createUser(user);

        assertNull(result);

    }

    @Test
    public void testSaveException() throws IOException {
        // Setup & Invoke
        doThrow(new IOException())
                .when(mockObjectMapper)
                .writeValue(any(File.class), any(User[].class));

        User user = new User(11, "Adira", new Integer[][]{}, "");

        assertThrows(IOException.class,
                () -> userFileDAO.createUser(user),
                "IOException not thrown");
    }

    @Test
    public void testUpdateUser() throws IOException {
        // Setup
        User user = new User(12, "Gavin", new Integer[][]{}, "");

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getIndex());
        assertEquals(actual, user);
    }

    @Test
    public void testUpdateUserNotFound() throws IOException {
        // Setup
        User user = new User(88, "Alex", new Integer[][]{}, "");

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testDeleteUser() throws IOException {
        // Setup
        userFileDAO.createUser(new User(15, "Donovan", new Integer[][]{}, ""));
        boolean expected = true;

        User newUser = userFileDAO.findUsers("*" + "Donovan")[0];
        // Invoke
        boolean result = userFileDAO.deleteUser(newUser.getIndex());


        // Analyze
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        // Invoke
        boolean result = userFileDAO.deleteUser(99);

        // Analyze
        assertFalse(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);

        // Invoke
        doThrow(new IOException())
                .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"), User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                () -> new UserFileDAO("doesnt_matter.txt", mockObjectMapper),
                "IOException not thrown");
    }

}
