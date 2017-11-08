package gov.va.ascent.framework.audit;

import gov.va.ascent.framework.exception.AscentRuntimeException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * The Class RequestResponseAspect.
 */
@Aspect
public class RequestResponseAspect extends BaseAuditAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseAspect.class);

    @Autowired
    RequestResponseLogSerializer asyncLogging;

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
            AuditEventData auditEventData = new AuditEventData(auditableAnnotation.event(),
                    auditableAnnotation.activity(), auditableAnnotation.auditClass());
            writeAudit(request, response, auditEventData);
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
        LOGGER.info("logRestPublicMethodRequestResponse called. " + Thread.currentThread().getName());
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
         AuditEventData auditEventData = new AuditEventData(AuditEvents.REQUEST_RESPONSE,
                 method.getName(), method.getDeclaringClass().getName());
    	 writeAudit(request, response, auditEventData);

         return response;
		
    }
    
	/**
	 * Write audit.
	 *
     * @param request the request
     * @param response the response
	 * @param auditEventData the auditable annotation
	 */
	private void writeAudit(Object request, Object response,
			AuditEventData auditEventData) {
		RequestResponseAuditData requestResponseAuditData = new RequestResponseAuditData();

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if(httpServletRequest != null){
            getHttpRequestAuditData(httpServletRequest, requestResponseAuditData);
        }
        if(request != null){
            requestResponseAuditData.setRequest(request);
        }
        if(response != null){
            requestResponseAuditData.setResponse(response);
        }
        asyncLogging.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData);

	}

	private void getHttpRequestAuditData(HttpServletRequest httpServletRequest, RequestResponseAuditData requestResponseAuditData){
	    final Map<String, String> headers = new HashMap<>();

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

	    while(headerNames.hasMoreElements()){
	        String headerName = headerNames.nextElement();
	        headers.put(headerName, httpServletRequest.getHeader(headerName));
        }
        requestResponseAuditData.setHeaders(headers);
	    requestResponseAuditData.setUri(httpServletRequest.getRequestURI());
	    requestResponseAuditData.setMethod(httpServletRequest.getMethod());
    }
}

