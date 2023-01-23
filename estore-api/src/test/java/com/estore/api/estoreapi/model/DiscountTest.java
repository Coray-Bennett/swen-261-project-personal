package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.util.Calendar;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class DiscountTest {

    @Test
    public void testConstructor() throws ParseException {
        //Setup
        String expected_exp = "1-1-2023T00:00";
        Calendar expected_exp_cal = Calendar.getInstance();
        expected_exp_cal.setTime(Discount.sdf.parse(expected_exp));
        int expected_index = 11;
        int expected_percent = 10;

        //Invoke
        Discount actual = new Discount(11, 10, "1-1-2023T00:00");

        //Analyze
        assertEquals(actual.getIndex(), expected_index);
        assertEquals(actual.getPercent(), expected_percent);
        assertEquals(actual.getExpiration(), expected_exp);
        assertEquals(actual.getExpirationCalendar().getTime(), expected_exp_cal.getTime());

    }


    @Test
    public void testInvalidDate() throws ParseException {
        //Setup
        Discount discount = new Discount(11, 10, "1-1-2023T00:00");
        
        String initial = "1-1-2023T00:00";
        String exp = "aaaa";

        // Invoke/Analyze
        try {
            discount.setExpiration(exp);
            fail();
        }
        catch(ParseException e) {
            assertEquals(initial, discount.getExpiration());
        }
        
    }


    
}
