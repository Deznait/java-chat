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
        String nickname = javax.swing.JOptionPane.showInputDialog("Enter your nickname:");
        if (nickname == null || nickname.trim().isEmpty()) {
            return;
        }

        // 1. View
        ChatView view = new ChatView();

        // 2. DAOs
        UserInterface userModel = new UserModel();
        MessageInterface messageModel = new MessageModel();

        // 3. Controller
        ChatControllerInterface controller = new ChatController(view, userModel, messageModel, nickname);
        
        // 4. Set controller to the view
        view.setController(controller);

        // 5. Show the UI
        view.setVisible(true);
    }
}
