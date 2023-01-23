package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.Jersey;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Jerseys
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component
public class JerseyFileDAO implements JerseyDAO {
    private static final Logger LOG = Logger.getLogger(JerseyFileDAO.class.getName());
    Map<Integer,Jersey> jerseys;   // Provides a local cache of the jersey objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between jersey
                                        // objects and JSON text format written
                                        // to the file
    private static int nextIndex;  // The next Id to assign to a new jersey
    private String filename;    // Filename to read from and write to

    public JerseyFileDAO(@Value("${jerseys.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the jerseys from the file
    }

    private boolean load() throws IOException {
        jerseys = new TreeMap<>();
        nextIndex = 0;

        Jersey[] jerseyArray = objectMapper.readValue(new File(filename),Jersey[].class);

        // Add each jersey to the tree map and keep track of the greatest id
        for (Jersey jersey : jerseyArray) {
            jerseys.put(jersey.getIndex(),jersey);
            if (jersey.getIndex() > nextIndex)
            nextIndex = jersey.getIndex();
        }
        // Make the next id one greater than the maximum from the file
        ++nextIndex;
        return true;
    }

    private synchronized static int nextIndex() {
        int index = nextIndex;
        ++nextIndex;
        return index;
    }


    private Jersey[] getJerseyArray() {
        return getJerseyArray(null);
    }

    private Jersey[] getJerseyArray(String containsText) { // if containsText == null, no filter
        ArrayList<Jersey> jerseyArrayList = new ArrayList<>();

        for (Jersey jersey : jerseys.values()) {
            jerseyArrayList.add(jersey);
        }

        Jersey[] jerseyArray = new Jersey[jerseyArrayList.size()];
        jerseyArrayList.toArray(jerseyArray);
        return jerseyArray;
    }

    private boolean save() throws IOException {
        Jersey[] jerseyArray = getJerseyArray();
        objectMapper.writeValue(new File(filename),jerseyArray);
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey[] getJerseys() {
        synchronized(jerseys) {
            return getJerseyArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey getJersey(int index) {
        synchronized(jerseys) {
            if (jerseys.containsKey(index))
                return jerseys.get(index);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey createJersey(Jersey jersey) throws IOException {
        synchronized(jerseys) {
            // We create a new jersey object because the id field is immutable
            // and we need to assign the next unique id
            Jersey newJersey = new Jersey(nextIndex(), jersey.getName(), jersey.getNumber(), jersey.getPrice(), jersey.getSize(), jersey.getColor(), jersey.getQuantity());
            Jersey[] jerseyArray = findJerseys("");
            for(Jersey item : jerseyArray) {
                if(item.equals(newJersey)) {
                    newJersey = null;
                }
            }
            if(newJersey != null)
            {
                jerseys.put(newJersey.getIndex(),newJersey);
                save(); // may throw an IOException
            }
            return newJersey;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey updateJersey(Jersey jersey) throws IOException {
        synchronized(jerseys) {
            if (jerseys.containsKey(jersey.getIndex()) == false)
                return null;  // jersey does not exist

            jerseys.put(jersey.getIndex(),jersey);
            save(); // may throw an IOException
            return jersey;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteJersey(int index) throws IOException {
        synchronized(jerseys) {
            if (jerseys.containsKey(index)) {
                jerseys.remove(index);
                return save();
            }
            else
                return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Jersey[] findJerseys(String containsText) throws IOException {

        ArrayList<Jersey> jerseyArrayList = new ArrayList<>();
        synchronized(jerseys) {

            for(Jersey jersey : jerseys.values()) {

                //can search for jerseys by color, name, number, and size (possible to add more)
                String searching = jersey.getColor() + " " + jersey.getName() + " " + jersey.getNumber() + " " + jersey.getSize();
                String[] keys = containsText.split(" ");    
                
                boolean match = true;
                for(String key : keys) {
                    if(!searching.toLowerCase().contains(key.toLowerCase())) {
                        match = false;
                        break;
                    }
                }

                if(match) {
                    jerseyArrayList.add(jersey);
                }
            }

        }
        Jersey[] jerseysArray = new Jersey[jerseyArrayList.size()];
        return jerseyArrayList.toArray(jerseysArray);
    }
}
