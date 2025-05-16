package controller;

import model.Message;
import model.User;
import utilities.ChatException;

import java.util.List;

/**
 * Interface for the Chat Controller.
 * Defines operations for managing the chat application logic.
 */
public interface ChatControllerInterface {
    
    /**
     * Sends a message to the chat.
     *
     * @param messageContent The content of the message to send
     * @throws ChatException If there is an error sending the message
     */
    void sendMessage(String messageContent) throws ChatException;
    
    /**
     * Retrieves messages from the chat system.
     *
     * @return List of Message objects
     * @throws ChatException If there is an error retrieving messages
     */
    List<Message> getMessages() throws ChatException;
    
    /**
     * Retrieves a list of currently connected users.
     *
     * @return List of User objects representing connected users
     * @throws ChatException If there is an error retrieving user data
     */
    List<User> getUsers() throws ChatException;
    
    /**
     * Disconnects the current user from the chat system.
     *
     * @throws ChatException If there is an error during disconnection
     */
    void disconnect() throws ChatException;
}