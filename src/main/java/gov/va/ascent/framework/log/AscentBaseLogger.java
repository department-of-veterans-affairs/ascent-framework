package gov.va.ascent.framework.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.event.Level;

/**
 * Base logger class that:
 * <li>splits messages so large messages can be logged in spite of the docker 16 KB limit
 * <li>can print ASCII Art banner messages in the log
 * <p>
 *
 * @author aburkholder
 */
public class AscentBaseLogger {

	protected static final String NEWLINE = System.lineSeparator();

	/** Maximum length we are allowing for the "message" part of the log, leaving room for AuditEventData and JSON formatting */
	private static final int MAX_MSG_LEN = 14336;
	/** The string to prepend when a message must be split */
	private static final String SPLIT_MDC_NAME = "Split-Log-Sequence";

	/** The actual logger implementation (logback under slf4j) */
	private org.slf4j.Logger logger;

	/**
	 * Create a new logger for Ascent.
	 *
	 * @param logger org.slf4j.Logger
	 */
	protected AscentBaseLogger(org.slf4j.Logger logger) {
		this.logger = logger;
	}

	/**
	 * Get the underlying logger interface implementation (in this case, slf4j).
	 *
	 * @return Logger
	 */
	protected org.slf4j.Logger getLoggerInterfaceImpl() {
		return logger;
	}

	/**
	 * Convert a stacktrace into a loggable string.
	 *
	 * @param t the Throwable
	 * @return String
	 */
	private String stacktraceToString(Throwable t) {
		if (t != null) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			return sw.toString();
		}
		return null;
	}

	/**
	 * Splits a message into an array of strings that are 14 KB or less.
	 * Short messages will be an array with one element.
	 *
	 * @param message the message to split
	 * @return an array of Strings
	 */
	private String[] splitMessages(String message) {
		message = message == null ? "null" : message; // NOSONAR intentional variable reuse

		if (message.length() <= MAX_MSG_LEN) {
			return new String[] { message };
		} else {
			// split message into 14 KB strings
			return message.split("(?<=\\G.{" + MAX_MSG_LEN + "})");
		}
	}

	/**
	 * Perform genericized logging for a given log level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 * <p>
	 * If log level is {@code null}, DEBUG is assumed.
	 *
	 * @param level
	 * @param part
	 */
	private void sendLog(Marker marker, Level level, String part) {
		if (level == null) {
			sendLogDebug(marker, part);
		} else {
			if (org.slf4j.event.Level.ERROR.equals(level)) {
				sendLogError(marker, part);
			} else if (org.slf4j.event.Level.WARN.equals(level)) {
				sendLogWarn(marker, part);
			} else if (org.slf4j.event.Level.INFO.equals(level)) {
				sendLogInfo(marker, part);
			} else if (org.slf4j.event.Level.TRACE.equals(level)) {
				sendLogTrace(marker, part);
			} else {
				sendLogDebug(marker, part);
			}
		}
	}

	/** Because sonar is a PITA */
	private void sendLogTrace(Marker marker, String part) {
		if (marker == null) {
			logger.trace(part);
		} else {
			logger.trace(marker, part);
		}
	}

	/** Because sonar is a PITA */
	private void sendLogDebug(Marker marker, String part) {
		if (marker == null) {
			logger.debug(part);
		} else {
			logger.debug(marker, part);
		}
	}

	/** Because sonar is a PITA */
	private void sendLogInfo(Marker marker, String part) {
		if (marker == null) {
			logger.info(part);
		} else {
			logger.info(marker, part);
		}
	}

	/** Because sonar is a PITA */
	private void sendLogWarn(Marker marker, String part) {
		if (marker == null) {
			logger.warn(part);
		} else {
			logger.warn(marker, part);
		}
	}

	/** Because sonar is a PITA */
	private void sendLogError(Marker marker, String part) {
		if (marker == null) {
			logger.error(part);
		} else {
			logger.error(marker, part);
		}
	}

	/* ================ TRACE ================ */

	/**
	 * Log a TRACE message with a marker.
	 *
	 * @param message
	 */
	protected void trace(String message) {
		trace(null, message);
	}

	/**
	 * Log a TRACE message with a marker.
	 *
	 * @param message
	 * @param t
	 */
	protected void trace(String message, Throwable t) {
		trace(null, message, t);
	}

	/**
	 * Log a TRACE message with a marker.
	 *
	 * @param marker
	 * @param message
	 * @param t
	 */
	protected void trace(Marker marker, String message, Throwable t) {
		trace(marker, message + NEWLINE + stacktraceToString(t));
	}

	/**
	 * Log a TRACE message with a marker.
	 *
	 * @param marker
	 * @param message
	 */
	protected void trace(Marker marker, String message) {
		int seq = 0;
		for (String part : splitMessages(message)) {
			MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
			if (marker == null) {
				logger.trace(part);
			} else {
				logger.trace(marker, part);
			}
			MDC.clear();
		}
	}

	/* ================ DEBUG ================ */

	/**
	 * Log a DEBUG message with a marker.
	 *
	 * @param message
	 */
	protected void debug(String message) {
		debug(null, message);
	}

	/**
	 * Log a DEBUG message with a marker.
	 *
	 * @param message
	 * @param t
	 */
	protected void debug(String message, Throwable t) {
		debug(null, message, t);
	}

	/**
	 * Log a DEBUG message with a marker.
	 *
	 * @param marker
	 * @param message
	 * @param t
	 */
	protected void debug(Marker marker, String message, Throwable t) {
		debug(marker, message + NEWLINE + stacktraceToString(t));
	}

	/**
	 * Log a DEBUG message with a marker.
	 *
	 * @param marker
	 * @param message
	 */
	protected void debug(Marker marker, String message) {
		int seq = 0;
		for (String part : splitMessages(message)) {
			MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
			if (marker == null) {
				logger.debug(part);
			} else {
				logger.debug(marker, part);
			}
			MDC.clear();
		}
	}

	/* ================ INFO ================ */

	/**
	 * Log a INFO message with a marker.
	 *
	 * @param message
	 */
	protected void info(String message) {
		info(null, message);
	}

	/**
	 * Log a INFO message with a marker.
	 *
	 * @param message
	 * @param t
	 */
	protected void info(String message, Throwable t) {
		info(null, message, t);
	}

	/**
	 * Log a INFO message with a marker.
	 *
	 * @param marker
	 * @param message
	 * @param t
	 */
	protected void info(Marker marker, String message, Throwable t) {
		info(marker, message + NEWLINE + stacktraceToString(t));
	}

	/**
	 * Log a INFO message with a marker.
	 *
	 * @param marker
	 * @param message
	 */
	protected void info(Marker marker, String message) {
		int seq = 0;
		for (String part : splitMessages(message)) {
			MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
			if (marker == null) {
				logger.info(part);
			} else {
				logger.info(marker, part);
			}
			MDC.clear();
		}
	}

	/* ================ WARN ================ */

	/**
	 * Log a WARN message with a marker.
	 *
	 * @param message
	 */
	protected void warn(String message) {
		warn(null, message);
	}

	/**
	 * Log a WARN message with a marker.
	 *
	 * @param message
	 * @param t
	 */
	protected void warn(String message, Throwable t) {
		warn(null, message, t);
	}

	/**
	 * Log a WARN message with a marker.
	 *
	 * @param marker
	 * @param message
	 * @param t
	 */
	protected void warn(Marker marker, String message, Throwable t) {
		warn(marker, message + NEWLINE + stacktraceToString(t));
	}

	/**
	 * Log a WARN message with a marker.
	 *
	 * @param marker
	 * @param message
	 */
	protected void warn(Marker marker, String message) {
		int seq = 0;
		for (String part : splitMessages(message)) {
			MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
			if (marker == null) {
				logger.warn(part);
			} else {
				logger.warn(marker, part);
			}
			MDC.clear();
		}
	}

	/* ================ ERROR ================ */

	/**
	 * Log a ERROR message with a marker.
	 *
	 * @param message
	 */
	protected void error(String message) {
		error(null, message);
	}

	/**
	 * Log a ERROR message with a marker.
	 *
	 * @param message
	 * @param t
	 */
	protected void error(String message, Throwable t) {
		error(null, message, t);
	}

	/**
	 * Log a ERROR message with a marker.
	 *
	 * @param marker
	 * @param message
	 * @param t
	 */
	protected void error(Marker marker, String message, Throwable t) {
		error(marker, message + NEWLINE + stacktraceToString(t));
	}

	/**
	 * Log a ERROR message with a marker.
	 *
	 * @param marker
	 * @param message
	 */
	protected void error(Marker marker, String message) {
		int seq = 0;
		for (String part : splitMessages(message)) {
			MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
			if (marker == null) {
				logger.error(part);
			} else {
				logger.error(marker, part);
			}
			MDC.clear();
		}
	}

	/* ================ GENERIC ================ */

	/**
	 * Generic logging, allowing to specify the log level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level
	 * @param message
	 */
	protected void log(Level level, String message) {
		log(level, null, message);
	}

	/**
	 * Generic logging, allowing to specify the log level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level
	 * @param message
	 * @param t
	 */
	protected void log(Level level, String message, Throwable t) {
		log(level, null, message, t);
	}

	/**
	 * Generic logging, allowing to specify the log level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level
	 * @param marker
	 * @param message
	 * @param t
	 */
	protected void log(Level level, Marker marker, String message, Throwable t) {
		log(level, marker, message + NEWLINE + stacktraceToString(t));
	}

	/**
	 * Generic logging, allowing to specify the log level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level
	 * @param marker
	 * @param message
	 */
	protected void log(Level level, Marker marker, String message) {
		int seq = 0;
		for (String part : splitMessages(message)) {
			MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
			sendLog(marker, level, part);
			MDC.clear();
		}
	}

}
