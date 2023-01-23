package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Jerseys
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author Joe Baillie
 */
@Component
public class UserFileDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());
    Map<Integer, User> users;

    private ObjectMapper objectMapper;

    private static int nextIndex;
    private String filename;

    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    private boolean load() throws IOException {
        users = new TreeMap<>();
        nextIndex = 0;

        User[] userArray = objectMapper.readValue(new File(filename), User[].class);

        for (User user : userArray) {
            users.put(user.getIndex(), user);
            if (user.getIndex() > nextIndex) {
                nextIndex = user.getIndex();
            }

        }
        ++nextIndex;
        return true;
    }

    private synchronized static int nextIndex() {
        int index = nextIndex;
        ++nextIndex;
        return index;
    }

    private User[] getUserArray() {
        return getUserArray(null);
    }

    private User[] getUserArray(String containsText) {
        ArrayList<User> userArrayList = new ArrayList<User>();
        for (User user : users.values()) {
            userArrayList.add(user);
        }
        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    private boolean save() throws IOException {
        User[] userArray = getUserArray();
        objectMapper.writeValue(new File(filename), userArray);
        return true;
    }

    @Override
    public User[] getUsers() throws IOException {
        synchronized (users) {
            return getUserArray();
        }
    }

    @Override
    public User getUser(int index) throws IOException {
        synchronized (users) {
            if (users.containsKey(index)) {
                return users.get(index);
            }
            return null;
        }
    }   

    @Override
    public User getUserByName(String name) throws IOException {
        synchronized (users) {
            for(Integer index : users.keySet()) {
                if(users.get(index).getUsername().equals(name)) {
                    return users.get(index);
                }
            }
            return null;
        }
    }

    @Override
    public User createUser(User user) throws IOException {
        synchronized (users) {
        
            User newUser = new User(nextIndex(), user.getUsername(), user.getCart(), user.getPasswordHash());

            User[] userArray = findUsers("");
            for (User item : userArray) {
                if (item.equals(newUser)) {
                    newUser = null;
                    nextIndex--;
                }
            }
            if (newUser != null) {
                users.put(newUser.getIndex(), newUser);
                save();
            }
            return newUser;
        }
    }

    @Override
    public User updateUser(User user) throws IOException {
        
        synchronized (users) {
            if (users.containsKey(user.getIndex()) == false)
                return null;  // user does not exist

            users.put(user.getIndex(),user);
            save(); // may throw an IOException
            return user;
            // if (users.containsKey(user.getIndex())) {
            //     users.put(user.getIndex(), user);
            //     save();
            //     return user;
            // }
            // return null;
        }
    }

    @Override
    public boolean deleteUser(int index) throws IOException {
        synchronized (users) {
            if (users.containsKey(index)) {
                users.remove(index);
                return save();
            } else
                return false;
        }
    }

    @Override
    public User[] findUsers(String containsText) throws IOException {

        if(containsText.length() > 0 && containsText.charAt(0) == '*') {
            User[] user = new User[1];
            user[0] = getUserByName(containsText.substring(1));
            return user;
        }
        
        ArrayList<User> userArrayList = new ArrayList<>();
        synchronized (users) {

            for (User user : users.values()) {

                String searching = user.getIndex() + " " + user.getUsername();
                String[] keys = containsText.split(" ");

                boolean match = true;
                for (String key : keys) {
                    if (!searching.contains(key)) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    userArrayList.add(user);
                }
            }

        }
        User[] userArray = new User[userArrayList.size()];
        return userArrayList.toArray(userArray);
    }

}
