package utilities;

import java.sql.*;

public class DBConnection {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    /**
     * Constructs a new ChatDAO and establishes a database connection.
     *
     * @throws ChatAppException if there's an error connecting to the database
     */
    public DBConnection() throws ChatException {
        // Parse database configuration
    	try {
        	DBConfigParser configParser = new DBConfigParser();
			configParser.parseConfig("database-config.xml");
	    	this.URL = "jdbc:mysql://" + configParser.getHost() + ":" + configParser.getPort() + "/" + configParser.getDatabase() + "?noAccessToProcedureBodies=true";
	        this.USER = configParser.getUsername();
	        this.PASSWORD = configParser.getPassword();
		} catch (Exception e) {
			throw new ChatException("Error sending message: " + e.getMessage(), e);
		}
    }

    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void disconnect(Connection connection) throws SQLException {
        connection.close();
    }
}

