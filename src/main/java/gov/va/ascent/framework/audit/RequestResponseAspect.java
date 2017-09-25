package gov.va.ascent.framework.audit;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.va.ascent.framework.exception.AscentRuntimeException;

/**
 * Created by vgadda on 8/17/17.
 */
@Aspect
public class RequestResponseAspect extends BaseAuditAspect {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseAspect.class);

    @Autowired
    ObjectMapper mapper;

    @Around("auditableExecution()")
    public Object logRequestResponse(final ProceedingJoinPoint joinPoint) {
        Object returnObject = null;
        Object request = joinPoint.getArgs()[0];
        try {
            returnObject = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new AscentRuntimeException(throwable);
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Auditable auditableAnnotation = method.getAnnotation(Auditable.class);
        if(AuditEvents.REQUEST_RESPONSE.equals(auditableAnnotation.event())) {
            try {
                AuditLogger.info(auditableAnnotation, mapper.writeValueAsString(new RequestResponse(request, returnObject)));
            }catch (JsonProcessingException ex) {
            		LOGGER.error("JsonProcessingException while logging.", ex);
                AuditLogger.warn(auditableAnnotation, ex.getMessage());
            }
        }
        return returnObject;
    }


}

class RequestResponse implements Serializable{

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
