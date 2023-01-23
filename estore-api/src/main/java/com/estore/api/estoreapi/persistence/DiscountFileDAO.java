package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.estore.api.estoreapi.model.Discount;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscountFileDAO implements DiscountDAO {

    private static final Logger LOG = Logger.getLogger(DiscountFileDAO.class.getName());
    Map<Integer,Discount> discounts;   // Provides a local cache of the discount objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between discount
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to


    public DiscountFileDAO(@Value("${discounts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the discounts from the file
    }

    private boolean load() throws IOException {
        discounts = new TreeMap<>();

        Discount[] discountArray = objectMapper.readValue(new File(filename),Discount[].class);

        // Add each discount to the tree map
        for (Discount discount : discountArray) {
            discounts.put(discount.getIndex(),discount);
        }

        return true;
    }


    private Discount[] getDiscountArray() {
        return getDiscountArray(null);
    }

    private Discount[] getDiscountArray(String containsText) { // if containsText == null, no filter
        ArrayList<Discount> discountArrayList = new ArrayList<>();

        for (Discount discount : discounts.values()) {
            discountArrayList.add(discount);
        }

        Discount[] DiscountArray = new Discount[discountArrayList.size()];
        discountArrayList.toArray(DiscountArray);
        return DiscountArray;
    }

    private boolean save() throws IOException {
        Discount[] discountArray = getDiscountArray();
        objectMapper.writeValue(new File(filename),discountArray);
        return true;
    }

    @Override
    public Discount[] getDiscounts() throws IOException {
        synchronized(discounts) {
            return getDiscountArray();
        }
    }

    @Override
    public Discount getDiscount(int index) throws IOException {
        synchronized(discounts) {
            if (discounts.containsKey(index))
                return discounts.get(index);
            else
                return null;
        }
    }

    @Override
    public Discount createDiscount(Discount discount) throws IOException {
        synchronized(discounts) {
            Discount newDiscount;
            try {
                newDiscount = new Discount(discount.getIndex(), discount.getPercent(), discount.getExpiration());
            } catch (ParseException e) {
                newDiscount = null;
            }

            Discount[] discountArray = getDiscountArray();
            for(Discount item : discountArray) {
                if(item.equals(newDiscount)) {
                    newDiscount = null;
                }
            }

            if(newDiscount != null) {
                discounts.put(newDiscount.getIndex(), newDiscount);
                save(); // may throw an IOException
            }

            return newDiscount;
        }
    }

    @Override
    public Discount updateDiscount(Discount discount) throws IOException {
        synchronized(discounts) {
            if (discounts.containsKey(discount.getIndex()) == false)
                return null;  // jersey does not exist

            discounts.put(discount.getIndex(),discount);
            save(); // may throw an IOException
            return discount;
        }
    }

    @Override
    public boolean deleteDiscount(int index) throws IOException {
        synchronized(discounts) {
            if (discounts.containsKey(index)) {
                discounts.remove(index);
                return save();
            }
            else
                return false;
        }
    }
    
}
