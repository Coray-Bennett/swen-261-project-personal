package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.persistence.DiscountDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;

public class JerseyControllerTest {
    private JerseyController jerseyController;
    private JerseyDAO mockJerseyDAO;
    private DiscountDAO mockDiscountDAO;
    /**
     * Before each test, create a new JerseyController object and inject
     * a mock Jersey DAO
     */
    @BeforeEach
    public void setupHeroController() {
        mockJerseyDAO = mock(JerseyDAO.class);
        mockDiscountDAO = mock(DiscountDAO.class);
        jerseyController = new JerseyController(mockJerseyDAO, mockDiscountDAO);
    }

    @Test
    public void testSearchJerseys() throws IOException {
        String key = "J";
        Jersey[] jerseys = new Jersey[2];

        jerseys[0] = new Jersey(11, "John", 44, 67.99, "Large", "Black", 2);
        jerseys[1] = new Jersey(12, "Jake", 99, 89.99, "Medium", "Black", 1);

        when(mockJerseyDAO.findJerseys(key)).thenReturn(jerseys);

        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys(key);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(jerseys, response.getBody());
    }

    @Test
    public void testSearchJerseysException() throws IOException {
        String key = "j";
        doThrow(new IOException()).when(mockJerseyDAO).findJerseys(key);

        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys(key);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateHero() throws IOException { // updateJersey may throw IOException
        // Setup
        Jersey jersey = new Jersey(17, "Josh", 87, 69.99, "Medium", "Black", 1);
        // when updateJersey is called, return true simulating successful
        // update and save
        when(mockJerseyDAO.updateJersey(jersey)).thenReturn(jersey);
        ResponseEntity<Jersey> response = jerseyController.updateJersey(jersey);
        jersey.setName("Sam");

        // Invoke
        response = jerseyController.updateJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jersey, response.getBody());
    }

    @Test
    public void testUpdateHeroFailed() throws IOException { // updateJersey may throw IOException
        // Setup
        Jersey jersey = new Jersey(17, "Carlos", 77, 79.99, "Large", "Black", 1);
        // when updateJersey is called, return true simulating successful
        // update and save
        when(mockJerseyDAO.updateJersey(jersey)).thenReturn(null);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.updateJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateHeroHandleException() throws IOException { // updateHero may throw IOException
        // Setup
        Jersey jersey = new Jersey(17, "Carlos", 77, 79.99, "Large", "Black", 1);
        // When updateHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockJerseyDAO).updateJersey(jersey);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.updateJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteHero() throws IOException { // deleteJersey may throw IOException
        // Setup
        int jerseyId = 12;
        // when deleteJersey is called return true, simulating successful deletion
        when(mockJerseyDAO.deleteJersey(jerseyId)).thenReturn(true);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteHeroNotFound() throws IOException { // deleteJersey may throw IOException
        // Setup
        int jerseyId = 99;
        // when deleteJersey is called return false, simulating failed deletion
        when(mockJerseyDAO.deleteJersey(jerseyId)).thenReturn(false);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteHeroHandleException() throws IOException { // deleteJersey may throw IOException
        // Setup
        int jerseyId = 99;
        // When deleteJersey is called on the Mock Jersey DAO, throw an IOException
        doThrow(new IOException()).when(mockJerseyDAO).deleteJersey(jerseyId);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateHero() throws IOException {
        // Manually create Jersey Object
        Jersey jersey = new Jersey(1, "Bob", 29, 49.99, "Large", "Black", 20);

        // Return true to simulate succesful creation and save
        when(mockJerseyDAO.createJersey(jersey)).thenReturn(jersey);

        // creates Jersey through mockJersey
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // compare Jersey Object and HTTP response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(jersey, response.getBody());
    }

    @Test
    public void testCreateHeroConflict() throws IOException {
        // Manually create Jersey Object
        Jersey jersey = new Jersey(1, "Bob", 29, 49.99, "Large", "Black", 20);

        // return false to simulate unsuccessful create and save
        when(mockJerseyDAO.createJersey(jersey)).thenReturn(null);

        // attempts to create jersey through mockjersey
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // compare HTTP status
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testCreateHeroFailure() throws IOException { 
        // Manually create Jersey Object
        Jersey jersey = new Jersey(1, "Bob", 29, 49.99, "Large", "Black", 20);

        // Throws IOexpception when call is made
        doThrow(new IOException()).when(mockJerseyDAO).createJersey(jersey);

        // attempt to create jersey throigh mockjersey
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // compare HTTP status
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetSingleJersey() throws IOException {
        // Setup
        Jersey jersey = new Jersey(11, "Audi", 12, 49.99, "Large", "Red", 10);
        when(mockJerseyDAO.getJersey(jersey.getIndex())).thenReturn(jersey);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.getJersey(jersey.getIndex());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jersey, response.getBody());
    }

    @Test
    public void testGetSingleJerseyNotFound() throws IOException {
        // Setup
        int index = 99;
        when(mockJerseyDAO.getJersey(index)).thenReturn(null);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.getJersey(index);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void testGetSingleJerseyException() throws IOException {
        //Setup
        int index = 99;
        doThrow(new IOException()).when(mockJerseyDAO).getJersey(index);

        //Invoke
        ResponseEntity<Jersey> response = jerseyController.getJersey(index);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetEntireInventory() throws IOException {
        // Setup
        Jersey one = new Jersey(99, "Baillie", 28, 99.99, "Large", "Black", 10);
        Jersey two = new Jersey(8, "Barros", 8, 450.00, "XXS", "red", 1);
        mockJerseyDAO.createJersey(one);
        mockJerseyDAO.createJersey(two);
        Jersey[] jerseys = { one, two };
        when(mockJerseyDAO.getJerseys()).thenReturn(jerseys);

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.getJerseys();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jerseys, response.getBody());
    }

    @Test
    public void testGetEntireInventoryHandleException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockJerseyDAO).getJerseys();

        //Invoke
        ResponseEntity<Jersey[]> response = jerseyController.getJerseys();

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
