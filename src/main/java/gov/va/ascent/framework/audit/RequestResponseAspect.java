package gov.va.ascent.framework.audit;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.ascent.framework.exception.AscentRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * The Class RequestResponseAspect.
 */
@Aspect
public class RequestResponseAspect extends BaseAuditAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseAspect.class);

    @Autowired
    ObjectMapper mapper;

    /**
     * Log annotated method request response.
     *
     * @param joinPoint the join point
     * @return the object
     */
    @Around("auditableExecution()")
    public Object logAnnotatedMethodRequestResponse(final ProceedingJoinPoint joinPoint) {
        Object response = null;
        Object request = null;
        
        if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
   		 request = joinPoint.getArgs()[0];
   	 	}
        try {
        	response = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new AscentRuntimeException(throwable);
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Auditable auditableAnnotation = method.getAnnotation(Auditable.class);
        
        if(auditableAnnotation != null && AuditEvents.REQUEST_RESPONSE.equals(auditableAnnotation.event())) {
            AuditData auditData = new AuditData(auditableAnnotation.event(),
                    auditableAnnotation.activity(), auditableAnnotation.auditClass());
            writeAudit(joinPoint, new RequestResponse(request, response), auditData);
        }
        return response;
    }
    
    /**
     * Log rest public method request response.
     *
     * @param joinPoint the join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("!@annotation(gov.va.ascent.framework.audit.Auditable) && auditRestController() || auditPublicServiceResponseRestMethod()")
	public Object logRestPublicMethodRequestResponse(final ProceedingJoinPoint joinPoint) throws Throwable {
    	 
    	 Object response = null;
    	 Object request = null;
    	 if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
    		 request = joinPoint.getArgs()[0];
    	 }
         
         try {
        	 response = joinPoint.proceed();
         } catch (Throwable throwable) {
             throw new AscentRuntimeException(throwable);
         }
         final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
         AuditData auditData = new AuditData(AuditEvents.REQUEST_RESPONSE,
                 method.getName(), method.getDeclaringClass().getName());
    	 writeAudit(joinPoint, new RequestResponse(request, response), auditData);
         
         return response;
		
    }
    
	/**
	 * Write audit.
	 *
	 * @param joinPoint the join point
     * @param requestResponse the request and response
	 * @param auditData the auditable annotation
	 */
	private void writeAudit(final ProceedingJoinPoint joinPoint, RequestResponse requestResponse,
			AuditData auditData) {
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		String request = StringUtils.EMPTY;
		String response = StringUtils.EMPTY;
		try {
			if (requestResponse.getRequest()!=null) {
				// serialize request as json
				request = mapper.writeValueAsString(requestResponse.getRequest()); 
			}
		} catch (JsonProcessingException ex) {
		    LOGGER.error("Json Processing Exception occurred, converting to string: ", ex);
			request = getMethodAndArgumentsAsString(joinPoint);
		}
		try {
			if (requestResponse.getResponse()!=null) {
				 // serialize response as json
				response = mapper.writeValueAsString(requestResponse.getResponse()); 
			}
		} catch (JsonProcessingException ex) {
            LOGGER.error("Json Processing Exception occurred, converting to string: ", ex);
			response = getResultAsString(requestResponse.getResponse());
		}
            AuditLogger.info(auditData, request.concat(response));
	}
}

class RequestResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private transient Object request;
    private transient Object response;

    public RequestResponse(Object request, Object response) {
        this.request = request;
        this.response = response;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
