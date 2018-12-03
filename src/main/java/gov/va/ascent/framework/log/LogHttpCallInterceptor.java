package gov.va.ascent.framework.log;

import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

import gov.va.ascent.framework.audit.AuditEvents;

public class LogHttpCallInterceptor implements ClientInterceptor {

	private static final String HANDLE_REQUEST_ACTIVITY_NAME = "handleRequest";
	private static final String HANDLE_REPONSE_ACTIVITY_NAME = "handleResponse";

	private String title = "";

	public LogHttpCallInterceptor(final String title) {
		super();
		this.title = title;
	}

	@Override
	public boolean handleRequest(final MessageContext messageContext) {
		return true;
	}

	@Override
	public boolean handleResponse(final MessageContext messageContext) {
		return true;
	}

	@Override
	public boolean handleFault(final MessageContext messageContext) {
		return true;
	}

	@Override
	public void afterCompletion(final MessageContext messageContext, final Exception ex) {
		HttpLoggingUtils.logMessage(title, messageContext.getRequest(), HANDLE_REQUEST_ACTIVITY_NAME, this.getClass().getName(),
				AuditEvents.PARTNER_SOAP_REQUEST);
		HttpLoggingUtils.logMessage(title, messageContext.getResponse(), HANDLE_REPONSE_ACTIVITY_NAME, this.getClass().getName(),
				AuditEvents.PARTNER_SOAP_RESPONSE);
	}

}
