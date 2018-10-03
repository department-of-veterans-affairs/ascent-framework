package gov.va.ascent.framework.ws.client.remote;

import java.util.Arrays;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
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
 * This is a custom performance logging interceptor which simply wraps a method (any method) and calculates elapsed time.
 * 
 * This interceptor will create a org.apache.commons.logging.Log for the actual method intercepted and the execution time of the method
 * is logged at the info level if info level logging is enabled for that actual class.
 * 
 * This method takes a configurable 'warningThreshhold', the number of milliseconds until performance is considered a "warning."
 * If/when the 'warningThreshhold' is exceeded the performance will be logged as at a warning level.
 * 
 * @see org.aopalliance.intercept.MethodInterceptor
 * 
 * @author vanapalliv
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
		final Logger methodLog = LoggerFactory.getLogger(methodInvocation.getMethod().getDeclaringClass());
		final RequestResponseAuditData requestResponseAuditData = new RequestResponseAuditData();
		
		// only log entry at the debug level
		if (methodLog.isDebugEnabled()) {
			methodLog.debug("Entered: " + methodInvocation.getMethod().getDeclaringClass().getSimpleName()
					+ methodInvocation.getMethod().getName());
		}

		Object[] args = methodInvocation.getArguments();
		AuditEventData auditEventData = new AuditEventData(AuditEvents.PARTNER_REQUEST_RESPONSE, methodInvocation.getMethod().getName(),
				methodInvocation.getMethod().getDeclaringClass().getSimpleName());
	    LOGGER.info("Number of Arguments : " + args.length);
	    for(int i=0; i < args.length; i++) {
	    	LOGGER.info(args[i].toString());
	    }
		LOGGER.debug("Setting data to requestResponseAuditData");
	    requestResponseAuditData.setRequest(Arrays.asList(args));
		final Object retVal = methodInvocation.proceed();
		requestResponseAuditData.setResponse(retVal);
		LOGGER.debug("Invoking asyncLogRequestResponseAspectAuditData");
		asyncLogging.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData, MessageSeverity.INFO);
		return retVal;
	}


	
	
}
