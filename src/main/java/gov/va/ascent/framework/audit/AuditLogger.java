package gov.va.ascent.framework.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The Class AuditLogger.
 */
public class AuditLogger {

     static final Logger LOGGER = LoggerFactory.getLogger(AuditLogger.class);

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
        LOGGER.debug(activityDetail);
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
        LOGGER.info(activityDetail);
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
        LOGGER.warn(activityDetail);
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
        LOGGER.error(activityDetail);
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
}
