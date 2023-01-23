package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Discount;
import com.estore.api.estoreapi.persistence.DiscountDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Discount resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 *
 * @author SWEN Faculty
 * @author Coray Bennett
 * @date 10/17/2022
 */

@RestController
@RequestMapping("discounts")
public class DiscountController {
    private static final Logger LOG = Logger.getLogger(DiscountController.class.getName());
    private DiscountDAO discountDao;
    private JerseyDAO jerseyDao;
 
    /**
     * Creates a REST API controller to reponds to requests
     *
     * @param discountDao The {@link JerseyDAO Jersey Data Access Object} to perform CRUD operations
     * @param jerseyDao allows access to jersey info
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public DiscountController(DiscountDAO discountDao, JerseyDAO jerseyDao) {
        this.discountDao = discountDao;
        this.jerseyDao = jerseyDao;
    }
 
    /**
     * Responds to the GET request for a {@linkplain Discount Discount} for the given id
     *
     * @param index The id used to locate the {@link Discount Discount}
     *
     * @return ResponseEntity with {@link Discount Discount} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{index}")
    public ResponseEntity<Discount> getDiscount(@PathVariable int index) {
        LOG.info("GET /discounts/" + index);
        try {
            Discount discount = discountDao.getDiscount(index);
            if (discount != null)
                return new ResponseEntity<Discount>(discount,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    /**
     * Responds to the GET request for all {@linkplain Discount Discounts}
     *
     * @return ResponseEntity with array of {@link Discount Discount} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Discount[]> getDiscounts() {
        LOG.info("GET /discounts");
 
        try {
            return new ResponseEntity<Discount[]>(discountDao.getDiscounts(), HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    /**
     * Creates a {@linkplain Discount Discount} with the provided jersey object
     *
     * @param Discount - The {@link Discount Discount} to create
     *
     * @return ResponseEntity with created {@link Discount Discount} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Discount Discount} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     * Example: when tested in insomnia
     * http://localhost:8080/discounts{Discount in JSON format}
     */
    //@PostMapping(value = "", consumes = "application/JSON", produces = "application/JSON")
    @PostMapping("")
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        LOG.info("POST /discounts/?discount=" + discount);
        try {

            if(jerseyDao.getJersey(discount.getIndex()) == null) { //will not create discount for jersey that does not exist
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            //cool line of code
            Discount myDiscount = discountDao.getDiscount(discount.getIndex()) != null ? discountDao.updateDiscount(discount) : discountDao.createDiscount(discount);

            if(myDiscount != null){
                return new ResponseEntity<Discount>(myDiscount, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    /**
     * Updates the {@linkplain Discount Discount} with the provided {@linkplain Discount Discount} object, if it exists
     *
     * @param discount The {@link Discount Discount} to update
     *
     * @return ResponseEntity with updated {@link Discount Discount} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Discount> updateDiscount(@RequestBody Discount discount) {
        LOG.info("PUT /discounts " + discount);
 
        try {
            Discount myDiscount = discountDao.updateDiscount(discount);
            if(myDiscount != null){
                return new ResponseEntity<Discount>(myDiscount, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    /**
     * Deletes a {@linkplain Discount Discount} with the given id
     *
     * @param id The id of the {@link Discount Discount} to deleted
     *
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{index}")
    public ResponseEntity<Discount> deleteDiscount(@PathVariable int index) {
        LOG.info("DELETE /discounts/" + index);
        try {
 
            if(discountDao.deleteDiscount(index)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
           
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}