package gov.va.ascent.framework.ws.client.remote;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ascent.framework.audit.RequestResponseAuditData;
import gov.va.ascent.framework.audit.RequestResponseLogSerializer;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ascent.framework.util.Defense;

public class RemoteServiceCallAspect extends BaseRemoteServiceCallAspect {

	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(RemoteServiceCallAspect.class);

	private static final String SOAP_METHOD = "POST";

	/** NOTE: Do not reference this member directly. Use getRequestResponseLogSerializer() */
	@Autowired
	private RequestResponseLogSerializer requestResponseLogSerializer;

	/** Getter for the Autowired member variable. This getter must be used, and is required to facilitate unit testing */
	RequestResponseLogSerializer getRequestResponseLogSerializer() {
		return requestResponseLogSerializer;
	}

	/**
	 * Around advice for partner service calls to audit the request and response.
	 *
	 * @param joinPoint supplied by spring-aop
	 * @param webserviceTemplate axiom WebServiceTemplate created by the IMPL
	 * @param request the request, a xjc-generated subclass of AbstractTransferObject
	 * @param requestClass the class of the request object
	 * @return AbstractTransferObject the response, a xjc-generated subclass of AbstractTransferObject
	 * @throws Throwable something went wrong with the RemoteServiceCall that occurs within this aspect
	 */
	@SuppressWarnings("unchecked")
	@Around("standardRemoteServiceCallMethod() && args(webserviceTemplate, request, requestClass)")
	public Object aroundAdvice(final ProceedingJoinPoint joinPoint, final WebServiceTemplate webserviceTemplate,
			final PartnerTransferObjectMarker request, final Class<? extends PartnerTransferObjectMarker> requestClass)
			throws Throwable {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(this.getClass().getSimpleName() + " executing around method:" + joinPoint.toLongString());
		}

		// fetch the request
		WebServiceTemplate adviceWebserviceTemplate = null;
		PartnerTransferObjectMarker adviceRequest = null;
		Class<? extends PartnerTransferObjectMarker> adviceRequestClass = null;
		PartnerTransferObjectMarker adviceResponse = null;
		final RequestResponseAuditData requestResponseAuditData = new RequestResponseAuditData();
		Method method = null;

		try {
			if (joinPoint.getArgs() != null && joinPoint.getArgs()[0] != null) {
				adviceWebserviceTemplate = (WebServiceTemplate) joinPoint.getArgs()[0];
			}
			if (joinPoint.getArgs().length >= 1 && joinPoint.getArgs()[1] != null) {
				adviceRequest = (PartnerTransferObjectMarker) joinPoint.getArgs()[1];
			}
			if (joinPoint.getArgs().length >= 2 && joinPoint.getArgs()[2] != null) {
				adviceRequestClass = (Class<? extends PartnerTransferObjectMarker>) joinPoint.getArgs()[2];
			}

			// confirm the objects got created
			Defense.notNull(adviceWebserviceTemplate,
					"RemoteServiceCallAspect aroundAdvice for standardRemoteServiceCallMethod received a null argument: WebServiceTemplate");
			Defense.notNull(adviceRequest,
					"RemoteServiceCallAspect aroundAdvice for standardRemoteServiceCallMethod received a null argument: serviceRequest");
			Defense.notNull(adviceRequestClass,
					"RemoteServiceCallAspect aroundAdvice for standardRemoteServiceCallMethod received a null argument: serviceRequestClass");

			final MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
			method = methodSignature.getMethod();
			requestResponseAuditData.setMethod(SOAP_METHOD);

			// accumulate the request
			requestResponseAuditData.setRequest(Arrays.asList(adviceRequest));

			// invoke the RemoteServiceCall.callRemoteService(...) method as the calling IMPL intended
			adviceResponse = (PartnerTransferObjectMarker) joinPoint.proceed();
		} catch (final Throwable throwable) {

			// log the exception
			LOGGER.error(this.getClass().getSimpleName() + " encountered "
					+ throwable.getClass().getName() + ": " + throwable.getMessage(),
					throwable);

			// accumulate the exception
			requestResponseAuditData.setResponse(throwable);
			// write the audit log
			writeAudit(MessageSeverity.ERROR, method, requestResponseAuditData);

			// and re-throw
			throw throwable;

		} finally {
			LOGGER.debug(this.getClass().getSimpleName() + " after method was called.");
		}

		if (adviceResponse == null) {
			adviceResponse = requestClass.newInstance();
		}
		// accumulate the response
		requestResponseAuditData.setResponse(adviceResponse);
		// write the audit log
		writeAudit(MessageSeverity.ERROR, method, requestResponseAuditData);

		return adviceResponse;
	}

	/**
	 * Perform with AuditLogger.
	 *
	 * @param severity
	 * @param method
	 * @param serviceTransferObject
	 */
	void writeAudit(final MessageSeverity severity, final Method method, final RequestResponseAuditData auditDataObject) {

		if (severity.equals(MessageSeverity.ERROR) || severity.equals(MessageSeverity.FATAL)) {

			getRequestResponseLogSerializer().asyncLogRequestResponseAspectAuditData(
					BaseRemoteServiceCallAspect.getDefaultAuditableInstance(method),
					auditDataObject,
					MessageSeverity.ERROR);

		} else {

			getRequestResponseLogSerializer().asyncLogRequestResponseAspectAuditData(
					BaseRemoteServiceCallAspect.getDefaultAuditableInstance(method),
					auditDataObject,
					MessageSeverity.INFO);

		}
	}
}
