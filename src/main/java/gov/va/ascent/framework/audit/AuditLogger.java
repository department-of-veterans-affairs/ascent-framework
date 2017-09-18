package gov.va.ascent.framework.audit;

import gov.va.ascent.framework.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The Class AuditLogger.
 */
public class AuditLogger {

    final static Logger LOGGER = LoggerFactory.getLogger(AuditLogger.class);

    public static void debug(Auditable auditable,String activityDetail) {
        addMdcSecurityEntries(auditable);
        LOGGER.debug(activityDetail);
        MDC.clear();

    }

    public static void info(Auditable auditable, String activityDetail) {
        addMdcSecurityEntries(auditable);
        LOGGER.info(activityDetail);
        MDC.clear();

    }

    public static void warn(Auditable auditable,String activityDetail) {
        addMdcSecurityEntries(auditable);
        LOGGER.warn(activityDetail);
        MDC.clear();

    }

    private static void addMdcSecurityEntries(Auditable auditable) {
    	MDC.put("logType", "auditlogs");
        MDC.put("activity", auditable.activity());
        MDC.put("event", auditable.event().name());
        if(SecurityUtils.getPersonTraits() != null) {
            MDC.put("user", SecurityUtils.getPersonTraits().getUser());
            MDC.put("tokenId", SecurityUtils.getPersonTraits().getTokenId());
        }
    }

}
