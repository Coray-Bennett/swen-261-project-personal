package com.estore.api.estoreapi.controller;

import org.apache.catalina.connector.Response;
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
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.DiscountDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;
import com.estore.api.estoreapi.persistence.UserDAO;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Joe Baillie
 */

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDao;
    private JerseyDAO jerseyDao;
    private DiscountDAO discountDao;

    public UserController(UserDAO userDao, JerseyDAO jerseyDao, DiscountDAO discountDao) {
        this.userDao = userDao;
        this.jerseyDao = jerseyDao;
        this.discountDao = discountDao;
    }

    @GetMapping("/{index}")
    public ResponseEntity<User> getUser(@PathVariable int index) {
        LOG.info("GET /users/" + index);
        try {
            User user = userDao.getUser(index);
            if (user != null) {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByName(@PathVariable String username) {
        LOG.info("GET /users/" + username);
        try {
            User user = userDao.getUserByName(username);
            if (user != null) {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users");
        try {
            return new ResponseEntity<User[]>(userDao.getUsers(), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<User[]> searchUsers(@RequestParam String key) {
        LOG.info("GET /users/?key=" + key);
        try {
            User[] users = userDao.findUsers(key);
            return new ResponseEntity<User[]>(users, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /users/?user=" + user);
        try {
            User addedUser = userDao.createUser(user);
            if (addedUser != null) {
                return new ResponseEntity<User>(addedUser, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    @PutMapping("/cart")
    public ResponseEntity<Integer> addToCart(@RequestBody User user, Jersey jersey) {
        LOG.info("PUT /cart");
        try {
            Integer response = user.addToCart(jersey);
            return new ResponseEntity<Integer>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

    // @PutMapping("/cart") public ResponseEntity<Integer> addToCart(@RequestBody User user, 
    // Integer index) {
    //     LOG.info("PUT /users/" + user + "cart/" + index);
    //     try {
    //         Integer response = user.addToCart(index, );
    //     return new ResponseEntity<Integer>(response, HttpStatus.OK);
    //     } catch (Exception e) {
    //     LOG.log(Level.SEVERE, e.getLocalizedMessage());
    //     return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }


    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        LOG.info("PUT /users");
        try {
            User updatedUser = userDao.updateUser(user);
            if (updatedUser != null) {
                return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{index}")
    public ResponseEntity<User> deleteUser(@RequestParam int index) {
        LOG.info("DELETE /users/" + index);
        try {
            if (userDao.deleteUser(index)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 
    @DeleteMapping("/{user}/cart/{jersey}")
    public ResponseEntity<Jersey> removeFromCart(@RequestBody User user, Jersey jersey) {
        LOG.info("DELETE/user/cart/" + jersey);
        try {
            if (user.removeFromCart(jersey.getIndex()) != -1) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    // @DeleteMapping("/{user}/cart/{jersey}")
    // public ResponseEntity<Jersey> removeFromCart(@RequestBody User user, Jersey
    // jersey) {
    // LOG.info("DELETE/user/cart/" + jersey);
    // try {
    // if (user.removeFromCart(jersey.getIndex()) != -1) {
    // return new ResponseEntity<>(HttpStatus.OK);
    // } else {
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }
    // } catch (Exception e) {
    // LOG.log(Level.SEVERE, e.getLocalizedMessage());
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

}
