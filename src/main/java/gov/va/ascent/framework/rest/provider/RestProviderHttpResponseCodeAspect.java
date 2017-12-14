package gov.va.ascent.framework.rest.provider;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gov.va.ascent.framework.audit.AuditEventData;
import gov.va.ascent.framework.audit.AuditEvents;
import gov.va.ascent.framework.audit.AuditLogger;
import gov.va.ascent.framework.audit.Auditable;
import gov.va.ascent.framework.audit.RequestResponseAuditData;
import gov.va.ascent.framework.audit.RequestResponseLogSerializer;
import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.service.ServiceResponse;

/**
 * The Class RestHttpResponseCodeAspect is an aspect to alter HTTP response codes from our REST endpoints.
 * It defers to the MessagesToHttpStatusRulesEngine to determine codes.  
 * 
 * This aspect pointcuts on standard REST endpoints.  
 * Ensure you follow that pattern to make use of this standard aspect.
 *
 * @author jshrader
 * @see gov.va.ascent.framework.rest.provider.BaseRestProviderAspect 
 */
@Aspect
@Order(-9998)
public class RestProviderHttpResponseCodeAspect extends BaseRestProviderAspect {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestProviderHttpResponseCodeAspect.class);
	
	/** The rules engine. */
	private MessagesToHttpStatusRulesEngine rulesEngine;

	@Autowired
	RequestResponseLogSerializer asyncLogging;

	/**
     * Instantiates a new RestHttpResponseCodeAspect using a default MessagesToHttpStatusRulesEngine.
     * 
     * Use a custom bean and the other constructor to customize the rules.
     *
     */
	public RestProviderHttpResponseCodeAspect() {
		MessagesToHttpStatusRulesEngine ruleEngine = new MessagesToHttpStatusRulesEngine();
		ruleEngine.addRule(
				new MessageSeverityMatchRule(MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR)); 
		ruleEngine.addRule(
				new MessageSeverityMatchRule(MessageSeverity.ERROR, 
						HttpStatus.BAD_REQUEST));
		this.rulesEngine = ruleEngine;
    }
	
    /**
     * Instantiates a new RestHttpResponseCodeAspect using the specified MessagesToHttpStatusRulesEngine
     *
     * @param rulesEngine the rules engine
     */
    public RestProviderHttpResponseCodeAspect(MessagesToHttpStatusRulesEngine rulesEngine) {
    	this.rulesEngine = rulesEngine;
    }
    
	/**
	 * Log annotated method request response.
	 *
	 * @param joinPoint
	 *            the join point
	 * @return the object
	 */
	@Around("auditableExecution()")
	public Object logAnnotatedMethodRequestResponse(final ProceedingJoinPoint joinPoint) throws Throwable {
		Object response = null;
		Object request = null;

		if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
			request = joinPoint.getArgs()[0];
		}

		response = joinPoint.proceed();

		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		Auditable auditableAnnotation = method.getAnnotation(Auditable.class);

		if (auditableAnnotation != null && AuditEvents.REQUEST_RESPONSE.equals(auditableAnnotation.event())) {
			AuditEventData auditEventData = new AuditEventData(auditableAnnotation.event(),
					auditableAnnotation.activity(), auditableAnnotation.auditClass());
			writeAudit(request, response, auditEventData, MessageSeverity.INFO);
		}
		return response;
	}

	
	/**
	 * Around advice executes around the pointcut.
	 *
	 * @param joinPoint the join point
	 * @return the response entity
	 * @throws Throwable the throwable
	 */
	@SuppressWarnings("unchecked")
	@Around("!@annotation(gov.va.ascent.framework.audit.Auditable) && restController() && publicServiceResponseRestMethod()")
	public ResponseEntity<ServiceResponse> aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RestHttpResponseCodeAspect executing around method:" + joinPoint.toLongString());
		}

		ResponseEntity<ServiceResponse> responseObject = null;
		Object requestObject = null;

		if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
			requestObject = joinPoint.getArgs()[0];
		}

		final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		AuditEventData auditEventData = new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(),
				method.getDeclaringClass().getName());

		try {
			responseObject = (ResponseEntity<ServiceResponse>) joinPoint.proceed();

			ServiceResponse serviceResponse = responseObject.getBody();
			if (serviceResponse == null) {
				serviceResponse = new ServiceResponse();
			}
			final HttpStatus ruleStatus = rulesEngine.messagesToHttpStatus(serviceResponse.getMessages());

			if (ruleStatus != null && (HttpStatus.Series.valueOf(ruleStatus.value()) == HttpStatus.Series.SERVER_ERROR
					|| HttpStatus.Series.valueOf(ruleStatus.value()) == HttpStatus.Series.CLIENT_ERROR)) {
				writeAudit(requestObject, responseObject, auditEventData, MessageSeverity.ERROR);
				return new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				writeAudit(requestObject, responseObject, auditEventData, MessageSeverity.INFO);
			}
		} catch (AscentRuntimeException ascentRuntimeException) {
			responseObject = writeAuditError(ascentRuntimeException, auditEventData);
		} catch (Throwable throwable) {
			AscentRuntimeException ascentRuntimeException = new AscentRuntimeException(throwable);
			responseObject = writeAuditError(ascentRuntimeException, auditEventData);
		} finally {
			LOGGER.debug("RestHttpResponseCodeAspect after method was called.");
		}
		return responseObject;
	}
	
	/**
	 * Write into Audit when exceptions occur
	 * @param ascentRuntimeException
	 * @param auditEventData
	 * @return
	 */
	private ResponseEntity<ServiceResponse> writeAuditError(AscentRuntimeException ascentRuntimeException,
			AuditEventData auditEventData) {
		LOGGER.error("RestHttpResponseCodeAspect encountered uncaught exception in REST endpoint.",
				ascentRuntimeException);
		ServiceResponse serviceResponse = new ServiceResponse();
		serviceResponse.addMessage(MessageSeverity.FATAL, "UNEXPECTED_ERROR", ascentRuntimeException.getMessage());
		StringBuilder sb = new StringBuilder();
		sb.append("Error Message: ").append(ascentRuntimeException);
		AuditLogger.error(auditEventData, sb.toString());
		return new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Write audit.
	 *
     * @param request the request
     * @param response the response
	 * @param auditEventData the auditable annotation
	 */
	private void writeAudit(Object request, Object response,
			AuditEventData auditEventData, MessageSeverity messageSeverity) {
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
        asyncLogging.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData, messageSeverity);

	}

	private void getHttpRequestAuditData(HttpServletRequest httpServletRequest, RequestResponseAuditData requestResponseAuditData){
	    final Map<String, String> headers = new HashMap<>();

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

	    while(headerNames.hasMoreElements()){
	        String headerName = headerNames.nextElement();
	        headers.put(AuditLogger.sanitize(headerName), AuditLogger.sanitize(httpServletRequest.getHeader(headerName)));
        }
        requestResponseAuditData.setHeaders(headers);
	    requestResponseAuditData.setUri(AuditLogger.sanitize(httpServletRequest.getRequestURI()));
	    requestResponseAuditData.setMethod(AuditLogger.sanitize(httpServletRequest.getMethod()));
    }	
}
