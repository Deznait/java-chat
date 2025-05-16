package model;

import java.sql.*;
import java.util.*;

import utilities.ChatException;
import utilities.DBConnection;

/**
 * Data Access Object implementation for Message entities.
 * Handles database operations related to messages in the chat application.
 */
public class MessageModel implements MessageInterface {
    /** Database connection utility */
	DBConnection dbconn;

   /**
     * Constructs a new MessageDAO.
     * 
     * @throws ChatException If there is an error initializing the database connection
     */
    public MessageModel() throws ChatException {
    	dbconn = new DBConnection();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String message) throws ChatException {
    	Connection conn = null;
        CallableStatement cstmt = null;
        
        try {
        	conn = dbconn.getConnection();
        	cstmt = conn.prepareCall("{call send(?)}");

        	cstmt.setString(1, message);
        	cstmt.execute();
        } catch (SQLException e) {
            throw new ChatException("Error sending message: " + e.getMessage(), e);
        } finally {
            closeResources(conn, cstmt, null);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getAll() throws ChatException {
        return getMessages();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getMessages() throws ChatException {
    	Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();

        try {
        	conn = dbconn.getConnection();
        	cstmt = conn.prepareCall("{call getMessages()}");
            boolean hasResults = cstmt.execute();

            if (hasResults) {
            	rs = cstmt.getResultSet();
                while (rs.next()) {
                    Message msg = new Message();
                    msg.setNick(rs.getString("nick"));
                    msg.setMessage(rs.getString("message"));
                    msg.setTimestamp(rs.getTimestamp("ts"));
                    messages.add(msg);
                }
            }
        } catch (SQLException e) {
            throw new ChatException("Database error while retrieving new messages: " + e.getMessage(), e);
        } finally {
            closeResources(conn, cstmt, rs);
        }
        
        return messages;
    }
    
    /**
     * Closes database resources.
     * 
     * @param conn The connection to close
     * @param stmt The statement to close
     * @param rs The result set to close
     */
    private void closeResources(Connection conn, CallableStatement stmt, ResultSet rs) {
        try {
            if (conn != null) conn.close();
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            // Just log the error and continue
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
