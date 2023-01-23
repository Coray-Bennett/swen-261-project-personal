package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Discount;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiscountFileDAOTest {
    DiscountFileDAO discountFileDAO;
    Discount[] testDiscounts;
    ObjectMapper mockObjectMapper;
    
    @BeforeEach
    public void setupDiscountFileDAO() throws IOException, ParseException {
        mockObjectMapper = mock(ObjectMapper.class);
        testDiscounts = new Discount[3];
        testDiscounts[0] =  new Discount(11, 10, "1-1-2023T00:00");
        testDiscounts[1] =  new Discount(12, 20, "1-1-2023T00:00");
        testDiscounts[2] =  new Discount(13, 30, "1-1-2023T00:00");

        when(mockObjectMapper
            .readValue(new File(""),Discount[].class))
                .thenReturn(testDiscounts);
        discountFileDAO = new DiscountFileDAO("",mockObjectMapper);
    }

    @Test
    public void testCreateDiscount() throws IOException, ParseException {
        Discount discount = new Discount(14, 40, "1-1-2023T00:00");
        Discount result = assertDoesNotThrow(() -> discountFileDAO.createDiscount(discount), "Unexpected exception thrown");

        assertNotNull(result);

        Discount actual = discountFileDAO.getDiscount(discount.getIndex());

        assertEquals(actual.getIndex(),discount.getIndex());
        assertEquals(actual.getPercent(),discount.getPercent());
        assertEquals(actual.getExpiration(),discount.getExpiration());
    }

    @Test
    public void testSaveException() throws IOException, ParseException{
        // Setup & Invoke
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Discount[].class));

                Discount discount = new Discount(14, 40, "1-1-2023T00:00");

        assertThrows(IOException.class,
                        () -> discountFileDAO.createDiscount(discount),
                        "IOException not thrown");
    }

    @Test
    public void testUpdateDiscount() throws ParseException, IOException {
        // Setup
        Discount discount = new Discount(13, 40, "1-1-2023T00:00");

        // Invoke
        Discount result = assertDoesNotThrow(() -> discountFileDAO.updateDiscount(discount),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Discount actual = discountFileDAO.getDiscount(discount.getIndex());
        assertEquals(actual,discount);
    }

    @Test
    public void testDeleteDiscountNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> discountFileDAO.deleteDiscount(101),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(discountFileDAO.discounts.size(), testDiscounts.length);
    }

    @Test
    public void testUpdateDiscountNotFound() throws ParseException {
        // Setup
        Discount discount = new Discount(15, 40, "1-1-2023T00:00");

        // Invoke
        Discount result = assertDoesNotThrow(() -> discountFileDAO.updateDiscount(discount),
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
                .readValue(new File(""),Discount[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new DiscountFileDAO("",mockObjectMapper),
                        "IOException not thrown");
    }









    
}
