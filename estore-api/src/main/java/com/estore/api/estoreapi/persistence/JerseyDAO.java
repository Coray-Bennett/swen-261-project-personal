package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Jersey;

/**
 * Defines the interface for Jersey object persistence
 * 
 * @author SWEN Faculty
 */
public interface JerseyDAO {
    Jersey[] getJerseys() throws IOException;

    /**
     * 
     * given some search parameters/keys (separated by space characters), returns an array of all jerseys that contain ALL of the search keys
     * 
     * @param containsText
     * @return Jersey[] : an array of jerseys; these jerseys must contain EVERY key in containsText 
     * @throws IOException
     */
    Jersey[] findJerseys(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Jersey jersey} with the given id
     * 
     * @param id The id of the {@link Jersey jersey} to get
     * 
     * @return a {@link Jersey jersey} object with the matching id
     * <br>
     * null if no {@link Jersey jersey} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey getJersey(int index) throws IOException;

    /**
     * Creates and saves a {@linkplain Jersey jersey}
     * 
     * @param jersey {@linkplain Jersey jersey} object to be created and saved
     * 
     * The Id for the Jersey is created through the nextIndex() method
     * all other information is takne from passed jersey object
     *
     * @return new {@link Jersey jersey} if successful, false otherwise 
     * 
     * @throws IOException if an issue occurs within the save() method
     */
    Jersey createJersey(Jersey jersey) throws IOException;

    /**
     * Updates and saves a {@linkplain Jersey jersey}
     * 
     * @param {@link Jersey jersey} object to be updated and saved
     * 
     * @return updated {@link Jersey jersey} if successful, null if
     * {@link Jersey jersey} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Jersey updateJersey(Jersey jersey) throws IOException;

    /**
     * Deletes a {@linkplain Jersey jersey} with the given id
     * 
     * @param id The id of the {@linkplain Jersey jersey}
     * 
     * @return true if the {@link Jersey jersey} was deleted
     * <br>
     * false if the {@link Jersey jersey} with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteJersey(int index) throws IOException;
}