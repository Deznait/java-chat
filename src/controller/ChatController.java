package controller;

import model.*;
import utilities.ChatException;
import view.ChatView;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Controller class that handles the logic and interactions between the model and the view.
 */
public class ChatController implements ChatControllerInterface {
    private final UserInterface userModel;
    private final MessageInterface messageModel;
    private final ChatView view;
    private final String nickname;

    /**
     * Initializes the ChatController and connects the user to the chat system.
     * 
     * @param view The chat view component
     * @param userDAO The User DAO
     * @param messageDAO The Message DAO
     * @param nickname The nickname of the user
     * @throws ChatException If there is an error during initialization
     */
    public ChatController(ChatView view, UserInterface userModel, MessageInterface messageModel, String nickname) throws ChatException {
        this.view = view;
        this.userModel = userModel;
        this.messageModel = messageModel;
        this.nickname = nickname;
        
        // Connect the user to the database using the model
        try {
        	this.userModel.connect(this.nickname);
        } catch (ChatException e) {
        	System.out.println(e.getMessage());
        	this.view.showError("Connection Error: " + e.getMessage());
            disconnect();
            System.exit(1);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String messageContent) throws ChatException {
        try {
            // Create and send message
            messageModel.sendMessage(messageContent);
        } catch (ChatException e) {
            view.showError("Failed to send message: " + e.getMessage());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getMessages() throws ChatException {
    	return messageModel.getMessages();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsers() throws ChatException {
    	return userModel.getConnectedUsers();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() throws ChatException {
    	userModel.disconnect();
    }
}
