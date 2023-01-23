package com.estore.api.estoreapi.model;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Discount on a single Jersey
 * 
 * @author Coray Bennett
 * 
 */
public class Discount {
    
    private static final Logger LOG = Logger.getLogger(Discount.class.getName());
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);

    static final String STRING_FORMAT = "Discount [index=%d, percent=%f, expiration=%s";

    @JsonProperty("index") private int index;
    @JsonProperty("percent") private double percent;
    @JsonProperty("expiration") private String expiration;
    @JsonIgnore private Calendar expirationCalendar;

    public Discount(@JsonProperty("index") int index, @JsonProperty("percent") double percent, 
        @JsonProperty("expiration") String expiration) throws ParseException {

            this.index = index;
            this.percent = percent;
            this.expiration = expiration;

            this.expirationCalendar = Calendar.getInstance();
            this.expirationCalendar.setTime(sdf.parse(this.expiration));
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) throws ParseException {
        this.expirationCalendar.setTime(sdf.parse(expiration));
        this.expiration = expiration;
    }

    public void setExpiration(int days) {
        Date newTime = this.expirationCalendar.getTime();
        newTime.setTime(newTime.getTime() + days * 86400000); //number of milliseconds in a day
        this.expirationCalendar.setTime(newTime);

        this.expiration = sdf.format(newTime);
    }

    public Calendar getExpirationCalendar() {
        return expirationCalendar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,index,percent,expiration);
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Discount)
        {
            Discount j = (Discount)o;
            return (this.index == j.index);
        }
        return false;
    }

}
