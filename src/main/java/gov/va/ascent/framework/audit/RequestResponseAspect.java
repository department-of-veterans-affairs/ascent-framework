package gov.va.ascent.framework.audit;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            writeAudit(joinPoint, request, response, auditData);
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
             throw new AscentRuntimeException(throwable);
         }
         final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
         AuditData auditData = new AuditData(AuditEvents.REQUEST_RESPONSE,
                 method.getName(), method.getDeclaringClass().getName());
    	 writeAudit(joinPoint, request, response, auditData);

         return response;
		
    }
    
	/**
	 * Write audit.
	 *
	 * @param joinPoint the join point
     * @param request the request
     * @param response the response
	 * @param auditData the auditable annotation
	 */
	private void writeAudit(final ProceedingJoinPoint joinPoint, Object request, Object response,
			AuditData auditData) {
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		RequestResponseAuditData requestResponseAuditData = new RequestResponseAuditData();

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if(httpServletRequest != null){
            requestResponseAuditData.setHttpRequestAuditData(getHttpRequestAuditData(httpServletRequest));
        }

		try {
			if (request != null) {
				// serialize request as json
                requestResponseAuditData.setRequest(mapper.writeValueAsString(request));
			}
		} catch (JsonProcessingException ex) {
		    LOGGER.error("Json Processing Exception occurred, converting to string: ", ex);
            requestResponseAuditData.setRequest(getMethodAndArgumentsAsString(joinPoint));
		}
		try {
			if (response != null) {
				 // serialize response as json
                requestResponseAuditData.setResponse(mapper.writeValueAsString(response));
			}
		} catch (JsonProcessingException ex) {
            LOGGER.error("Json Processing Exception occurred, converting to string: ", ex);
            requestResponseAuditData.setResponse(getResultAsString(response));
		}
		try{
            AuditLogger.info(auditData, mapper.writeValueAsString(requestResponseAuditData));
        } catch (JsonProcessingException ex){
		    LOGGER.error("Error occurred on JSON processing, calling toString", ex);
		    AuditLogger.info(auditData, requestResponseAuditData.toString());
        }
	}

	private HttpRequestAuditData getHttpRequestAuditData(HttpServletRequest request){
	    final Map<String, String> headers = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();

	    while(headerNames.hasMoreElements()){
	        String headerName = headerNames.nextElement();
	        headers.put(headerName, request.getHeader(headerName));
        }
        return new HttpRequestAuditData(headers, request.getRequestURI(),
                request.getMethod());
    }
}

