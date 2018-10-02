package gov.va.ascent.framework.audit;

import org.slf4j.MDC;
import org.slf4j.event.Level;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.util.SanitizationUtil;

/**
 * The Class AuditLogger.
 */
public class AuditLogger {

	static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(AuditLogger.class);

	/** Maximum length we are allowing for the "message" part of the log, leaving room for AuditEventData and JSON formatting */
	private static final int MAX_MSG_LEN = 14336;
	/** The string to prepend when a message must be split */
	private static final String PREPEND = "SPLIT LOG SEQ#";

	/*
	 * private constructor
	 */
	private AuditLogger() {

	}

	/**
	 * Debug.
	 *
	 * @param auditable the auditable
	 * @param activityDetail the activity detail
	 */
	public static void debug(AuditEventData auditable, String activityDetail) {
		addMdcSecurityEntries(auditable);
		logMessage(Level.DEBUG, SanitizationUtil.stripXSS(activityDetail));
		MDC.clear();
	}

	/**
	 * Info.
	 *
	 * @param auditable the auditable
	 * @param activityDetail the activity detail
	 */
	public static void info(AuditEventData auditable, String activityDetail) {
		addMdcSecurityEntries(auditable);
		logMessage(Level.INFO, SanitizationUtil.stripXSS(activityDetail));
		MDC.clear();

	}

	/**
	 * Warn.
	 *
	 * @param auditable the auditable
	 * @param activityDetail the activity detail
	 */
	public static void warn(AuditEventData auditable, String activityDetail) {
		addMdcSecurityEntries(auditable);
		logMessage(Level.WARN, SanitizationUtil.stripXSS(activityDetail));
		MDC.clear();

	}

	/**
	 * Error.
	 *
	 * @param auditable the auditable
	 * @param activityDetail the activity detail
	 */
	public static void error(AuditEventData auditable, String activityDetail) {
		addMdcSecurityEntries(auditable);
		logMessage(Level.ERROR, SanitizationUtil.stripXSS(activityDetail));
		MDC.clear();

	}

	/**
	 * Adds the MDC security entries.
	 *
	 * @param auditable the auditable
	 */
	private static void addMdcSecurityEntries(AuditEventData auditable) {
		MDC.put("logType", "auditlogs");
		MDC.put("activity", auditable.getActivity());
		MDC.put("event", auditable.getEvent().name());
		MDC.put("audit_class", auditable.getAuditClass());
		MDC.put("user", auditable.getUser());
		MDC.put("tokenId", auditable.getTokenId());
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
	private static void logMessage(Level severity, String message) {
		if (message != null && message.length() <= MAX_MSG_LEN) {
			doLog(severity, message);
		} else {
			// split message into 14 KB strings
			String[] messages = message == null ? new String[] { "" } : message.split("(?<=\\G.{14336})");
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
	private static void doLog(Level severity, String part) {
		if (severity == null) {
			LOGGER.debug(part);
		} else {
			if (Level.ERROR.equals(severity)) {
				LOGGER.error(part);
			} else if (Level.WARN.equals(severity)) {
				LOGGER.warn(part);
			} else if (Level.INFO.equals(severity)) {
				LOGGER.info(part);
			} else {
				LOGGER.debug(part);
			}
		}
	}
}
