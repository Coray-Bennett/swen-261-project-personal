package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Jersey;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
public class JerseyFileDAOTest {

    JerseyFileDAO jerseyFileDAO;
    Jersey[] testJerseys;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupHeroFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testJerseys = new Jersey[3];
        testJerseys[0] = new Jersey(11, "John", 44, 67.99, "Large", "Black", 2);
        testJerseys[1] = new Jersey(12, "Bobby", 33, 67.99, "Small", "Black", 8);
        testJerseys[2] = new Jersey(13, "John", 44, 67.99, "Small", "Yellow", 3);

        when(mockObjectMapper
                .readValue(new File(""), Jersey[].class))
                .thenReturn(testJerseys);
        jerseyFileDAO = new JerseyFileDAO("", mockObjectMapper);
    }

    @Test
    public void testFindJerseys() throws IOException {

        Jersey[] result = jerseyFileDAO.findJerseys("John");
        assertEquals(2, result.length);
        assertEquals(testJerseys[0], result[0]);
        assertEquals(testJerseys[2], result[1]);

    }

    @Test
    public void testFindJerseysNotFound() throws IOException {

        Jersey[] result = jerseyFileDAO.findJerseys("sadfasdfasdfasdfasdf");
        assertEquals(0, result.length);

    }

    @Test
    public void testFindJerseysEmptyString() throws IOException {

        Jersey[] result = jerseyFileDAO.findJerseys("");
        assertEquals(3, result.length);
        assertEquals(testJerseys[0], result[0]);
        assertEquals(testJerseys[1], result[1]);
        assertEquals(testJerseys[2], result[2]);

    }

    @Test
    public void testFindJerseysMultipleKeysA() throws IOException {

        Jersey[] result = jerseyFileDAO.findJerseys("John Large");
        assertEquals(1, result.length);
        assertEquals(testJerseys[0], result[0]);

    }

    @Test
    public void testFindJerseysMultipleKeysB() throws IOException {

        Jersey[] result = jerseyFileDAO.findJerseys("Small John");
        assertEquals(1, result.length);
        assertEquals(testJerseys[2], result[0]);

    }

    @Test
    public void testCreateJersey() throws IOException {
        Jersey jersey = new Jersey(11, "John", 44, 67.99, "Large", "White", 2);

        Jersey result = assertDoesNotThrow(() -> jerseyFileDAO.createJersey(jersey), "Unexpected exception thrown");

        assertNotNull(result);
        Jersey actual = jerseyFileDAO.getJersey(jersey.getIndex());
        assertEquals(actual.getIndex(), jersey.getIndex());
        assertEquals(actual.getName(), jersey.getName());
    }

    @Test
    public void testSaveException() throws IOException {
        // Setup & Invoke
        doThrow(new IOException())
                .when(mockObjectMapper)
                .writeValue(any(File.class), any(Jersey[].class));

        Jersey jersey = new Jersey(11, "John", 44, 67.99, "Large", "White", 2);

        assertThrows(IOException.class,
                () -> jerseyFileDAO.createJersey(jersey),
                "IOException not thrown");
    }

    @Test
    public void testUpdateJersey() {
        // Setup
        Jersey jersey = new Jersey(11, "John", 44, 70.99, "Large", "White", 2);

        // Invoke
        Jersey result = assertDoesNotThrow(() -> jerseyFileDAO.updateJersey(jersey),
                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Jersey actual = jerseyFileDAO.getJersey(jersey.getIndex());
        assertEquals(actual, jersey);
    }

    @Test
    public void testDeleteJerseyNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> jerseyFileDAO.deleteJersey(98),
                "Unexpected exception thrown");

        // Analyze
        assertEquals(result, false);
        assertEquals(jerseyFileDAO.jerseys.size(), testJerseys.length);
    }

    @Test
    public void testUpdateJerseyNotFound() {
        // Setup
        Jersey jersey = new Jersey(88, "John", 44, 67.99, "Large", "White", 2);

        // Invoke
        Jersey result = assertDoesNotThrow(() -> jerseyFileDAO.updateJersey(jersey),
                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);

        // Invoke
        doThrow(new IOException())
                .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"), Jersey[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                () -> new JerseyFileDAO("doesnt_matter.txt", mockObjectMapper),
                "IOException not thrown");
    }

}
