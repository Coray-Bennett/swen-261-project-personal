package com.estore.api.estoreapi.model;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents a User entity
 * 
 * @author Joe Baillie
 */

public class User {

    static final boolean CUSTOMER = false;
    static final boolean ADMIN = true;

    private static final Logger LOG = Logger.getLogger(User.class.getName());

    static final String STRING_FORMAT = "User[index = %d, username = %s, status = %b]";

    @JsonProperty("index")
    private int index;
    @JsonProperty("username")
    private String username;
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("cart")
    private Integer[][] cart;   
    @JsonProperty("passwordHash")
    private String passwordHash;


    /**
     * Create a user
     * If username is "admin" in any case variation, it will automatically be given
     * admin status of "true"
     * If username is anything but admin, it will automatically be given the
     * customer status of "false"
     */
    public User(@JsonProperty("index") int index, @JsonProperty("username") String username, 
        @JsonProperty("cart") Integer[][] cart, @JsonProperty("passwordHash") String passwordHash) {

        this.index = index;
        this.username = username;
        this.cart = cart;
        if (username.equalsIgnoreCase("admin")) {
            status = true;
        } else {
            status = false;
        }
        this.passwordHash = passwordHash;
    }

    /**
     * Getters
     */
    public int getIndex() {
        return index;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean getStatus() {
        return status;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Setters
     */

    public void setIndex(int index) {
        this.index = index;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, index, username, status);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User) o;
            return (this.username.equals(u.username));
        }
        return false;
    }

    public Integer[][] getCart() {
        return cart;
    }

   


    //     for(Integer[] item : cart) {
    //         if(item[0] == index) {
    //             item[1] += quantity;
    //             return true;
    //         }
    //         cartList.add(item);
    //     }

    //     cartList.add(new Integer[] {index, quantity});

    //     Integer[][] newCart = new Integer[cartList.size()][2];
    //     cart = cartList.toArray(newCart);


    //     return true;

    // }

    /**
     * @param jersey
     *               Take jersey and add it to a user's shopping cart list
     */

    // public Integer addToCart(Jersey jersey) {
    // try {
    // if (this.shoppingCart.get(jersey.getIndex()) == null) {
    // this.shoppingCart.put(jersey.getIndex(), 1);
    // return this.shoppingCart.get(jersey.getIndex());
    // }
    // this.shoppingCart.put(jersey.getIndex(), shoppingCart.get(jersey.getIndex() +
    // 1));
    // return shoppingCart.get(jersey.getIndex());
    // } catch (Exception e) {
    // return null;
    // }

    // }


    // public HashMap<Integer, Integer> addToShoppingCart(Jersey jersey, int
    // quantity) {
    // if (shoppingCart.get(jersey.getIndex()) == null) {
    // shoppingCart.put(jersey.getIndex(), quantity);
    // } else {
    // shoppingCart.put(jersey.getIndex(), shoppingCart.get(jersey.getIndex()) +
    // quantity);
    // }
    // return shoppingCart;
    // }

    // public HashMap<Integer, Integer> removeFromShoppingCart(Jersey jersey) {
    // if (shoppingCart.get(jersey.getIndex()) != null) {
    // shoppingCart.remove(jersey.getIndex());
    // }
    // return shoppingCart;
    // }

    // NEED TO CHECK FOR DELETION

    // public Integer removeFromCart(int key) {
    // if (this.shoppingCart.get(key) == null) {
    // return -1;
    // } else if (shoppingCart.get(key) == 1) {
    // this.shoppingCart.remove(key);
    // return 0;
    // }
    // this.shoppingCart.put(key, shoppingCart.get(key) - 1);
    // return this.shoppingCart.get(key);

    // }

    // public JSONPObject getShoppingCart() {
    // return shoppingCart;
    // }
}
