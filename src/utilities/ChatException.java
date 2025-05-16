package utilities;

/**
 * Custom exception for chat-related errors.
 */
public class ChatException extends Exception {
    private static final long serialVersionUID = 25021629001007722L;

	/**
     * Constructs a new ChatAppException with the specified detail message.
     *
     * @param message the detail message
     */

	public ChatException(String message) {
        super(message);
    }

	/**
     * Constructs a new ChatAppException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
