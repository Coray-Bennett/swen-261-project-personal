package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;

@Tag("Model-Tier")
public class UserTest {

    @Test
    public void testConstructorCustomer() throws IOException {
        // Setup
        int expectedIndex = 1;
        String expectedUsername = "jjb";
        boolean expectedStatus = false;
        Integer[][] expectedCart = {};

        // Invoke
        User user = new User(expectedIndex, expectedUsername, expectedCart, "");

        // Analyze
        assertEquals(expectedIndex, user.getIndex());
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedStatus, user.getStatus());
    }

    @Test
    public void testConstructorAdmin() throws IOException {
        // Setup
        int expectedIndex = 1;
        String expectedUsername = "admin";
        boolean expectedStatus = true;
        Integer[][] expectedCart = {};

        // Invoke
        User user = new User(expectedIndex, expectedUsername, expectedCart, "");

        // Analyze
        assertEquals(expectedIndex, user.getIndex());
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedStatus, user.getStatus());
    }

    @Test
    public void testToString() throws IOException {
        // Setup
        String STRING_FORMAT = "User[index = %d, username = %s, status = %b]";
        User user = new User(1, "jjb", new Integer[][]{}, "");
        String expectedString = String.format(STRING_FORMAT, user.getIndex(), user.getUsername(), user.getStatus());

        // Invoke
        String toString = user.toString();

        // Analyze
        assertEquals(expectedString, toString);

    }

    @Test
    public void testEqualsTrue() throws IOException {
        // Setup
        User user1 = new User(1, "admin", new Integer[][]{}, "");
        User user2 = new User(1, "admin", new Integer[][]{}, "");
        boolean expectedResponse = true;

        // Invoke
        boolean response = user1.equals(user2);

        // Analyze
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testEqualsFalse() throws IOException {
        // Setup
        User user1 = new User(1, "admin", new Integer[][]{}, "");
        User user2 = new User(1, "notAdmin", new Integer[][]{}, "");
        boolean expectedResponse = false;

        // Invoke
        boolean response = user1.equals(user2);

        // Analyze
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testEqualsFalseNotUser() throws IOException {
        // Setup
        User user1 = new User(1, "admin", new Integer[][]{}, "");
        Object user2 = new Object();
        boolean expectedResponse = false;

        // Invoke
        boolean response = user1.equals(user2);

        // Analyze
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testSetIndex() throws IOException {
        // Setup
        User user1 = new User(1, "admin", new Integer[][]{}, "");
        int setIndex = 2;
        int expectedResponse = setIndex;

        // Invoke
        user1.setIndex(setIndex);

        // Analyze
        assertEquals(expectedResponse, user1.getIndex());
    }

    @Test
    public void testSetUsername() throws IOException {
        // Setup
        User user1 = new User(1, "admin", new Integer[][]{}, "");
        String setUserName = "notAdmin";
        String expectedResponse = setUserName;

        // Invoke
        user1.setUsername(setUserName);

        // Analyze
        assertEquals(expectedResponse, user1.getUsername());
    }

    @Test
    public void testSetStatus() throws IOException {
        // Setup
        User user = new User(1, "admin", new Integer[][]{}, "");
        boolean setStatus = false;
        boolean expectedStatus = setStatus;

        // Invoke
        user.setStatus(expectedStatus);

        // Analyze
        assertEquals(expectedStatus, user.getStatus());
    }

}
