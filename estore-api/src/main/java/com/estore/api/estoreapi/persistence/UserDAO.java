package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.User;

public interface UserDAO {
    User[] getUsers() throws IOException;

    User getUser(int index) throws IOException;

    User getUserByName(String name) throws IOException;

    User[] findUsers(String containsText) throws IOException;

    User createUser(User user) throws IOException;

    User updateUser(User user) throws IOException;

    boolean deleteUser(int index) throws IOException;
}
