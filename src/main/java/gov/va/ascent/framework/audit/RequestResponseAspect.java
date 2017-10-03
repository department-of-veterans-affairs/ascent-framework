package gov.va.ascent.framework.audit;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * The Class RequestResponseAspect.
 */
@Aspect
public class RequestResponseAspect extends BaseAuditAspect {

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
            throw new RuntimeException(throwable);
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Auditable auditableAnnotation = method.getAnnotation(Auditable.class);
        
        if(auditableAnnotation != null && AuditEvents.REQUEST_RESPONSE.equals(auditableAnnotation.event())) {
            auditableAnnotation = getAuditableInstance(auditableAnnotation, method);
            writeAudit(joinPoint, new RequestResponse(request, response), auditableAnnotation);
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
    @Around("!@annotation(gov.va.ascent.framework.audit.Auditable) && auditRestController() && auditPublicServiceResponseRestMethod()")
	public Object logRestPublicMethodRequestResponse(final ProceedingJoinPoint joinPoint) throws Throwable {
    	 
    	 Object response = null;
    	 Object request = null;
    	 if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
    		 request = joinPoint.getArgs()[0];
    	 }
         
         try {
        	 response = joinPoint.proceed();
         } catch (Throwable throwable) {
             throw new RuntimeException(throwable);
         }
         final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
         final Auditable auditableAnnotation = getDefaultAuditableInstance(method);
         
    	 writeAudit(joinPoint, new RequestResponse(request, response), auditableAnnotation);
         
         return response;
		
    }
    
	/**
	 * Write audit.
	 *
	 * @param joinPoint the join point
	 * @param returnObject the return object
	 * @param request the request
	 * @param auditableAnnotation the auditable annotation
	 */
	private void writeAudit(final ProceedingJoinPoint joinPoint, RequestResponse requestResponse,
			Auditable auditableAnnotation) {
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
			request = getMethodAndArgumentsAsString(joinPoint);
		}
		try {
			if (requestResponse.getResponse()!=null) {
				 // serialize response as json
				response = mapper.writeValueAsString(requestResponse.getResponse()); 
			}
		} catch (JsonProcessingException ex) {
			response = getResultAsString(requestResponse.getResponse());
		}
		AuditLogger.info(auditableAnnotation, request.concat(response));
	}
}

class RequestResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Object request;
    private Object response;

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


