package gov.va.ascent.framework.log;

import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

import gov.va.ascent.framework.audit.AuditEvents;

public class LogHttpCallInterceptor implements ClientInterceptor {

	public static final String CLIENT_REQUEST_MESSAGE_TEXT = "Client Request Message : ";
	public static final String CLIENT_REPONSE_MESSAGE_TEXT = "Client Response Message : ";
	private static final String HANDLE_REQUEST_ACTIVITY_NAME = "handleRequest";
	private static final String HANDLE_REPONSE_ACTIVITY_NAME = "handleResponse";

	@Override
	public boolean handleRequest(final MessageContext messageContext) {
		HttpLoggingUtils.logMessage(CLIENT_REQUEST_MESSAGE_TEXT, messageContext.getRequest(), HANDLE_REQUEST_ACTIVITY_NAME,
				this.getClass().getName(), AuditEvents.PARTNER_SOAP_REQUEST);
		return true;
	}

	@Override
	public boolean handleResponse(final MessageContext messageContext) {
		HttpLoggingUtils.logMessage(CLIENT_REPONSE_MESSAGE_TEXT, messageContext.getResponse(), HANDLE_REPONSE_ACTIVITY_NAME,
				this.getClass().getName(), AuditEvents.PARTNER_SOAP_RESPONSE);
		return true;
	}

	@Override
	public boolean handleFault(final MessageContext messageContext) {
		return true;
	}

	@Override
	public void afterCompletion(final MessageContext messageContext, final Exception ex) {
		// nothing to do after completion
	}

}
