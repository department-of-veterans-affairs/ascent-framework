package gov.va.ascent.framework.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.va.ascent.framework.messages.MessageSeverity;

/**
 * @author npaulus
 * The purpose of this class is to asynchronuously serialize an object to JSON and then write it to the audit logs.
 */
@Component
public class RequestResponseLogSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseLogSerializer.class);

    @Autowired
    ObjectMapper mapper;

    /**
     * Asynchronuously converts an object to JSON and then writes it to the audit logger.
     * @param auditEventData Data specific to the audit event
     * @param requestResponseAuditData The request and response audit data
     */
    @Async
    public void asyncLogRequestResponseAspectAuditData(
    		AuditEventData auditEventData, RequestResponseAuditData requestResponseAuditData, MessageSeverity messageSeverity){

        String auditDetails;
        try{
        	mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            auditDetails = mapper.writeValueAsString(requestResponseAuditData);
        } catch (JsonProcessingException ex){
            LOGGER.error("Error occurred on JSON processing, calling toString", ex);
            auditDetails = requestResponseAuditData.toString();
        }
        if (messageSeverity.equals(MessageSeverity.ERROR) || messageSeverity.equals(MessageSeverity.FATAL)) {
        		AuditLogger.error(auditEventData, auditDetails);
        } else {
        		AuditLogger.info(auditEventData, auditDetails); 
        }
    }


}
