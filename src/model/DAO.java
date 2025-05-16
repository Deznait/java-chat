package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import utilities.ChatException;

/**
 * Common interface for Data Access Objects.
 * Defines the basic operations that all DAOs should implement.
 * 
 * @param <T> The type of entity this DAO handles
 */
public interface DAO<T> {
    
    /**
     * Retrieves all entities of type T from the data source.
     * 
     * @return A list of entities of type T
     * @throws ChatException If there is an error retrieving the entities
     */
    List<T> getAll() throws ChatException;
    
    /**
     * Closes database resources.
     * 
     * @param conn The connection to close
     * @param stmt The statement to close
     * @param rs The result set to close
     */
    abstract void closeResources(Connection conn, CallableStatement stmt, ResultSet rs);
}