package gov.va.ascent.framework.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * The Class RestProviderHttpResponseCodeAspect is an aspect to alter HTTP response codes from our REST endpoints.
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
	
	private static final int NUMBER_OF_BYTES = 1024;
	
	/** The rules engine. */
	private MessagesToHttpStatusRulesEngine rulesEngine;

	@Autowired
	RequestResponseLogSerializer asyncLogging;

	/**
     * Instantiates a new RestProviderHttpResponseCodeAspect using a default MessagesToHttpStatusRulesEngine.
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
     * Instantiates a new RestProviderHttpResponseCodeAspect using the specified MessagesToHttpStatusRulesEngine
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
		List<Object> request = null;

		if (joinPoint.getArgs().length > 0) {
			request = Arrays.asList(joinPoint.getArgs());
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
	@SuppressWarnings({"unchecked","squid:MethodCyclomaticComplexity"})
	@Around("!@annotation(gov.va.ascent.framework.audit.Auditable) && restController() && publicServiceResponseRestMethod()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RestProviderHttpResponseCodeAspect executing around method:" + joinPoint.toLongString());
		}
		
		Object responseObject = null;
		List<Object> requestObject = null;

		if (joinPoint.getArgs().length > 0) {
			requestObject = Arrays.asList(joinPoint.getArgs());
		}
		
		final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		final HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		
		boolean returnTypeIsServiceResponse = method.getReturnType().toString().contains("ResponseEntity") ? false : true;
		
		AuditEventData auditEventData = new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(),
				method.getDeclaringClass().getName());
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Request Object: {}", requestObject);
			LOGGER.debug("Method: {}", method);
			LOGGER.debug("Return Type as ResponseEntity: {}", returnTypeIsServiceResponse);
			LOGGER.debug("AuditEventData Object: {}", auditEventData.toString());
		}

		ServiceResponse serviceResponse = null;
		try {
			responseObject = joinPoint.proceed();
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Response Object: {}", responseObject);
			}
	
			if (responseObject == null) {
				serviceResponse = new ServiceResponse();
			} else if (responseObject instanceof ServiceResponse) {
				serviceResponse = (ServiceResponse)  responseObject;
			} else {
				serviceResponse = ((ResponseEntity<ServiceResponse>)responseObject).getBody();
			}
			
			final HttpStatus ruleStatus = rulesEngine.messagesToHttpStatus(serviceResponse.getMessages());

			if (ruleStatus != null && (HttpStatus.Series.valueOf(ruleStatus.value()) == HttpStatus.Series.SERVER_ERROR
					|| HttpStatus.Series.valueOf(ruleStatus.value()) == HttpStatus.Series.CLIENT_ERROR)) {
				LOGGER.debug("HttpStatus {}", ruleStatus.value());
				writeAudit(requestObject, responseObject, auditEventData, MessageSeverity.ERROR);
				
				if (returnTypeIsServiceResponse) {
					response.setStatus(ruleStatus.value());
					return serviceResponse;
				} else {
					return new ResponseEntity<>(serviceResponse, ruleStatus);
				}
			} else {
				writeAudit(requestObject, responseObject, auditEventData, MessageSeverity.INFO);
			}
		} catch (AscentRuntimeException ascentRuntimeException) {
			responseObject = writeAuditError(ascentRuntimeException, auditEventData);
			if (response !=null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			return getReturnResponse(returnTypeIsServiceResponse, responseObject);
		} catch (Throwable throwable) {
			AscentRuntimeException ascentRuntimeException = new AscentRuntimeException(throwable);
			responseObject = writeAuditError(ascentRuntimeException, auditEventData);
			if (response !=null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			return getReturnResponse(returnTypeIsServiceResponse, responseObject);
		} finally {
			LOGGER.debug("RestProviderHttpResponseCodeAspect after method was called.");
		}
		
		return responseObject;
	}
	
	/**
	 * 
	 * @param returnTypeIsServiceResponse
	 * @param responseObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object getReturnResponse(boolean returnTypeIsServiceResponse, Object responseObject) {
		if (returnTypeIsServiceResponse) {
			return ((ResponseEntity<ServiceResponse>)responseObject).getBody();
		} else {
			return responseObject;
		}
	}
	/**
	 * Write into Audit when exceptions occur
	 * @param ascentRuntimeException
	 * @param auditEventData
	 * @return
	 */
	private ResponseEntity<ServiceResponse> writeAuditError(AscentRuntimeException ascentRuntimeException,
			AuditEventData auditEventData) {
		LOGGER.error("RestProviderHttpResponseCodeAspect encountered uncaught exception in REST endpoint.",
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
	private void writeAudit(List<Object> request, Object response,
			AuditEventData auditEventData, MessageSeverity messageSeverity) {
		
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		
		RequestResponseAuditData requestResponseAuditData = new RequestResponseAuditData();
		
        if(request != null){
            requestResponseAuditData.setRequest(request);
        }
        if(response != null){
            requestResponseAuditData.setResponse(response);
        }
        
        if(httpServletRequest != null) {
            getHttpRequestAuditData(httpServletRequest, requestResponseAuditData);
        }
        
        LOGGER.debug("Invoking asyncLogRequestResponseAspectAuditData");
        
        if (asyncLogging != null) {
        		asyncLogging.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData, messageSeverity);
        }
	}

	private void getHttpRequestAuditData(HttpServletRequest httpServletRequest,
			RequestResponseAuditData requestResponseAuditData) {
		final Map<String, String> headers = new HashMap<>();
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String value = httpServletRequest.getHeader(headerName);
			headers.put(headerName, value);
		}
		requestResponseAuditData.setHeaders(headers);
		requestResponseAuditData.setUri(httpServletRequest.getRequestURI());
		requestResponseAuditData.setMethod(httpServletRequest.getMethod());
		
		final String contentType = httpServletRequest.getContentType();
		
		LOGGER.debug("Content Type: {}", contentType);
		
		if (contentType != null
				&& (contentType.toLowerCase(Locale.ENGLISH).startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)
						|| contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/mixed"))) {
			List<String> attachmentTextList = new ArrayList<>();
			InputStream inputstream = null;
			try {
				for (Part part : httpServletRequest.getParts()) {
					inputstream = part.getInputStream();
					attachmentTextList.add(convertBytesToString(inputstream));
					inputstream.close();
				}
			} catch (Exception ex) {
				LOGGER.error("Error occurred while reading the upload file. {}", ex);
			} finally {
				IOUtils.closeQuietly(inputstream);
			}
			LOGGER.debug("Attachment Text List: {}", attachmentTextList);
			requestResponseAuditData.setAttachmentTextList(attachmentTextList);
			requestResponseAuditData.setRequest(null);
		}

	}

	/**
	 * Read the first 1024 bytes and convert that into a string.
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String convertBytesToString(InputStream in) throws IOException {
		int offset = 0;
		int bytesRead = 0;
		byte[] data = new byte[NUMBER_OF_BYTES];
		while ((bytesRead = in.read(data, offset, data.length - offset)) != -1) {
			offset += bytesRead;
			if (offset >= data.length) {
				break;
			}
		}
		return new String(data, 0, offset, "UTF-8");
	}
}
