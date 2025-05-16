package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import model.User;
import model.UserInterface;
import utilities.ChatException;


/**
 * A login view for the chat application.
 * Provides a graphical interface for users to enter their nickname.
 */
public class LoginView extends JFrame {
	private static final long serialVersionUID = -4056116619527159147L;
	private static final int TIMER_INTERVAL = 3000; // milliseconds
    
    private JPanel contentPane;
    private JTextField nicknameField;
    private JButton loginButton;
    private JButton cancelButton;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private String nickname;
    private boolean loginSuccessful;
    private UserInterface userModel;
    private Timer refreshTimer;

    /**
     * Creates a new login view.
     */
    public LoginView(UserInterface userModel) {
        setTitle("Java Chat - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 350);
        setLocationRelativeTo(null);
        
        this.userModel = userModel;
        
        initUI();
        setupEventListeners();
        startRefreshTimer();
    }
    
    /**
     * Initializes the UI components.
     */
    private void initUI() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 20));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        contentPane.add(titlePanel, BorderLayout.NORTH);
        
        JLabel titleLabel = new JLabel("Welcome to Java Chat");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        
        // Main content panel (form + users list)
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        
        JLabel nicknameLabel = new JLabel("Enter your nickname:");
        formPanel.add(nicknameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        nicknameField = new JTextField();
        nicknameField.setColumns(15);
        formPanel.add(nicknameField, gbc);
        
        // User list panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Connected Users",
            TitledBorder.LEFT,
            TitledBorder.TOP));
        userPanel.setPreferredSize(new Dimension(200, 0));
        mainPanel.add(userPanel, BorderLayout.EAST);
        
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane scrollPane = new JScrollPane(userList);
        userPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        loginButton = new JButton("Login");
        buttonPanel.add(loginButton);
        
        cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
    }
    
    /**
     * Sets up event listeners for the UI components.
     */
    private void setupEventListeners() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processLogin();
            }
        });
        
        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopRefreshTimer();
                dispose();
                System.exit(0);
            }
        });
        
        // Enter key in nickname field
        nicknameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processLogin();
                }
            }
        });
    }
    
    /**
     * Processes the login attempt.
     */
    private void processLogin() {
        nickname = nicknameField.getText().trim();
        if (nickname != null && !nickname.isEmpty()) {
            stopRefreshTimer();
            loginSuccessful = true;
            setVisible(false);
            dispose();
        } else {
            // Highlight the field to indicate an error
            nicknameField.requestFocus();
        }
    }
    
    /**
     * Shows the login dialog and waits for user input.
     * 
     * @return The entered nickname, or null if login was cancelled
     */
    public String showLoginDialog() {
        setVisible(true);
        
        // This is needed because we need to wait for the dialog to close
        while (isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return loginSuccessful ? nickname : null;
    }
    
    /**
     * Starts the timer to periodically refresh the user list.
     */
    private void startRefreshTimer() {
        refreshTimer = new Timer(TIMER_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshConnectedUsers();
            }
        });
        refreshTimer.start();
        
        // Initial refresh
        refreshConnectedUsers();
    }
    
    /**
     * Stops the refresh timer.
     */
    private void stopRefreshTimer() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }
    
    /**
     * Refreshes the list of connected users.
     */
    private void refreshConnectedUsers() {
        try {
            List<User> users = userModel.getAll();
            userListModel.clear();
            
            // Add a count of online users at the top
            userListModel.addElement("Online Users: " + users.size());
            userListModel.addElement("------------------------");
            
            for (User user : users) {
                userListModel.addElement(user.getNick());
            }
        } catch (ChatException e) {
            // Just log the error, don't show it to the user during login
            System.err.println("Error refreshing user list: " + e.getMessage());
        }
    }
   
}
