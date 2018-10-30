package gov.va.ascent.framework.security;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.util.WSSecurityUtil;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecuritySecurementException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Extension of the Wss4jSecurityInterceptor to inject the VA application headers into the WSS security element
 * 
 * <pre>
 * &lt;vaws:VaServiceHeaders xmlns:vaws="http://vbawebservices.vba.va.gov/vawss">
 * &lt;vaws:applicationName>VDC</vaws:applicationName> </vaws:VaServiceHeaders>
 * </pre>
 */
public class VAServiceWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {

	/** The namespace constant. */
	public static final String VA_NS = "http://vbawebservices.vba.va.gov/vawss";

	/** The prefix constant */
	public static final String VA_PREFIX = "vaws:";

	/** The headers node constant */
	public static final String VA_SERVICE_HEADERS = "VaServiceHeaders";

	/** The application name node constant */
	public static final String VA_APPLICATION_NAME = "applicationName";

	/** The Constant CLIENT_MACHINE. */
	public static final String CLIENT_MACHINE = "CLIENT_MACHINE";

	/** The Constant STN_ID. */
	public static final String STN_ID = "STN_ID";

	/** The Constant EXTERNAL_UID. */
	public static final String EXTERNAL_UID = "ExternalUid";

	/** The Constant EXTERNAL_KEY. */
	public static final String EXTERNAL_KEY = "ExternalKey";

	/** The Constant for when a userid cannot be obtained. */
	public static final String EXTERNAL_UID_DEFAULT = "EVSS";

	/** The va client machine. */
	private String clientMachine;

	/** The va station id. */
	private String stationId;

	/** The va application name. */
	private String vaApplicationName;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#secureMessage(
	 * org.springframework.ws.soap.SoapMessage, org.springframework.ws.context.MessageContext)
	 */
	@Override
	public final void secureMessage(final SoapMessage soapMessage, final MessageContext messageContext) {
		super.secureMessage(soapMessage, messageContext);

		// Build the VA Header Element
		final Document soapDoc = soapMessage.getDocument();
		final Element vaHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + VA_SERVICE_HEADERS);

		final Element clientMachineHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + CLIENT_MACHINE);
		clientMachineHeader.setTextContent(BEPWebServiceUtil.getClientMachine(clientMachine));
		vaHeader.appendChild(clientMachineHeader);

		final Element stationIdHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + STN_ID);
		stationIdHeader.setTextContent(stationId);
		vaHeader.appendChild(stationIdHeader);

		final Element appNameHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + VA_APPLICATION_NAME);
		appNameHeader.setTextContent(vaApplicationName);
		vaHeader.appendChild(appNameHeader);

		final Element externalUidHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + EXTERNAL_UID);
		final String userId = BEPWebServiceUtil.getExternalUID(EXTERNAL_UID_DEFAULT);
		externalUidHeader.setTextContent(userId);
		vaHeader.appendChild(externalUidHeader);

		final Element externalKeyHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + EXTERNAL_KEY);
		externalKeyHeader.setTextContent(userId); // RRR: should be changed BEPWebServiceUtil.getExternalKey(null)
		vaHeader.appendChild(externalKeyHeader);

		// Add the VA application id header
		Element secHeader;
		try {
			secHeader = WSSecurityUtil.findWsseSecurityHeaderBlock(soapDoc, soapDoc.getDocumentElement(), true);
			secHeader.appendChild(vaHeader);
			soapMessage.setDocument(soapDoc);
		} catch (final WSSecurityException ex) {
			throw new Wss4jSecuritySecurementException(ex.getMessage(), ex);
		}

	}

	/**
	 * Gets the client machine.
	 *
	 * @return the client machine
	 */
	public final String getClientMachine() {
		return clientMachine;
	}

	/**
	 * Sets the client machine.
	 *
	 * @param clientMachine the new client machine
	 */
	public final void setClientMachine(final String clientMachine) {
		this.clientMachine = clientMachine;
	}

	/**
	 * Gets the station id.
	 *
	 * @return the station id
	 */
	public final String getStationId() {
		return stationId;
	}

	/**
	 * Sets the station id.
	 *
	 * @param stationId the new station id
	 */
	public final void setStationId(final String stationId) {
		this.stationId = stationId;
	}

	/**
	 * Gets the va application name.
	 *
	 * @return the va application name
	 */
	public final String getVaApplicationName() {
		return vaApplicationName;
	}

	/**
	 * Sets the va application name.
	 *
	 * @param vaApplicationName the new va application name
	 */
	public final void setVaApplicationName(final String vaApplicationName) {
		this.vaApplicationName = vaApplicationName;
	}

}
