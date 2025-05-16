package model;

import java.util.List;
import utilities.ChatException;

/**
 * Interface for User Data Access Object.
 * Defines operations for user management in the chat application.
 */
public interface UserInterface extends DAO<User> {
    
    /**
     * Connects a user to the chat system.
     * 
     * @param nick The nickname of the user to connect
     * @throws ChatException If there is an error during connection
     */
    void connect(String nick) throws ChatException;
    
    /**
     * Disconnects the current user from the chat system.
     * 
     * @throws ChatException If there is an error during disconnection
     */
    void disconnect() throws ChatException;
    
    /**
     * Retrieves a list of all currently connected users.
     * Implementation of the getAll method from DAO interface.
     * 
     * @return List of User objects representing connected users
     * @throws ChatException If there is an error retrieving user data
     */
    @Override
    List<User> getAll() throws ChatException;
    
    /**
     * Alias for getAll() that provides a more descriptive name.
     * 
     * @return List of User objects representing connected users
     * @throws ChatException If there is an error retrieving user data
     */
    List<User> getConnectedUsers() throws ChatException;
}