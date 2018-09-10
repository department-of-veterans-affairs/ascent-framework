package gov.va.ascent.framework.exception;

/**
 * Custom extension of RuntimeException so that we can raise this for exceptions we have no intention
 * of handling and need to raise but for some reason (i.e. checkstyle checks) cannot raise
 * java's RuntimeException or allow the original exception to simply propagate.
 *
 * @author jshrader
 */
public class AscentRuntimeException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2598842813684506358L;

	/** Server name exception occurred on */
	public static final String SERVER_NAME = System.getProperty("server.name");

	/**
	 * Instantiates a new exception.
	 */
	public AscentRuntimeException() {
		super();
	}

	/**
	 * Instantiates a new service exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AscentRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new service exception.
	 *
	 * @param message the message
	 */
	public AscentRuntimeException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new service exception.
	 *
	 * @param cause the cause
	 */
	public AscentRuntimeException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Gets the server name.
	 *
	 * @return the server name
	 */
	public final String getServerName() {
		return SERVER_NAME;
	}
}
