package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class JerseyTest {
    @Test
    public void testConstructor() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        // Invoke
        Jersey jersey = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);

        // Analyze
        assertEquals(expected_name,jersey.getName());
        assertEquals(expected_number,jersey.getNumber());
        assertEquals(expected_price,jersey.getPrice());
        assertEquals(expected_size,jersey.getSize());
        assertEquals(expected_color,jersey.getColor());
    }

    @Test
    public void testToString() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";
        String expected_string = String.format(Jersey.STRING_FORMAT,0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);

        // Invoke
        String actual_string = jersey.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }

    @Test
    public void testEqualsTrue() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        //invoke
        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);

        // Analyze
        assertEquals(jersey1,jersey2);
    }

    @Test
    public void testEqualsTrueQuantity() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        //invoke
        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 50);

        // Analyze
        assertEquals(jersey1,jersey2);
    }

    @Test
    public void testEqualsFalseNotJersey() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        //invoke
        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Object jersey2 = new Object(); 

        // Analyze
        assertFalse(jersey1.equals(jersey2));
    }

    @Test
    public void testEqualsFalseName() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        //invoke
        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,"Jeff", expected_number, expected_price, expected_size, expected_color, 10);

        // Analyze
        assertFalse(jersey1.equals(jersey2));
    }

    @Test
    public void testEqualsFalseNumber() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        //invoke
        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, 10000, expected_price, expected_size, expected_color, 10);

        // Analyze
        assertFalse(jersey1.equals(jersey2));
    }

    @Test
    public void testEqualsFalsePrice() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        //invoke
        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, 40.00, expected_size, expected_color, 10);

        // Analyze
        assertFalse(jersey1.equals(jersey2));
    }

    @Test
    public void testEqualsFalseSize() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        //invoke
        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, expected_price, "SMALL", expected_color, 10);

        // Analyze
        assertFalse(jersey1.equals(jersey2));
    }

    @Test
    public void testEqualsFalseColor() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        //invoke
        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, "WHITE", 10);

        // Analyze
        assertFalse(jersey1.equals(jersey2));
    }

    @Test
    public void testSetName() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,"John", expected_number, expected_price, expected_size, expected_color, 10);

        //invoke
        jersey2.setName(expected_name);

        // Analyze
        assertEquals(jersey1,jersey2);
    }

    @Test
    public void testSetNumber() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, 10000, expected_price, expected_size, expected_color, 10);

        //invoke
        jersey2.setNumber(expected_number);

        // Analyze
        assertEquals(jersey1,jersey2);
    }

    @Test
    public void testSetPrice() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, 55.55, expected_size, expected_color, 10);

        //invoke
        jersey2.setPrice(expected_price);

        // Analyze
        assertEquals(jersey1,jersey2);
    }

    @Test
    public void testSetSize() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, expected_price, "SMALL", expected_color, 10);

        //invoke
        jersey2.setSize(expected_size);

        // Analyze
        assertEquals(jersey1,jersey2);
    }

    @Test
    public void testSetColor() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, "WHITE", 10);

        //invoke
        jersey2.setColor(expected_color);

        // Analyze
        assertEquals(jersey1,jersey2);
    }

    @Test
    public void testSetQuantity() {
        // Setup
        String expected_name = "Joseph";
        int expected_number = 45;
        double expected_price = 39.99;
        String expected_size = "LARGE";
        String expected_color = "BLACK";

        Jersey jersey1 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10);
        Jersey jersey2 = new Jersey(0,expected_name, expected_number, expected_price, expected_size, expected_color, 10000);

        //invoke
        jersey2.setQuantity(10);

        // Analyze
        assertEquals(jersey1.getQuantity(),jersey2.getQuantity());
    }
}