package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class User {
    private String nick;
    private Timestamp dateCon;

    /**
     * Constructs a User with the specified nickname and connection date.
     *
     * @param nick The user's nickname
     * @param dateCon The timestamp when the user connected
     */
    public User(String nick, Timestamp dateCon) {
        this.nick = nick;
        this.dateCon = dateCon;
    }

    // Getters and setters
    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    public Timestamp getDateCon() { return dateCon; }
    public void setDateCon(Timestamp dateCon) { this.dateCon = dateCon; }
    
    /**
     * Returns a string representation of the User.
     * Format: [nickname] connection_date
     *
     * @return A string representation of the User
     */
    @Override
    public String toString() {
    	String sTimestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dateCon);
    	return "[" + nick + "] " + sTimestamp;
    }
}
