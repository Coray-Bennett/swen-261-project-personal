package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Discount;
import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.persistence.DiscountDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;

public class DiscountControllerTest {
    private DiscountController discountController;
    private DiscountDAO mockDiscountDAO;
    private JerseyDAO mockJerseyDAO;

    /**
     * Before each test, create a new DiscountController object and inject
     * a mock Discount DAO
     */
    @BeforeEach
    public void setupDiscountController() {
        mockDiscountDAO = mock(DiscountDAO.class);
        mockJerseyDAO = mock(JerseyDAO.class);
       discountController = new DiscountController(mockDiscountDAO, mockJerseyDAO);
    }

    @Test
    public void testUpdateDiscount() throws IOException, ParseException { // updateDiscount may throw IOException
        // Setup
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");
        // when updateDiscount is called, return true simulating successful
        // update and save
        when(mockDiscountDAO.updateDiscount(discount)).thenReturn(discount);
        ResponseEntity<Discount> response = discountController.updateDiscount(discount);


        // Invoke
        response = discountController.updateDiscount(discount);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discount, response.getBody());
    }

    @Test
    public void testUpdateDiscountFailed() throws IOException, ParseException { // updateDiscount may throw IOException
        // Setup
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");
        // when updateDiscount is called, return true simulating successful
        // update and save
        when(mockDiscountDAO.updateDiscount(discount)).thenReturn(null);

        // Invoke
        ResponseEntity<Discount> response = discountController.updateDiscount(discount);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateDiscountHandleException() throws IOException, ParseException { // updateDiscount may throw IOException
        // Setup
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");
        // When updateDiscount is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockDiscountDAO).updateDiscount(discount);

        // Invoke
        ResponseEntity<Discount> response = discountController.updateDiscount(discount);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteDiscount() throws IOException { // deleteDiscount may throw IOException
        // Setup
        int id = 12;
        // when deleteDiscount is called return true, simulating successful deletion
        when(mockDiscountDAO.deleteDiscount(id)).thenReturn(true);

        // Invoke
        ResponseEntity<Discount> response = discountController.deleteDiscount(id);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteDiscountNotFound() throws IOException { // deleteDiscount may throw IOException
        // Setup
        int id = 99;
        // when deleteDiscount is called return false, simulating failed deletion
        when(mockDiscountDAO.deleteDiscount(id)).thenReturn(false);

        // Invoke
        ResponseEntity<Discount> response = discountController.deleteDiscount(id);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteDiscountHandleException() throws IOException { // deleteDiscount may throw IOException
        // Setup
        int id = 99;
        // When deleteDiscount is called on the Mock Discount DAO, throw an IOException
        doThrow(new IOException()).when(mockDiscountDAO).deleteDiscount(id);

        // Invoke
        ResponseEntity<Discount> response = discountController.deleteDiscount(id);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateDiscount() throws IOException, ParseException {
        // Manually create Discount Object
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");

        when(mockJerseyDAO.getJersey(discount.getIndex())).thenReturn(new Jersey(-1, null, 0, 0, null, null, 0));

        // Return true to simulate succesful creation and save
        when(mockDiscountDAO.createDiscount(discount)).thenReturn(discount);

        // creates Discount through mockDiscount
        ResponseEntity<Discount> response = discountController.createDiscount(discount);

        // compare Discount Object and HTTP response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(discount, response.getBody());
    }

    @Test
    public void testCreateDiscountConflict() throws IOException, ParseException {
        // Manually create Discount Object
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");

        when(mockJerseyDAO.getJersey(discount.getIndex())).thenReturn(new Jersey(-1, null, 0, 0, null, null, 0));

        // return false to simulate unsuccessful create and save
        when(mockDiscountDAO.createDiscount(discount)).thenReturn(null);

        // attempts to create Discount through mockDiscount
        ResponseEntity<Discount> response = discountController.createDiscount(discount);

        // compare HTTP status
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testCreateDiscountFailure() throws IOException, ParseException { 
        // Manually create Discount Object
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");

        when(mockJerseyDAO.getJersey(discount.getIndex())).thenReturn(new Jersey(-1, null, 0, 0, null, null, 0));

        // Throws IOexpception when call is made
        doThrow(new IOException()).when(mockDiscountDAO).createDiscount(discount);

        // attempt to create Discount throigh mockDiscount
        ResponseEntity<Discount> response = discountController.createDiscount(discount);

        // compare HTTP status
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testCreateDiscountNoJersey() throws IOException, ParseException {
        // Manually create Discount Object
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");

        //return null to simulate jersey that does not exist
        when(mockJerseyDAO.getJersey(discount.getIndex())).thenReturn(null);

        // Return true to simulate succesful creation and save
        when(mockDiscountDAO.createDiscount(discount)).thenReturn(discount);

        // attempt to create Discount throigh mockDiscount
        ResponseEntity<Discount> response = discountController.createDiscount(discount);

        // compare HTTP status
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetSingleDiscount() throws IOException, ParseException {
        // Setup
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");
        when(mockDiscountDAO.getDiscount(discount.getIndex())).thenReturn(discount);

        // Invoke
        ResponseEntity<Discount> response = discountController.getDiscount(discount.getIndex());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discount, response.getBody());
    }

    @Test
    public void testGetSingleDiscountNotFound() throws IOException {
        // Setup
        int index = 99;
        when(mockDiscountDAO.getDiscount(index)).thenReturn(null);

        // Invoke
        ResponseEntity<Discount> response = discountController.getDiscount(index);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void testGetSingleDiscountException() throws IOException {
        //Setup
        int index = 99;
        doThrow(new IOException()).when(mockDiscountDAO).getDiscount(index);

        //Invoke
        ResponseEntity<Discount> response = discountController.getDiscount(index);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetEntireDiscountList() throws IOException, ParseException {
        // Setup
        Discount one = new Discount(11, 10, "1-1-2023T00:00");
        Discount two = new Discount(12, 10, "1-1-2023T00:00");
        mockDiscountDAO.createDiscount(one);
        mockDiscountDAO.createDiscount(two);
        Discount[] Discounts = { one, two };
        when(mockDiscountDAO.getDiscounts()).thenReturn(Discounts);

        // Invoke
        ResponseEntity<Discount[]> response = discountController.getDiscounts();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Discounts, response.getBody());
    }

    @Test
    public void testGetEntireDiscountListHandleException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockDiscountDAO).getDiscounts();

        //Invoke
        ResponseEntity<Discount[]> response = discountController.getDiscounts();

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
