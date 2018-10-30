package gov.va.ascent.framework.ws.client.remote;

import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.io.IOException;
import java.text.MessageFormat;

import javax.xml.transform.Source;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ascent.framework.util.Defense;

/**
 * <p>
 * Provides abstract and concrete methods to simplify mocking of
 * {@link RemoteServiceCall} mock/simulator implementations.
 * </p>
 * <p>
 * Classes that are simulator profile implementations for mocking should extend
 * this class.
 * </p>
 *
 * @author aburkholder
 */
public abstract class AbstractRemoteServiceCallMock implements RemoteServiceCall {
	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(AbstractRemoteServiceCallMock.class);

	/** Constant for the filename template for mocked files */
	public static final String MOCK_FILENAME_TEMPLATE = "test/mocks/{0}.xml";

	/**
	 * <p>
	 * Implements the logic to extract a key value from the request object, that is
	 * used as the file name for the mocked XML response file. See
	 * {@link AbstractRemoteServiceCallMock#MOCK_FILENAME_TEMPLATE} for the
	 * replacement pattern used to identify a file.
	 * </p>
	 * <p>
	 * Implementation of this method must not return {@code null} or empty string
	 * (there is a Defense for this). Implementation could be as simple as:
	 * </p>
	 *
	 * <pre>
	 * {@code
	 * 	&#64;Override
	 * 	String getKeyForMockResponse(final AbstractTransferObject request) {
	 * 		Defense.notNull(request);
	 * 		// cast the request back to the original request transfer object type so we can get the state code
	 * 		final String stateAbbr = ((GetVAMedicalTreatmentFacilityList) request).getStateCd();
	 * 		Defense.hasText(stateAbbr);
	 * 		return stateAbbr;
	 * 	}
	 * }
	 * </pre>
	 *
	 * @param request
	 *            the request object received from the calling functional test or or
	 *            unit test.
	 * @return String the key to be used to retrieve the mock response XML
	 */
	protected abstract String getKeyForMockResponse(PartnerTransferObjectMarker request);

	/**
	 * <p>
	 * Execution of a mocked remote call to the web service identified by the
	 * WebServiceTemplate. There is a bean of the same name for
	 * PROFILE_REMOTE_CLIENT_IMPLS in the ClientConfig class.
	 * </p>
	 * <p>
	 * TESTING NOTES:<br/>
	 * Because this method uses mockito's MockWebServiceServer to mock the SOAP
	 * call to the partner, request & response expectations are set before the call,
	 * and then verified after the call.
	 * </p>
	 * <p>
	 * For some reason, this causes issues when running any unit test on this method.<br/>
	 * When executing test from "Run As Junit", the expectations set on the mock server disappear when
	 * marshallSendAndreceive is run, causing the mock servers ".verify()" to fail.<br/>
	 * Strangely, everything works fine when run under "mvn test".
	 * </p>
	 * <p>
	 * Synchronized block of code was added to address multi-threading errors seen during performance testing.<br/>
	 * Using WebServiceTemplate as an object to be locked.
	 * </p>
	 *
	 * @param webserviceTemplate
	 *            the template for the web service being called
	 * @param request
	 *            the request (a subclass of AbstractTransferObject)
	 * @param requestClass
	 *            the actual Class of the request object
	 * @return PartnerTransferObjectMarker the response from the remote web service (cast
	 *         it to the desired response type)
	 */
	protected PartnerTransferObjectMarker callMockService(final WebServiceTemplate webserviceTemplate,
			final PartnerTransferObjectMarker request,
			final Class<? extends PartnerTransferObjectMarker> requestClass) {

		Defense.notNull(webserviceTemplate, "To callMockService, the WebServiceTemplate cannot be null.");
		Defense.notNull(request, "To callMockService, the transfer object 'request' cannot be null.");
		Defense.notNull(requestClass, "To callMockService, the 'requestClass' of the request transfer object cannot be null.");

		PartnerTransferObjectMarker response = null;

		try {
			final Source requestPayload =
					marshalMockRequest((Jaxb2Marshaller) webserviceTemplate.getMarshaller(), request, requestClass);
			final Source responsePayload = readMockResponseByKey(request);

			synchronized (webserviceTemplate) {

				final MockWebServiceServer mockSoapServer = MockWebServiceServer.createServer(webserviceTemplate);

				mockSoapServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

				response = (PartnerTransferObjectMarker) webserviceTemplate.marshalSendAndReceive(requestClass.cast(request));

				mockSoapServer.verify();
			}
		} catch (Exception e) {
			LOGGER.error("MOCK service call failed with requestClass "
					+ requestClass.getName() + " and request object " + ReflectionToStringBuilder.toString(request), e);
			throw e;
		}

		return response;
	}

	/**
	 * Mock helper for functional tests. Marshals an AbstractTransferObject request
	 * object to a StringSource formatted as XML.
	 *
	 * @param marshaller
	 *            a JAXB2 marshaler
	 * @param request
	 *            the request transfer object generated by xjc
	 * @param requestClass
	 *            the response transfer object generated by xjc
	 * @return StringSource the xml
	 */
	private StringSource marshalMockRequest(final Jaxb2Marshaller marshaller, final PartnerTransferObjectMarker request,
			final Class<?> requestClass) {
		final StringResult result = new StringResult();
		marshaller.marshal(requestClass.cast(request), result);
		return new StringSource(result.toString());
	}

	/**
	 * Mock helper for functional tests. Based on the key provided by implementing
	 * code in the abstract
	 * {@code getKeyForMockResponse(AbstractTransferObject request)} method.
	 *
	 * @param request
	 * @param requestClass
	 * @return
	 * @throws IOException
	 */
	private ResourceSource readMockResponseByKey(final PartnerTransferObjectMarker request) {
		// get the key from the calling class
		final String key = getKeyForMockResponse(request);

		Defense.hasText(key);

		// read the resource
		ResourceSource resource = null;
		try {
			resource = new ResourceSource(new ClassPathResource(MessageFormat.format(MOCK_FILENAME_TEMPLATE, key)));
		} catch (final IOException e) {
			throw new AscentRuntimeException("Could not read mock XML file '" + MessageFormat.format(MOCK_FILENAME_TEMPLATE, key)
					+ "' using key '" + key + "'. Please make sure this response file exists in the main/resources directory.", e);
		}
		return resource;
	}

}
