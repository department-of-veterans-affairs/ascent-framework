package gov.va.ascent.framework.security;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecTimestamp;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.w3c.dom.Document;

import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;

/**
 * A Wss4j2 Security Interceptor to add a timestamp and time-to-live to a soap message.
 */
public class VAServiceTimeStampWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {

	/** The Constant LOGGER. */
	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(VAServiceTimeStampWss4jSecurityInterceptor.class);

	/** The time stamp. */
	private String timeStampTtl;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#secureMessage(org.springframework.ws.soap.SoapMessage,
	 * org.springframework.ws.context.MessageContext)
	 */
	@Override
	protected final void secureMessage(final SoapMessage soapMessage, final MessageContext messageContext) {

		super.secureMessage(soapMessage, messageContext);

		try {
			LOGGER.debug("Header timestamp TTL {} " + getTimeStampTtl());
			final Document doc = soapMessage.getDocument();
			final WSSecHeader secHeader = new WSSecHeader();
			secHeader.insertSecurityHeader(doc);
			final WSSecTimestamp timestamp = new WSSecTimestamp();
			timestamp.setTimeToLive(Integer.valueOf(getTimeStampTtl()));

			timestamp.build(doc, secHeader);

			soapMessage.setDocument(doc);

		} catch (final WSSecurityException e) {
			LOGGER.error("failed encryption ", e);
			throw new AscentRuntimeException(e);
		}

	}

	/**
	 * Gets the time stamp time to live. If not {@code null}, will be valid for Integer.valueOf(String) conversion.
	 *
	 * @return the time stamp ttl
	 */
	public final String getTimeStampTtl() {
		return this.timeStampTtl;
	}

	/**
	 * Sets the time stamp time to live. Must be valid for Integer.valueOf(String) conversion.
	 *
	 * @param timeStampTtl the new time stamp ttl
	 */
	public final void setTimeStampTtl(final String timeStampTtl) {
		this.timeStampTtl = timeStampTtl;
	}
}
