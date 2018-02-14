package gov.va.ascent.framework.ws.client.remote;

import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ascent.framework.transfer.AbstractTransferObject;

/**
 * Interface for executing remote service calls.
 * The implementation may be real, or may mock the call for testing. Mocking implementations of this interface should extend {@link AbstractRemoteServiceCallMock}.
 */
@FunctionalInterface
public interface RemoteServiceCall {

	/** The spring bean name for any implementations. */
	static final String BEAN_NAME = "remoteServiceCall";

	/**
	 * Execution of a real or mocked remote call to the web service identified by the WebServiceTemplate.
	 * @param webserviceTemplate the template for the web service being called
	 * @param request the request (a subclass of AbstractTransferObject)
	 * @param requestClass the actual Class of the request object
	 * @return AbstractTransferObject the response from the remote web service (cast it to the desired response type)
	 */
	AbstractTransferObject callRemoteService(final WebServiceTemplate webserviceTemplate, final AbstractTransferObject request, final Class<? extends AbstractTransferObject> requestClass);

}
