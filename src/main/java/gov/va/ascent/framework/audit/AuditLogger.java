package gov.va.ascent.framework.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.util.SanitizationUtil;

/**
 * The Class AuditLogger.
 */
public class AuditLogger {

	static final Logger LOGGER = LoggerFactory.getLogger(AuditLogger.class);

	/** Maximum length we are allowing for the "message" part of the log */
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
		logMessage(null, SanitizationUtil.stripXSS(activityDetail));
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
		logMessage(MessageSeverity.INFO, SanitizationUtil.stripXSS(activityDetail));
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
		logMessage(MessageSeverity.WARN, SanitizationUtil.stripXSS(activityDetail));
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
		logMessage(MessageSeverity.ERROR, SanitizationUtil.stripXSS(activityDetail));
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
	 * <p>
	 * This is a temporary measure, until a way is found to mitigate the 16 KB log size limit imposed by Docker.
	 *
	 * @param severity
	 * @param message
	 */
	private static void logMessage(MessageSeverity severity, String message) {
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
	 * If severity is {@code null}, DEBUG is assumed. Otherwise the lowest log level is INFO.
	 *
	 * @param severity
	 * @param part
	 */
	private static void doLog(MessageSeverity severity, String part) {
		if (severity == null) {
			LOGGER.debug(part);
		} else {
			if (MessageSeverity.FATAL.equals(severity)) {
				LOGGER.error("[FATAL] " + part);
			} else if (MessageSeverity.ERROR.equals(severity)) {
				LOGGER.error(part);
			} else if (MessageSeverity.WARN.equals(severity)) {
				LOGGER.warn(part);
			} else {
				LOGGER.info(part);
			}
		}
	}
}
