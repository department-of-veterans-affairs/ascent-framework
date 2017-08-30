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

    public static void debug(String activity,String activityDetail) {
        addMdcSecurityEntries();
        MDC.put("activity", activity);
        LOGGER.debug(activityDetail);
        MDC.clear();

    }

    public static void info(String activity, String activityDetail) {
        addMdcSecurityEntries();
        MDC.put("activity", activity);
        LOGGER.info(activityDetail);
        MDC.clear();

    }

    public static void warn(String activity,String activityDetail) {
        addMdcSecurityEntries();
        MDC.put("activity", activity);
        LOGGER.warn(activityDetail);
        MDC.clear();

    }

    private static void addMdcSecurityEntries(){
        if(SecurityUtils.getPersonTraits() != null) {
            MDC.put("user", SecurityUtils.getPersonTraits().getUser());
            MDC.put("tokenId", SecurityUtils.getPersonTraits().getTokenId());
        }
    }

}
