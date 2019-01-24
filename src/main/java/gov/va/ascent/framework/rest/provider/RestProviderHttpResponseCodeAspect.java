package gov.va.ascent.framework.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import org.slf4j.event.Level;
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
import gov.va.ascent.framework.audit.RequestAuditData;
import gov.va.ascent.framework.audit.RequestResponseLogSerializer;
import gov.va.ascent.framework.audit.ResponseAuditData;
import gov.va.ascent.framework.constants.AnnotationConstants;
import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.log.AscentBanner;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.service.ServiceResponse;
import gov.va.ascent.framework.util.SanitizationUtil;

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
	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(RestProviderHttpResponseCodeAspect.class);

	private static final int NUMBER_OF_BYTES = 1024;

	/** The rules engine. */
	private final MessagesToHttpStatusRulesEngine rulesEngine;

	@Autowired
	RequestResponseLogSerializer asyncLogging;

	/**
	 * Instantiates a new RestProviderHttpResponseCodeAspect using a default MessagesToHttpStatusRulesEngine.
	 *
	 * Use a custom bean and the other constructor to customize the rules.
	 *
	 */
	public RestProviderHttpResponseCodeAspect() {
		final MessagesToHttpStatusRulesEngine ruleEngine = new MessagesToHttpStatusRulesEngine();
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
	public RestProviderHttpResponseCodeAspect(final MessagesToHttpStatusRulesEngine rulesEngine) {
		this.rulesEngine = rulesEngine;
	}

	/**
	 * Advice to log methods that are annotated with @Auditable. Separately logs the call to the method and its arguments, and the
	 * response from the method.
	 *
	 * @param joinPoint
	 *            the join point
	 * @return the object
	 */
	@Around("auditableExecution()")
	public Object logAnnotatedMethodRequestResponse(final ProceedingJoinPoint joinPoint) throws Throwable {
		Object response = null;
		List<Object> request = null;

		try {
			if (joinPoint.getArgs().length > 0) {
				request = Arrays.asList(joinPoint.getArgs());
			}

			final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
			final Auditable auditableAnnotation = method.getAnnotation(Auditable.class);
			AuditEventData auditEventData;
			if (auditableAnnotation != null) {
				auditEventData =
						new AuditEventData(auditableAnnotation.event(), auditableAnnotation.activity(),
								auditableAnnotation.auditClass());

				writeRequestInfoAudit(request, auditEventData);
			}

			response = joinPoint.proceed();

			if (auditableAnnotation != null) {
				auditEventData = new AuditEventData(auditableAnnotation.event(), auditableAnnotation.activity(),
						auditableAnnotation.auditClass());
				writeResponseAudit(response, auditEventData, MessageSeverity.INFO, null);
			}
		} catch (Throwable e) {
			LOGGER.error(AscentBanner.newBanner(AnnotationConstants.INTERCEPTOR_EXCEPTION, Level.ERROR), 
					"Error while executing logAnnotatedMethodRequestResponse around auditableExecution", e);
		}
		return response;
	}

	/**
	 * Around advice to separately log Audits for REST request object and response object, and alter HTTP response codes.
	 *
	 * @param joinPoint the join point
	 * @return the response entity
	 * @throws Throwable the throwable
	 */
	@SuppressWarnings({ "unchecked", "squid:MethodCyclomaticComplexity" })
	@Around("!@annotation(gov.va.ascent.framework.audit.Auditable) && restController() && publicServiceResponseRestMethod()")
	public Object aroundAdvice(final ProceedingJoinPoint joinPoint) throws Throwable {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RestProviderHttpResponseCodeAspect executing around method:" + joinPoint.toLongString());
		}

		Object responseObject = null;
		List<Object> requestObject = null;
		ServiceResponse serviceResponse = null;
		AuditEventData auditEventData = null;
		boolean returnTypeIsServiceResponse = false;
		HttpServletResponse response = null;
		Method method = null;

		if (joinPoint.getArgs().length > 0) {
			requestObject = Arrays.asList(joinPoint.getArgs());
		}

		try {
			method = ((MethodSignature) joinPoint.getSignature()).getMethod();
			response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

			returnTypeIsServiceResponse = method.getReturnType().toString().contains("ResponseEntity") ? false : true;

			auditEventData = new AuditEventData(AuditEvents.REST_REQUEST, method.getName(), method.getDeclaringClass().getName());

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Request Object: {}", requestObject);
				LOGGER.debug("Method: {}", method);
				LOGGER.debug("Return Type as ResponseEntity: {}", returnTypeIsServiceResponse);
				LOGGER.debug("AuditEventData Object: {}", auditEventData.toString());
			}
			writeRequestInfoAudit(requestObject, auditEventData);

			responseObject = joinPoint.proceed();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Response Object: {}", responseObject);
			}

			if (responseObject == null) {
				serviceResponse = new ServiceResponse();
			} else if (responseObject instanceof ServiceResponse) {
				serviceResponse = (ServiceResponse) responseObject;
			} else {
				serviceResponse = ((ResponseEntity<ServiceResponse>) responseObject).getBody();
			}

			final HttpStatus ruleStatus = rulesEngine.messagesToHttpStatus(serviceResponse.getMessages());

			auditEventData = new AuditEventData(AuditEvents.REST_RESPONSE, method.getName(), method.getDeclaringClass().getName());
			if (ruleStatus != null && (HttpStatus.Series.valueOf(ruleStatus.value()) == HttpStatus.Series.SERVER_ERROR
					|| HttpStatus.Series.valueOf(ruleStatus.value()) == HttpStatus.Series.CLIENT_ERROR)) {
				LOGGER.debug("HttpStatus {}", ruleStatus.value());
				writeResponseAudit(responseObject, auditEventData, MessageSeverity.ERROR, null);

				if (returnTypeIsServiceResponse) {
					response.setStatus(ruleStatus.value());
					return serviceResponse;
				} else {
					return new ResponseEntity<>(serviceResponse, ruleStatus);
				}
			} else {
				writeResponseAudit(responseObject, auditEventData, MessageSeverity.INFO, null);
			}
		} catch (final AscentRuntimeException ascentRuntimeException) {
			Object returnObj = null;
			LOGGER.error(AscentBanner.newBanner(AnnotationConstants.INTERCEPTOR_EXCEPTION, Level.ERROR), 
					"Error while executing RestProviderHttpResponseCodeAspect.aroundAdvice around restController",
					ascentRuntimeException);
			try {
				responseObject = writeAuditError(ascentRuntimeException, auditEventData);
				if (response != null) {
					response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				}
				returnObj = getReturnResponse(returnTypeIsServiceResponse, responseObject);
			} catch (Throwable e) { // NOSONAR intentionally catching throwable
				LOGGER.error(AscentBanner.newBanner(AnnotationConstants.INTERCEPTOR_EXCEPTION, Level.ERROR), 
						"Throwable occured while attempting to writeAuditError for AscentRuntimeException.", e);

			}
			return returnObj;
		} catch (final Throwable throwable) { // NOSONAR intentionally catching throwable
			Object returnObj = null;
			LOGGER.error(AscentBanner.newBanner(AnnotationConstants.INTERCEPTOR_EXCEPTION, Level.ERROR), 
					"Throwable while executing RestProviderHttpResponseCodeAspect.aroundAdvice around restController", throwable);
				try {
				final AscentRuntimeException ascentRuntimeException = new AscentRuntimeException(throwable);
				responseObject = writeAuditError(ascentRuntimeException, auditEventData);
				if (response != null) {
					response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				}
				returnObj = getReturnResponse(returnTypeIsServiceResponse, responseObject);
			} catch (Throwable e) { // NOSONAR intentionally catching throwable
				LOGGER.error(AscentBanner.newBanner(AnnotationConstants.INTERCEPTOR_EXCEPTION, Level.ERROR), 
						"Throwable occured while attempting to writeAuditError for Throwable.", e);
			}
			return returnObj;
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
	private Object getReturnResponse(final boolean returnTypeIsServiceResponse, final Object responseObject) {
		if (returnTypeIsServiceResponse) {
			return ((ResponseEntity<ServiceResponse>) responseObject).getBody();
		} else {
			return responseObject;
		}
	}

	/**
	 * Write into Audit when exceptions occur
	 *
	 * @param ascentRuntimeException
	 * @param auditEventData
	 * @return
	 */
	private ResponseEntity<ServiceResponse> writeAuditError(final AscentRuntimeException ascentRuntimeException,
			final AuditEventData auditEventData) {
		LOGGER.error("RestProviderHttpResponseCodeAspect encountered uncaught exception in REST endpoint.", ascentRuntimeException);
		final ServiceResponse serviceResponse = new ServiceResponse();
		serviceResponse.addMessage(MessageSeverity.FATAL, "UNEXPECTED_ERROR", ascentRuntimeException.getMessage());
		final StringBuilder sb = new StringBuilder();
		sb.append("Error Message: ").append(ascentRuntimeException);
		AuditLogger.error(auditEventData, sb.toString(), ascentRuntimeException);
		return new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Write audit for request.
	 *
	 * @param request the request
	 * @param auditEventData the auditable annotation
	 */
	private void writeRequestInfoAudit(final List<Object> request, final AuditEventData auditEventData) {

		final HttpServletRequest httpServletRequest =
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		final RequestAuditData requestAuditData = new RequestAuditData();

		if (request != null) {
			requestAuditData.setRequest(request);
		}

		if (httpServletRequest != null) {
			getHttpRequestAuditData(httpServletRequest, requestAuditData);
		}

		LOGGER.debug("Invoking asyncLogRequestResponseAspectAuditData");

		if (asyncLogging != null) {
			asyncLogging.asyncLogRequestResponseAspectAuditData(auditEventData, requestAuditData, RequestAuditData.class,
					MessageSeverity.INFO, null);
		}
	}

	/**
	 * Write audit for response.
	 *
	 * @param response the response
	 * @param auditEventData the auditable annotation
	 */
	private void writeResponseAudit(final Object response, final AuditEventData auditEventData, final MessageSeverity messageSeverity,
			final Throwable t) {

		final HttpServletResponse httpServletReponse =
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

		final ResponseAuditData responseAuditData = new ResponseAuditData();

		if (httpServletReponse != null) {
			getHttpResponseAuditData(httpServletReponse, responseAuditData);
		}

		if (response != null) {
			responseAuditData.setResponse(response);
		}

		LOGGER.debug("Invoking asyncLogRequestResponseAspectAuditData");

		if (asyncLogging != null) {
			asyncLogging.asyncLogRequestResponseAspectAuditData(auditEventData, responseAuditData, ResponseAuditData.class,
					messageSeverity, t);
		}
	}

	private void getHttpResponseAuditData(final HttpServletResponse httpServletReponse, final ResponseAuditData responseAuditData) {
		final Map<String, String> headers = new HashMap<>();
		final Collection<String> headerNames = httpServletReponse.getHeaderNames();
		populateHeadersMap(httpServletReponse, headers, headerNames);
		responseAuditData.setHeaders(headers);
	}

	private void getHttpRequestAuditData(final HttpServletRequest httpServletRequest, final RequestAuditData requestAuditData) {
		final Map<String, String> headers = new HashMap<>();
		httpServletRequest.getHeaderNames();

		ArrayList<String> listOfHeaderNames = Collections.list(httpServletRequest.getHeaderNames());
		populateHeadersMap(httpServletRequest, headers, listOfHeaderNames);

		requestAuditData.setHeaders(headers);
		requestAuditData.setUri(httpServletRequest.getRequestURI());
		requestAuditData.setMethod(httpServletRequest.getMethod());

		final String contentType = httpServletRequest.getContentType();

		LOGGER.debug("Content Type: {}", SanitizationUtil.stripXSS(contentType));

		if (contentType != null && (contentType.toLowerCase(Locale.ENGLISH).startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)
				|| contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/mixed"))) {
			final List<String> attachmentTextList = new ArrayList<>();
			InputStream inputstream = null;
			// NOSONAR try (ByteArrayInputStream partTooBigMessage =
			// NOSONAR new ByteArrayInputStream("This part of the request is too big to be displayed.".getBytes())) {
			try {
				for (final Part part : httpServletRequest.getParts()) {
					final Map<String, String> partHeaders = new HashMap<>();
					populateHeadersMap(part, partHeaders, part.getHeaderNames());
					inputstream = part.getInputStream();
					// NOSONAR if (inputstream.available() > gov.va.ascent.framework.log.AscentBaseLogger.MAX_MSG_LENGTH) {
					// NOSONAR inputstream.close();
					// NOSONAR inputstream = partTooBigMessage;
					// NOSONAR }
					attachmentTextList.add(partHeaders.toString() + ", " + convertBytesToString(inputstream));
					IOUtils.closeQuietly(inputstream);
					// NOSONAR IOUtils.closeQuietly(partTooBigMessage);
				}
			} catch (final Exception ex) {
				LOGGER.error(AscentBanner.newBanner(AnnotationConstants.INTERCEPTOR_EXCEPTION, Level.ERROR), 
						"Error occurred while reading the upload file. {}", ex);

			} finally {
				IOUtils.closeQuietly(inputstream);
			}
			requestAuditData.setAttachmentTextList(attachmentTextList);
			requestAuditData.setRequest(null);
		}
	}

	private void populateHeadersMap(final HttpServletRequest httpServletRequest, final Map<String, String> headersToBePopulated,
			final Collection<String> listOfHeaderNames) {
		for (final String headerName : listOfHeaderNames) {
			String value;
			value = httpServletRequest.getHeader(headerName);
			headersToBePopulated.put(headerName, value);
		}
	}

	private void populateHeadersMap(final HttpServletResponse httpServletResponse, final Map<String, String> headersToBePopulated,
			final Collection<String> listOfHeaderNames) {
		for (final String headerName : listOfHeaderNames) {
			String value;
			value = httpServletResponse.getHeader(headerName);
			headersToBePopulated.put(headerName, value);
		}
	}

	private void populateHeadersMap(final Part part, final Map<String, String> headersToBePopulated,
			final Collection<String> listOfHeaderNames) {
		for (final String headerName : listOfHeaderNames) {
			String value;
			value = part.getHeader(headerName);
			headersToBePopulated.put(headerName, value);
		}
	}

	/**
	 * Read the first 1024 bytes and convert that into a string.
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	protected static String convertBytesToString(final InputStream in) throws IOException {
		int offset = 0;
		int bytesRead = 0;
		final byte[] data = new byte[NUMBER_OF_BYTES];
		while ((bytesRead = in.read(data, offset, data.length - offset)) != -1) {
			offset += bytesRead;
			if (offset >= data.length) {
				break;
			}
		}
		return new String(data, 0, offset, "UTF-8");
	}
}
