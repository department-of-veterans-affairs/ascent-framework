package gov.va.ascent.framework.security;

import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecEncrypt;
import org.apache.ws.security.message.WSSecHeader;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;

import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;

/**
 * A Wss4j2 Security Interceptor to encrypt secure message header and body.
 * <p>
 * Instantiation of this class requires a {@link #retrieveCryptoProps()} method
 * that returns a {@link CryptoProperties} implementation, as declared in
 * {@link AbstractWss4jSecurityInterceptor#retrieveCryptoProps()}.
 * <p>
 * A complete example can be found in the spring beans of vetservices-partner-efolder EFolderWsClientConfig.java.
 * <p>
 * <b>NOTE:</b> VBMS uses the same cert for ssl AND signing AND key time stamp AND decryption.
 * If future implementations require separate certificates, this code - and possibly the {@link CryptoProperties}
 * interface and certainly its implementations - will need to be modified to provide the additional alias(es).
 * <p>
 * Calling code would typically provide the method in-line during construction, for example:
 *
 * <pre>
 * new VAServiceEncryptionWss4jSecurityInterceptor() {
 * &#64;Override
 * public CryptoProperties retrieveCryptoProps() {
 * 	return new VAServiceEncryptionWss4jSecurityInterceptor interceptor = cryptoProps.retrieveCryptoProperties();
 * }
 * };
 */
public abstract class VAServiceEncryptionWss4jSecurityInterceptor extends AbstractWss4jSecurityInterceptor {

	/** The Constant LOGGER. */
	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(VAServiceEncryptionWss4jSecurityInterceptor.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#secureMessage(org.springframework.ws.soap .SoapMessage,
	 * org.springframework.ws.context.MessageContext)
	 */
	@Override
	protected final void secureMessage(final SoapMessage soapMessage, final MessageContext messageContext) {

		super.secureMessage(soapMessage, messageContext);

		WSSConfig.init();
		try {

			CryptoProperties props = retrieveCryptoProps();

			LOGGER.debug("Encrypting outgoing message...");

			final WSSecEncrypt encrypt = new WSSecEncrypt();
			encrypt.setUserInfo(props.getCryptoEncryptionAlias());

			final Document doc = soapMessage.getDocument();
			final WSSecHeader secHeader = new WSSecHeader();
			secHeader.insertSecurityHeader(doc);
			encrypt.setDocument(doc);

			encrypt.build(doc, CryptoFactory.getInstance(props), secHeader);

			soapMessage.setDocument(doc);
		} catch (final WSSecurityException e) {
			LOGGER.error("failed to encrypt ", e);
			throw new AscentRuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#validateMessage(org.springframework.ws.soap.SoapMessage,
	 * org.springframework.ws.context.MessageContext)
	 */
	@Override
	protected final void validateMessage(final SoapMessage soapMessage, final MessageContext messageContext) {
		super.validateMessage(soapMessage, messageContext);
	}
}
