package chat;

import controller.*;
import view.*;
import model.*;
import utilities.ChatException;

/**
 * Entry point of the chat application.
 */
public class Main {
    public static void main(String[] args) throws ChatException {
        // 1. Models
        UserInterface userModel = new UserModel();
        MessageInterface messageModel = new MessageModel();
        
        // 2. Views
    	LoginView loginView = new LoginView(userModel);
        ChatView view = new ChatView();
        
        // 3. 
    	String nickname = loginView.showLoginDialog();
    	
        //String nickname = javax.swing.JOptionPane.showInputDialog("Enter your nickname:");
        if (nickname == null || nickname.trim().isEmpty()) {
            return;
        }

        // 4. Controller
        ChatControllerInterface controller = new ChatController(view, userModel, messageModel, nickname);
        
        // 5. Set controller to the view
        view.setController(controller);

        // 6. Show the UI
        view.setVisible(true);
    }
}
