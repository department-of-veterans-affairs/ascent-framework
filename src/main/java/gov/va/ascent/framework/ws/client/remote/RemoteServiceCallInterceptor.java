package gov.va.ascent.framework.ws.client.remote;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.ascent.framework.audit.AuditEventData;
import gov.va.ascent.framework.audit.AuditEvents;
import gov.va.ascent.framework.audit.RequestResponseAuditData;
import gov.va.ascent.framework.audit.RequestResponseLogSerializer;
import gov.va.ascent.framework.messages.MessageSeverity;

/**
 * Audit log partner request/response data
 * 
 * @see org.aopalliance.intercept.MethodInterceptor
 */
@Component
public class RemoteServiceCallInterceptor implements MethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteServiceCallInterceptor.class);

	@Autowired
	RequestResponseLogSerializer asyncLogging;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	// CHECKSTYLE:OFF
	public final Object invoke(final MethodInvocation methodInvocation) throws Throwable {
		// CHECKSTYLE:ON

		String paramtypesToLog = "";
		for (Class<?> c : methodInvocation.getMethod().getParameterTypes()) {
			paramtypesToLog += c.getName() + ", ";
		}
		paramtypesToLog = StringUtils.removeEnd(paramtypesToLog, ", ");
		LOGGER.info("Intercepted: " + methodInvocation.getMethod().getDeclaringClass().getName()
				+ "." + methodInvocation.getMethod().getName()
				+ "(" + paramtypesToLog + ")");

		Object[] args = methodInvocation.getArguments();
		LOGGER.info("Number of Arguments : " + args.length + "; values: " + ReflectionToStringBuilder.toString(args));

		final RequestResponseAuditData requestResponseAuditData = new RequestResponseAuditData();
		requestResponseAuditData.setRequest(Arrays.asList(args));

		AuditEventData auditEventData =
				new AuditEventData(AuditEvents.PARTNER_REQUEST_RESPONSE, methodInvocation.getMethod().getName(),
						methodInvocation.getMethod().getDeclaringClass().getSimpleName());

		Object retVal = "";
		try {
			retVal = methodInvocation.proceed();
		} catch (Exception e) {
			/**
			 * Catch; Audit Log and Rethrow exception
			 */
			String errMsg = e.getMessage() != null ? e.getMessage() : " null.";
			LOGGER.error("Partner error: methodName {} " + methodInvocation.getMethod() + "; method args {} "
					+ ReflectionToStringBuilder.toString(methodInvocation.getArguments()) + ".\n Message: " + errMsg, e);

			asyncLogging.asyncLogMessageAspectAuditData(auditEventData, errMsg, MessageSeverity.ERROR);
			throw e;
		}

		requestResponseAuditData.setResponse(retVal);

		LOGGER.debug("Invoking asyncLogRequestResponseAspectAuditData");
		asyncLogging.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData, MessageSeverity.INFO);
		return retVal;
	}

}
