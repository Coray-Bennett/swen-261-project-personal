package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Discount;

public interface DiscountDAO {
    Discount[] getDiscounts() throws IOException;

    Discount getDiscount(int index) throws IOException;

    Discount createDiscount(Discount discount) throws IOException;

    Discount updateDiscount(Discount discount) throws IOException;

    boolean deleteDiscount(int index) throws IOException;
}
