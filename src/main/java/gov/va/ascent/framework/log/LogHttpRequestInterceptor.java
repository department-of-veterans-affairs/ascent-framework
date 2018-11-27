package gov.va.ascent.framework.log;

import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

public class LogHttpRequestInterceptor implements ClientInterceptor {

	public static final String CLIENT_REQUEST_MESSAGE_TEXT = "Client Request Message : ";
	private static final String HANDLE_REQUEST_ACTIVITY_NAME = "handleRequest";

	@Override
	public boolean handleRequest(final MessageContext messageContext) {
		HttpLoggingUtils.logMessage(CLIENT_REQUEST_MESSAGE_TEXT, messageContext.getRequest(), HANDLE_REQUEST_ACTIVITY_NAME,
				this.getClass().getName());
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
		// nothing to do after completion
	}

}
