package com.estore.api.estoreapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Jersey entity
 * 
 * @author SWEN Faculty
 */
public class Jersey {
    private static final Logger LOG = Logger.getLogger(Jersey.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Jersey [index=%d, name=%s, number=%d, price=%.2f, size=%s, color=%s, quantity=%d]";

    @JsonProperty("index") private int index;
    @JsonProperty("name") private String name;
    @JsonProperty("number") private int number;
    @JsonProperty("price") private double price;
    @JsonProperty("size") private String size;
    @JsonProperty("color") private String color;
    @JsonProperty("quantity") private int quantity;
    

    /**
     * Create a jersey
     */
    public Jersey(@JsonProperty("index") int index, @JsonProperty("name") String name, @JsonProperty("number") int number, 
        @JsonProperty("price") double price, @JsonProperty("size") String size, @JsonProperty("color") String color,  @JsonProperty("quantity") int quantity) {
        
        this.index = index;
        this.name = name;
        this.number = number;
        this.price = price;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
    }

    //getters
    public int getIndex() {return index;}
    public String getName() {return name;}
    public int getNumber() {return number;}
    public double getPrice() {return price;}
    public String getSize() {return size;}
    public String getColor() {return color;}
    public int getQuantity() {return quantity;}

    //setter
    public void setName(String name) {this.name = name;}
    public void setSize(String size) {this.size = size;}
    public void setIndex(int index) {this.index = index;}
    public void setNumber(int number) {this.number = number;}
    public void setPrice(double price) {this.price = price;}
    public void setColor(String color) {this.color = color;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,index,name,number,price,size,color,quantity);
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Jersey)
        {
            Jersey j = (Jersey)o;
            return ((name.equals(j.name)) && (number == j.number) && (price == j.price) && (size.equals(j.size)) && (color.equals(j.color)));
        }
        return false;
    }
}