package model;

import java.util.List;
import utilities.ChatException;

/**
 * Interface for Message Data Access Object.
 * Defines operations for message management in the chat application.
 */
public interface MessageInterface extends DAO<Message> {
    
    /**
     * Sends a message to the chat.
     * 
     * @param message The content of the message to send
     * @throws ChatException If there is an error sending the message
     */
    void sendMessage(String message) throws ChatException;
    
    /**
     * Retrieves messages from the chat system.
     * Implementation of the getAll method from DAO interface.
     * 
     * @return List of Message objects
     * @throws ChatException If there is an error retrieving messages
     */
    @Override
    List<Message> getAll() throws ChatException;
}