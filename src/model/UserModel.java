package model;

import java.sql.*;
import java.util.*;

import utilities.ChatException;
import utilities.DBConnection;

public class UserModel implements UserInterface {
	/** Database connection utility */
	DBConnection dbconn;

	/**
     * Constructs a new UserDAO.
     * 
     * @throws ChatException If there is an error initializing the database connection
     */
    public UserModel() throws ChatException {
    	dbconn = new DBConnection();
	}

    /**
     * {@inheritDoc}
     */
    @Override
	public void connect(String nick) throws ChatException {
    	Connection conn = null;
        CallableStatement cstmt = null;
        
    	try {
        	conn = dbconn.getConnection();
        	cstmt = conn.prepareCall("{call connect(?)}");

        	cstmt.setString(1, nick);
        	cstmt.execute();
        } catch (SQLException e) {
            throw new ChatException("Error connecting user: " + e.getMessage(), e);
        } finally {
            closeResources(conn, cstmt, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() throws ChatException {
    	Connection conn = null;
        CallableStatement cstmt = null;
        
    	try {
        	conn = dbconn.getConnection();
        	cstmt = conn.prepareCall("{call disconnect()}");
            
        	cstmt.execute();
        } catch (SQLException e) {
            throw new ChatException("Error disconnecting user: " + e.getMessage(), e);
        } finally {
            closeResources(conn, cstmt, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() throws ChatException {
        return getConnectedUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getConnectedUsers() throws ChatException {
    	Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
        	conn = dbconn.getConnection();
        	cstmt = conn.prepareCall("{call getConnectedUsers()}");
            rs = cstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setNick(rs.getString("nick"));
                user.setDateCon(rs.getTimestamp("date_con"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new ChatException("Error disconnecting user: " + e.getMessage(), e);
        } finally {
            closeResources(conn, cstmt, rs);
        }

        return users;
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
