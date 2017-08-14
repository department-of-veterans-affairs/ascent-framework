package gov.va.ascent.framework.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The Class AuditLogger.
 */
public class AuditLogger {
  
    final static Logger LOGGER = LoggerFactory.getLogger(AuditLogger.class);
    
    public static void debug(String userid, String activity,String activityDetail) {
      
      MDC.put("user", userid);
      MDC.put("activity", activity);
      LOGGER.debug(activityDetail);
      MDC.clear();
      
    }    

    public static void info(String userid, String activity, String activityDetail) {
      
      MDC.put("user", userid);
      MDC.put("activity", activity);
      LOGGER.info(activityDetail);
      MDC.clear();
            
    }
    
    public static void warn(String userid, String activity,String activityDetail) {
      
      MDC.put("user", userid);
      MDC.put("activity", activity);
      LOGGER.warn(activityDetail);
      MDC.clear();
      
    }    

}
