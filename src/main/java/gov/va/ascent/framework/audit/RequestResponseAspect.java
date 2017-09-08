package gov.va.ascent.framework.audit;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by vgadda on 8/17/17.
 */
@Aspect
public class RequestResponseAspect extends BaseAuditAspect {

    @Autowired
    ObjectMapper mapper;

    @Around("auditableExecution()")
    public Object logRequestResponse(final ProceedingJoinPoint joinPoint) {
        Object returnObject = null;
        Object request = joinPoint.getArgs()[0];
        try {
            returnObject = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Auditable auditableAnnotation = method.getAnnotation(Auditable.class);
        if(AuditEvents.REQUEST_RESPONSE.equals(auditableAnnotation.event())) {
            try {
                AuditLogger.info(auditableAnnotation, mapper.writeValueAsString(new RequestResponse(request, returnObject)));
            }catch (JsonProcessingException ex) {
                AuditLogger.warn(auditableAnnotation, ex.getMessage());
            }
        }
        return returnObject;
    }


}

class RequestResponse implements Serializable{
    /**
	 * 
	 */
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
