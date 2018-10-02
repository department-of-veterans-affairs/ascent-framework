package gov.va.ascent.framework.log;

/**
 * Base logger class that:
 * <li>splits messages so large messages can be logged in spite of the docker 16 KB limit
 * <li>can print short ASCII Art banner messages in the log
 * <p>
 *
 * @author aburkholder
 */
public class AscentBaseLogger {

	/** Maximum length we are allowing for the "message" part of the log, leaving room for AuditEventData and JSON formatting */
	private static final int MAX_MSG_LEN = 14336;
	/** The string to prepend when a message must be split */
	private static final String PREPEND = "SPLIT LOG SEQ#";

	/** The actual logger implementation (logback under slf4j) */
	protected org.slf4j.Logger logger;

	/**
	 * Create a new logger for Ascent.
	 *
	 * @param logger org.slf4j.Logger
	 */
	protected AscentBaseLogger(org.slf4j.Logger logger) {
		this.logger = logger;
	}

	/**
	 * Perform actions to split logs into 14 KB chunks and log separately.
	 * A 14 KB message leaves room for AuditEventData and JSON formatting.
	 * <p>
	 * This is a temporary measure, until a way is found to mitigate the 16 KB log size limit imposed by Docker.
	 *
	 * @param severity
	 * @param message
	 */
	protected void logMessage(org.slf4j.event.Level severity, String message) {
		if (message == null || message.length() <= MAX_MSG_LEN) {
			doLog(severity, message);
		} else {
			// split message into 14 KB strings
			String[] messages = message.split("(?<=\\G.{" + MAX_MSG_LEN + "})");
			int seq = 0;
			for (String part : messages) {
				part = PREPEND + ++seq + " " + part;
				doLog(severity, part);
			}
		}
	}

	/**
	 * Perform the logging.
	 * <p>
	 * If severity is {@code null} or {@code TRACE}, DEBUG is assumed.
	 *
	 * @param severity
	 * @param part
	 */
	private void doLog(org.slf4j.event.Level severity, String part) {
		if (severity == null) {
			logger.debug(part);
		} else {
			if (org.slf4j.event.Level.ERROR.equals(severity)) {
				logger.error(part);
			} else if (org.slf4j.event.Level.WARN.equals(severity)) {
				logger.warn(part);
			} else if (org.slf4j.event.Level.INFO.equals(severity)) {
				logger.info(part);
			} else {
				logger.debug(part);
			}
		}
	}

}
