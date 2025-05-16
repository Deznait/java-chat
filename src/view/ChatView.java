package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import controller.*;
import model.Message;
import model.User;
import utilities.ChatException;


/**
 * View class that constructs the chat UI using Swing components.
 * Handles user interactions and displays data from the model.
 */
public class ChatView extends JFrame {
    private static final long serialVersionUID = -6901782913751163706L;
    private final int TIMER_MILL = 2000;
    private JPanel contentPane;
    private JTextArea chatArea;
    private JTextField messageField;
    public JButton sendButton;;
    public JButton logoutButton;
    private JList<String> userList;
	private ChatControllerInterface controller;
    private Timer timer;

    /**
     * Constructs the main chat window.
     * Sets up the UI components and layout.
     */
    public ChatView() {
    	// Set up frame
        setTitle("Java Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null);

        initUI();
    }
    
    /**
     * Initializes the UI components and layout.
     * Sets up the content pane, panels, and components.
     */
    private void initUI() {
    	// Create content pane
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        // Center panel with chat area and user list
        JPanel centerPanel = new JPanel();
        contentPane.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new BorderLayout(0, 0));
        
        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        // Enable auto-scroll
        DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        centerPanel.add(chatScrollPane, BorderLayout.CENTER);

        // User list
        userList = new JList<>();
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(200, 0));
        centerPanel.add(userScrollPane, BorderLayout.EAST);

        // Top panel for title and logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Add a title to the top
        JLabel titleLabel = new JLabel("Welcome to Java Chat", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Add logout button
        logoutButton = new JButton("Logout");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(logoutButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        contentPane.add(topPanel, BorderLayout.NORTH);

        // Bottom panel for message input
        JPanel bottomPanel = new JPanel();
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setLayout(new BorderLayout(0, 0));

        // Message input field
        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);
        messageField.setColumns(10);

        // Send button
        sendButton = new JButton("Send");
        bottomPanel.add(sendButton, BorderLayout.EAST);
        
        setupEventListeners();
        
        // Set up a timer to auto-refresh the chat every 2 seconds
        timer = new Timer(TIMER_MILL, e -> update());
        timer.start();
    }
    
    /**
     * Sets the controller for this view.
     * 
     * @param controller The controller to set
     */
    public void setController(ChatControllerInterface controller) {
        this.controller = controller;
        update();
    }
    
    /**
     * Sets up event listeners for UI components.
     */
    private void setupEventListeners() {
    	// Event listener when closing the Window, in order to disconnect
    	addWindowListener(new WindowAdapter() {
    	    @Override
    	    public void windowClosing(WindowEvent event) {
    	    	logout();
    	    }
    	});
        
        // Action listener for the send button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					sendMessage();
				} catch (ChatException e1) {
					showError(e1.getMessage());
				}
            }
        });
        
        // Key listener for the message field (to handle Enter key)
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        sendMessage();
                        e.consume(); // Prevent the default action
                    }
				} catch (ChatException e1) {
					showError(e1.getMessage());
				}
            }
        });
        
        // Action listener for the logout button
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

    }
    
    /**
     * Sends a message from the message input field.
     * 
     * @throws ChatException If there is an error sending the message
     */
    private void sendMessage() throws ChatException {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            controller.sendMessage(message);
        	updateMessages();
            
            // Clear message input in view
            messageField.setText("");
        }
    }
    
    /**
     * Updates the chat area and user list with the latest data.
     * Called periodically by the timer.
     */
    private void update() {
    	if(controller != null) {
        	updateMessages();
        	updateUsers();
    	}
    }
    
    /**
     * Updates the chat area with the latest messages from the database.
     */
    private void updateMessages() {
        try {
            List<Message> messages = controller.getMessages();
            for (Message msg : messages) {
            	chatArea.append(msg + "\n");
            }
        } catch (ChatException e) {
        	showError("Error updating messages: " + e.getMessage());
        }
    }
    
    /**
     * Updates the chat area with the latest messages from the database.
     */
    private void updateUsers() {
    	try {
            List<User> users = controller.getUsers();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            
            for (User user : users) {
                listModel.addElement(user.toString());
            }
            
            userList.setModel(listModel);
        } catch (ChatException e) {
        	showError("Error updating active users: " + e.getMessage());
        }

    }
    
    /**
     * Handles user logout.
     */
    public void logout() {
        try {
            // Stop timers
            if (timer != null) {
            	timer.stop();
            }
            
            // Disconnect from the server
            controller.disconnect();
            
            // Hide the current window
            setVisible(false);
            dispose();
            
            // Notify the login manager to show login screen
            System.exit(NORMAL);
        } catch (ChatException e) {
        	showError("Error during logout: " + e.getMessage());
        }
    }
    
    /**
     * Displays an error message.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
    	JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
