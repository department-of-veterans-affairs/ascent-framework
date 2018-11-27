package gov.va.ascent.framework.audit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.util.SanitizationUtil;

/**
 * The Class AuditLogger.
 */
public class AuditLogger {

	static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(AuditLogger.class);

	/** Replacement for {@code null} parameters to the MDC entries that cannot be null or empty */
	private static final String UNKNOWN = "Unknown";
	/** Replacement for {@code null} parameters to the MDC entries that cannot be null */
	private static final String EMPTY = "";

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
		LOGGER.debug(SanitizationUtil.stripXSS(activityDetail));
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
		LOGGER.info(SanitizationUtil.stripXSS(activityDetail));
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
		LOGGER.warn(SanitizationUtil.stripXSS(activityDetail));
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
		LOGGER.error(SanitizationUtil.stripXSS(activityDetail));
		MDC.clear();

	}

	/**
	 * Adds the MDC security entries.
	 *
	 * @param auditable the auditable
	 */
	private static void addMdcSecurityEntries(AuditEventData auditable) {
		if (auditable == null) {
			auditable = new AuditEventData(AuditEvents.UNKNOWN, UNKNOWN, UNKNOWN); // NOSONAR
		}
		MDC.put("logType", "auditlogs");
		MDC.put("activity", StringUtils.isBlank(auditable.getActivity()) ? UNKNOWN : auditable.getActivity());
		MDC.put("event", auditable.getEvent() == null ? AuditEvents.UNKNOWN.name() : auditable.getEvent().name());
		MDC.put("audit_class", StringUtils.isBlank(auditable.getAuditClass()) ? UNKNOWN : auditable.getAuditClass());
		MDC.put("user", StringUtils.isBlank(auditable.getUser()) ? UNKNOWN : auditable.getUser());
		MDC.put("tokenId", StringUtils.isBlank(auditable.getTokenId()) ? EMPTY : auditable.getTokenId());
	}
}
