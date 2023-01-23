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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Discount;
import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.persistence.DiscountDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Jersey resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 *
 * @author SWEN Faculty
 * @author Samuel Lloyd
 * @author Coray Bennett
 * @author Joe Baillie
 * @author Chase Elliott
 * @date 9/27/2022
 */

@RestController
@RequestMapping("jerseys")
public class JerseyController {
    private static final Logger LOG = Logger.getLogger(JerseyController.class.getName());
    private JerseyDAO jerseyDao;
    private DiscountDAO discountDao;

    /**
     * Creates a REST API controller to reponds to requests
     *
     * @param jerseyDao The {@link JerseyDAO Jersey Data Access Object} to perform
     *                  CRUD operations
     *                  <br>
     *                  This dependency is injected by the Spring Framework
     */
    public JerseyController(JerseyDAO jerseyDao, DiscountDAO discountDao) {
        this.jerseyDao = jerseyDao;
        this.discountDao = discountDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Jersey Jersey} for the given id
     *
     * @param id The id used to locate the {@link Jersey Jersey}
     *
     * @return ResponseEntity with {@link Jersey Jersey} object and HTTP status of
     *         OK if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{index}")
    public ResponseEntity<Jersey> getJersey(@PathVariable int index) {
        LOG.info("GET /jerseys/" + index);
        try {

            Discount discount = discountDao.getDiscount(index);
            if (discount != null) {
                Calendar current = Calendar.getInstance();
                if (discount.getExpirationCalendar().getTimeInMillis() < current.getTimeInMillis()) {
                    discountDao.deleteDiscount(index);
                }
            }

            Jersey jersey = jerseyDao.getJersey(index);
            if (jersey != null)
                return new ResponseEntity<Jersey>(jersey, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Jersey Jerseys}
     *
     * @return ResponseEntity with array of {@link Jersey Jersey} objects (may be
     *         empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Jersey[]> getJerseys() {
        LOG.info("GET /jerseys");

        try {
            return new ResponseEntity<Jersey[]>(jerseyDao.getJerseys(), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Jersey Jerseys} whose name
     * contains
     * the text in name
     *
     * @param name The name parameter which contains the text used to find the
     *             {@link Jersey Jerseys}
     *
     * @return ResponseEntity with array of {@link Jersey Jersey} objects (may be
     *         empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         <p>
     *         Example: Find all jerseys that contain the text "ma"
     *         GET http://localhost:8080/jerseys/?key=ma
     */
    @GetMapping("/")
    public ResponseEntity<Jersey[]> searchJerseys(@RequestParam String key) {
        LOG.info("GET /jerseys/?key=" + key);
        try {
            Jersey[] jerseys = jerseyDao.findJerseys(key);
            return new ResponseEntity<Jersey[]>(jerseys, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Jersey jersey} with the provided jersey object
     *
     * @param Jersey - The {@link Jersey jersey} to create
     *
     * @return ResponseEntity with created {@link Jersey jersey} object and HTTP
     *         status of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Jersey jersey}
     *         object already exists<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     *         Example: when tested in insomnia
     *         http://localhost:8080/jerseys{Jersey in JSON format}
     */
    // @PostMapping(value = "", consumes = "application/JSON", produces =
    // "application/JSON")
    @PostMapping("")
    public ResponseEntity<Jersey> createJersey(@RequestBody Jersey jersey) {
        LOG.info("POST /jerseys/?jersey=" + jersey);

        try {
            Jersey myJersey = jerseyDao.createJersey(jersey);
            if (myJersey != null) {
                return new ResponseEntity<Jersey>(myJersey, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Jersey>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Jersey jersey} with the provided {@linkplain Jersey
     * jersey} object, if it exists
     *
     * @param jersey The {@link Jersey jersey} to update
     *
     * @return ResponseEntity with updated {@link Jersey jersey} object and HTTP
     *         status of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Jersey> updateJersey(@RequestBody Jersey jersey) {
        LOG.info("PUT /jerseys " + jersey);

        try {
            Jersey myJersey = jerseyDao.updateJersey(jersey);
            if (myJersey != null) {
                return new ResponseEntity<Jersey>(myJersey, HttpStatus.OK);
            } else {
                return new ResponseEntity<Jersey>(myJersey, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Jersey jersey} with the given id
     *
     * @param id The id of the {@link Jersey jersey} to deleted
     *
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{index}")
    public ResponseEntity<Jersey> deleteJersey(@PathVariable int index) {
        LOG.info("DELETE /jerseys/" + index);
        try {

            if (jerseyDao.deleteJersey(index)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}