package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Message {
    private String nick;
    private String message;
    private Timestamp timestamp;

    /**
     * Default constructor for the Message class.
     */
    public Message() {}

    /**
     * Constructs a Message with the specified sender, content, and timestamp.
     *
     * @param nick The nickname of the sender
     * @param message The content of the message
     * @param timestamp The timestamp when the message was sent
     */
    public Message(String nick, String message, Timestamp timestamp) {
        this.nick = nick;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    /**
     * Returns a string representation of the Message.
     * Format: [date time] nickname: message
     *
     * @return A string representation of the Message
     */
    @Override
    public String toString() {
    	String sTimestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    	return "[" + sTimestamp + "] " + nick + ": " + message;
    }
}

