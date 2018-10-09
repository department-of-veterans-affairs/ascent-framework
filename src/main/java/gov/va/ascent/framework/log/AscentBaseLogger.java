package gov.va.ascent.framework.log;

import org.slf4j.LoggerFactory;
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

	/** Maximum length we are allowing for the "message" part of the log, leaving room for AuditEventData and JSON formatting */
	private static final int MAX_MSG_LEN = 10240;
	/** The string to prepend when a message must be split */
	private static final String SPLIT_MDC_NAME = "Split-Log-Sequence";

	/** The actual logger implementation (logback under slf4j) */
	private org.slf4j.Logger logger;

	/** Systems line separator */
	protected static final String NEWLINE = System.lineSeparator();

	/** Name of the root logger */
	public static final String ROOT_LOGGER_NAME = org.slf4j.Logger.ROOT_LOGGER_NAME;

	/**
	 * Create a new logger for Ascent.
	 *
	 * @param logger org.slf4j.Logger
	 */
	protected AscentBaseLogger(org.slf4j.Logger logger) {
		this.logger = logger;
	}

	/**
	 * Set the log level for the logger to a new logging level.
	 * <p>
	 * This method accesses the underlying log implementation (e.g. logback).
	 *
	 * @param level the org.slf4j.event.Level
	 */
	public void setLevel(Level level) {
		((ch.qos.logback.classic.Logger) logger).setLevel(ch.qos.logback.classic.Level.toLevel(level.name()));
	}

	/**
	 * Get the current log level for the logger.
	 * If no level has been set, the ROOT_LOGGER level is returned.
	 * If ROOT_LOGGER has not been set, DEBUG is returned.
	 * <p>
	 * This method accesses the underlying log implementation (e.g. logback).
	 *
	 * @return Level the org.slf4j.event.Level
	 */
	public Level getLevel() {
		ch.qos.logback.classic.Level lvl = ((ch.qos.logback.classic.Logger) logger).getLevel();
		if (lvl == null) {
			lvl = ((ch.qos.logback.classic.Logger) LoggerFactory.getILoggerFactory().getLogger(ROOT_LOGGER_NAME)).getLevel();
		}
		return lvl == null ? Level.DEBUG : Level.valueOf(lvl.toString());
	}

	/**
	 * Get the underlying logger interface implementation (in this case, slf4j).
	 *
	 * @return Logger
	 */
	protected org.slf4j.Logger getLoggerInterfaceImpl() {
		return this.logger;
	}

	/**
	 * Splits a message into an array of strings that are {@link #MAX_MSG_LEN} KB or less.
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
			// split message into MAX_MSG_LEN strings
			return message.split("(?<=\\G.{" + MAX_MSG_LEN + "})");
		}
	}

	/* ================ Logger ================ */

	/**
	 * Generic logging, allowing to specify the log level.
	 *
	 * @param level
	 * @param marker
	 * @param message
	 */
	protected void sendlog(Level level, Marker marker, String message, Throwable t) {
		int seq = 0;
		for (String part : splitMessages(message)) {
			MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
			this.sendLevelLog(level, marker, part, t);
			MDC.clear();
		}
	}

	/**
	 * Perform genericized logging for a given log level.
	 * <p>
	 * If log level is {@code null}, DEBUG is assumed.
	 *
	 * @param level
	 * @param part
	 */
	private void sendLevelLog(Level level, Marker marker, String part, Throwable t) {
		if (level == null) {
			sendLogDebug(marker, part, t);
		} else {
			if (org.slf4j.event.Level.ERROR.equals(level)) {
				sendLogError(marker, part, t);
			} else if (org.slf4j.event.Level.WARN.equals(level)) {
				sendLogWarn(marker, part, t);
			} else if (org.slf4j.event.Level.INFO.equals(level)) {
				sendLogInfo(marker, part, t);
			} else if (org.slf4j.event.Level.TRACE.equals(level)) {
				sendLogTrace(marker, part, t);
			} else {
				sendLogDebug(marker, part, t);
			}
		}
	}

	/** Because sonar is a PITA */
	private void sendLogTrace(Marker marker, String part, Throwable t) {
		if (t == null) {
			if (marker == null) {
				this.logger.trace(part);
			} else {
				this.logger.trace(marker, part);
			}
		} else {
			if (marker == null) {
				this.logger.trace(part, t);
			} else {
				this.logger.trace(marker, part, t);
			}
		}
	}

	/** Because sonar is a PITA */
	private void sendLogDebug(Marker marker, String part, Throwable t) {
		if (t == null) {
			if (marker == null) {
				this.logger.debug(part);
			} else {
				this.logger.debug(marker, part);
			}
		} else {
			if (marker == null) {
				this.logger.debug(part, t);
			} else {
				this.logger.debug(marker, part, t);
			}
		}
	}

	/** Because sonar is a PITA */
	private void sendLogInfo(Marker marker, String part, Throwable t) {
		if (t == null) {
			if (marker == null) {
				this.logger.info(part);
			} else {
				this.logger.info(marker, part);
			}
		} else {
			if (marker == null) {
				this.logger.info(part, t);
			} else {
				this.logger.info(marker, part, t);
			}
		}
	}

	/** Because sonar is a PITA */
	private void sendLogWarn(Marker marker, String part, Throwable t) {
		if (t == null) {
			if (marker == null) {
				this.logger.warn(part);
			} else {
				this.logger.warn(marker, part);
			}
		} else {
			if (marker == null) {
				this.logger.warn(part, t);
			} else {
				this.logger.warn(marker, part, t);
			}
		}
	}

	/** Because sonar is a PITA */
	private void sendLogError(Marker marker, String part, Throwable t) {
		if (t == null) {
			if (marker == null) {
				this.logger.error(part);
			} else {
				this.logger.error(marker, part);
			}
		} else {
			if (marker == null) {
				this.logger.error(part, t);
			} else {
				this.logger.error(marker, part, t);
			}
		}
	}

}
