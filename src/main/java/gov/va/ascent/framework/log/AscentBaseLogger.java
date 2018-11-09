package gov.va.ascent.framework.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.event.Level;

import com.fasterxml.jackson.core.io.JsonStringEncoder;

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

		/*
		 * DELETE THIS COMMENT WHEN CODE IS WRITTEN AND TESTED
		 * Pseudo code for splitting message and stack trace
		 *
		 *   mdcReserve = 10240 (10 KB), leaving 6144 (6 KB) for message and stacktrace
		 *   messageLen = safeMessage length
		 *   stacktraceLen = stacktrace length
		 *   if mcdReserve + messageLen + stacktraceLen > 16384 then
		 *     if messageLen >= 6144 then
		 *       split the safeMessage
		 *       loop on splitMessages
		 *         add MDC SPLIT_MDC_NAME and seq
		 *         log the splitMessage
		 *       end loop
		 *     else
		 *       log the safeMessage
		 *     end if
		 *     if stacktraceLen >= 6144 then
		 *       split the stacktrace
		 *       loop on splitStacktraces
		 *         add MDC SPLIT_MDC_NAME and value
		 *         log the splitStacktrace
		 *       end loop
		 *     else
		 *       log the throwable
		 *     end if
		 *   else
		 *     log the safeMessage AND the stacktrace
		 *   end if
		 */

		String safeMessage = safeMessage(message);
		// *** DO NOT USE THE message PARAM BELOW HERE ***

		// get the stack trace
		String stackTrace = null;
		if (t != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			t.printStackTrace(new PrintStream(out));
			try {
				stackTrace = IOUtils.toString(out.toByteArray(), "UTF-8");
			} catch (IOException e) { // NOSONAR not rethrowing just yet
				this.logger.error("While retrieving stacktrace", e);
			}
		}
		this.logger.debug(stackTrace);

		// need logic to print message(s), followed by stacktrace(s)
		// stack trace needs to be manually added to MDC under "stack_trace"

		if (safeMessage != null && safeMessage.length() > MAX_MSG_LEN) {
			int seq = 0;
			String[] splitMessages = splitMessages(safeMessage);
			for (String part : splitMessages) {
				MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
				// manually add a MDC put "stack_trace", string
				this.sendLogAtLevel(level, marker, part, null); // from now on, throwable arg will always be null
			}
		} else {
			this.sendLogAtLevel(level, marker, safeMessage, t); // only if message AND stacktrace fit into 10 KB
		}
		MDC.clear();
	}

	/**
	 * Get a string that is safe for use within a JSON context - escapes quotes, etc.
	 * <p>
	 * If {@code null} is passed as the message, then {@code null} will be returned.
	 *
	 * @param message
	 * @return String the escaped message, or {@code null}
	 */
	private String safeMessage(String message) {
		return message == null ? null : String.valueOf(JsonStringEncoder.getInstance().quoteAsString(message));
	}

	/**
	 * Perform genericized logging for a given log level.
	 * <p>
	 * If log level is {@code null}, DEBUG is assumed.
	 *
	 * @param level
	 * @param part
	 */
	private void sendLogAtLevel(Level level, Marker marker, String part, Throwable t) {
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
