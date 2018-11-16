package gov.va.ascent.framework.log;

import java.io.PrintWriter;
import java.io.StringWriter;

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

	/** The property name used by logback encoder for stack traces */
	private static final String STACK_TRACE_MDC_NAME = "stack_trace";

	/** Maximum length we are allowing for a single log, as dictated by docker limits */
	public static final int MAX_TOTAL_LOG_LEN = 16384;

	/** The string to prepend when a message must be split */
	private static final String SPLIT_MDC_NAME = "Split-Log-Sequence";

	/**
	 * Maximum length we are allowing for the "message" part of the log, leaving room for AuditEventData and JSON formatting and stack
	 * trace
	 */
	public static final int MAX_MSG_LENGTH = 6144;

	/** The actual logger implementation (logback under slf4j) */
	private org.slf4j.Logger logger;

	/** Systems line separator */
	protected static final String NEWLINE = System.lineSeparator();

	/** Name of the root logger */
	public static final String ROOT_LOGGER_NAME = org.slf4j.Logger.ROOT_LOGGER_NAME;

	/**
	 * Maximum length we are allowing for AuditEventData and JSON formatting, etc., (everything other than the message and stack trace
	 * text
	 */
	public static final int MDC_RESERVE_LENGTH = 10240;

	/**
	 * Maximum length we are allowing for the "stack trace" part of the log, leaving room for AuditEventData and JSON formatting and
	 * message
	 */
	public static final int MAX_STACK_TRACE_TEXT_LENGTH = 6144;

	/**
	 * Create a new logger for Ascent.
	 *
	 * @param logger org.slf4j.Logger
	 */
	protected AscentBaseLogger(final org.slf4j.Logger logger) {
		this.logger = logger;

	}

	/**
	 * Set the log level for the logger to a new logging level.
	 * <p>
	 * This method accesses the underlying log implementation (e.g. logback).
	 *
	 * @param level the org.slf4j.event.Level
	 */
	public void setLevel(final Level level) {
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
	 * Splits a message into an array of strings that are {@link #MAX_MSG_LENGTH} KB or less. Short messages will be an array with one
	 * element.
	 *
	 * @param message the message to split
	 * @return an array of Strings
	 */
	private static String[] splitMessages(String message) {
		message = message == null ? "null" : message; // NOSONAR intentional variable reuse

		if (message.length() <= MAX_MSG_LENGTH) {
			return new String[] { message };
		} else {
			// split message into MAX_MSG_LENGTH strings
			return message.split("(?<=\\G.{" + MAX_MSG_LENGTH + "})");
		}
	}

	/**
	 * Splits a stack trace text into an array of strings that are {@link #MAX_STACK_TRACE_TEXT_LENGTH} KB or less. Short stack trace
	 * text will be an array with one element.
	 *
	 * @param stackTraceText the message to split
	 * @return an array of Strings
	 */
	private String[] splitStackTraceText(String stackTraceText) {
		stackTraceText = stackTraceText == null ? "null" : stackTraceText; // NOSONAR intentional variable reuse

		if (stackTraceText.length() <= MAX_STACK_TRACE_TEXT_LENGTH) {
			return new String[] { stackTraceText };
		} else {
			// split message into MAX_MSG_OR_STACK_TRACE_LENGTH strings
			return stackTraceText.split("\\b.{1," + (MAX_STACK_TRACE_TEXT_LENGTH - 1) + "}\\b\\W?");
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
	protected void sendlog(final Level level, final Marker marker, final String message, final Throwable t) {

		String safeMessage = safeMessage(message);

		// get the stack trace
		String stackTrace = getStackTraceAsString(t);

		int messageLength = safeMessage == null ? 0 : safeMessage.length();
		int stackTraceLength = stackTrace == null ? 0 : stackTrace.length();
		int mdcReserveLength = MDC_RESERVE_LENGTH;

		if (mdcReserveLength + messageLength + stackTraceLength > MAX_TOTAL_LOG_LEN) {
			if (messageLength >= MAX_MSG_LENGTH) {
				int seq = 0;
				String[] splitMessages = splitMessages(safeMessage);
				for (String part : splitMessages) {
					MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
					// manually add an MDC put "stack_trace", string
					MDC.put(STACK_TRACE_MDC_NAME, "stack trace will be printed in successive split logs");
					// throwable arg will be null if the stack trace needs to be split to another split log message
					this.sendLogAtLevel(level, marker, part, null);
				}
			} else {
				// manually add a MDC put "stack_trace", string
				MDC.put(STACK_TRACE_MDC_NAME, "stack trace will be printed in successive split logs");
				// log the safeMessage
				// throwable arg will be null if the stack trace needs to be split to another split log message
				this.sendLogAtLevel(level, marker, safeMessage, null);
			}

			String messageStub = "message is already printed in previous split logs";
			if (stackTraceLength >= MAX_STACK_TRACE_TEXT_LENGTH) {
				int seq = 0;
				String[] splitstackTrace = splitStackTraceText(stackTrace);
				for (String part : splitstackTrace) {
					MDC.put(SPLIT_MDC_NAME, Integer.toString(++seq));
					// manually add an MDC put "stack_trace", string
					MDC.put(STACK_TRACE_MDC_NAME, part);
					// throwable arg will be null if the stack trace needs to be split to another split log message
					this.sendLogAtLevel(level, marker, messageStub, null);
				}
			} else if (stackTraceLength != 0) {
				this.sendLogAtLevel(level, marker, messageStub, t);
			}

		} else {
			this.sendLogAtLevel(level, marker, safeMessage, t);
		}

		MDC.clear();
	}

	/**
	 * Get the stack trace formatted the same way as with Throwable.printStckTrace().
	 *
	 * @param t Throwable that contains the stack trace
	 * @return String the formatted stack trace
	 */
	private String getStackTraceAsString(final Throwable t) {

		if (t == null) {
			return "";
		}

		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		t.printStackTrace(printWriter);
		printWriter.flush();

		return writer.toString();
	}

	/**
	 * Get a string that is safe for use within a JSON context - escapes quotes, etc.
	 * <p>
	 * If {@code null} is passed as the message, then {@code null} will be returned.
	 *
	 * @param message
	 * @return String the escaped message, or {@code null}
	 */
	private String safeMessage(final String message) {
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
	private void sendLogAtLevel(final Level level, final Marker marker, final String part, final Throwable t) {
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
	private void sendLogTrace(final Marker marker, final String part, final Throwable t) {
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
	private void sendLogDebug(final Marker marker, final String part, final Throwable t) {
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
	private void sendLogInfo(final Marker marker, final String part, final Throwable t) {
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
	private void sendLogWarn(final Marker marker, final String part, final Throwable t) {
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
	private void sendLogError(final Marker marker, final String part, final Throwable t) {
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
