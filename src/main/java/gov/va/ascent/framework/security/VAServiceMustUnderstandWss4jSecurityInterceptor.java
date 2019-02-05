package gov.va.ascent.framework.security;

import java.util.Arrays;
import java.util.List;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.message.WSSecHeader;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.log.HttpLoggingUtils;

/**
 * A Wss4j2 Security Interceptor to remove "mustUnderstand" attributes from the envelope namespaces in the message header.
 */
public class VAServiceMustUnderstandWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {

	/** The Constant LOGGER. */
	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(VAServiceMustUnderstandWss4jSecurityInterceptor.class);

	/** The Constant MUST_UNDERSTAND_ATTR. */
	private static final String MUST_UNDERSTAND_ATTR = "mustUnderstand";

	/** The Constant SOAP_NS_LIST. */
	private static final List<String> SOAP_NS_LIST = Arrays.asList(new String[] {
			javax.xml.soap.SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, javax.xml.soap.SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE });

	/**
	 * Create the interceptor that sets the "Must Understand" header value.
	 * Validation actions are turned off.
	 */
	public VAServiceMustUnderstandWss4jSecurityInterceptor() {
		setValidationActions("NoSecurity");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#secureMessage(org.springframework.ws.soap.SoapMessage,
	 * org.springframework.ws.context.MessageContext)
	 */
	@Override
	protected final void secureMessage(final SoapMessage soapMessage, final MessageContext messageContext) {

		super.secureMessage(soapMessage, messageContext);
		final Document doc = soapMessage.getDocument();
		final WSSecHeader secHeader = new WSSecHeader();

		try {
			if (!secHeader.isEmpty(doc)) {
				final Element header = secHeader.getSecurityHeader();
				removeAttributeWithSOAPNS(header, MUST_UNDERSTAND_ATTR);
			}
		} catch (final WSSecurityException e) { // NOSONAR no action to take
			LOGGER.error(e.getMessage()); // NOSONAR no action to take
			throw new AscentRuntimeException(e);
		}

		soapMessage.setDocument(doc);

		HttpLoggingUtils.logMessage("SOAP message in VAServiceMustUnderstandWss4jSecurityInterceptor", soapMessage);

	}

	/**
	 * Removes the attribute with soapns.
	 *
	 * @param target the target
	 * @param attrLocalName the attr local name
	 * @return true, if successful
	 */
	boolean removeAttributeWithSOAPNS(final Element target, final String attrLocalName)
			throws WSSecurityException { // NOSONAR throws is to assist unit testing and coverage
		boolean retVal = false;

		if (target != null) {
			for (final String namespace : SOAP_NS_LIST) {
				if (target.hasAttributeNS(namespace, attrLocalName)) {
					target.removeAttributeNS(namespace, attrLocalName);
					retVal = true;
					break;
				}
			}
		}

		return retVal;
	}
}
