package gov.va.ascent.framework.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
    public void asyncLogRequestResponseAspectAuditData(AuditEventData auditEventData, RequestResponseAuditData requestResponseAuditData){

        String auditDetails;
        try{
            auditDetails = mapper.writeValueAsString(requestResponseAuditData);
        } catch (JsonProcessingException ex){
            LOGGER.error("Error occurred on JSON processing, calling toString", ex);
            auditDetails = requestResponseAuditData.toString();
        }
        AuditLogger.info(auditEventData, auditDetails);
    }


}
