package gov.va.ascent.framework.security;

import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecEncrypt;
import org.apache.ws.security.message.WSSecHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;

/**
 * A Wss4j2 Security Interceptor to encrypt secure message header and body.
 */
public class VAServiceEncryptionWss4jSecurityInterceptor extends AbstractEncryptionWss4jSecurityInterceptor {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(VAServiceEncryptionWss4jSecurityInterceptor.class);

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

			if (getCrypto() == null) {
				LOGGER.error("Initializing crypto properties..." + getCryptoFile() + "end");
				setCrypto(CryptoFactory.getInstance(getCryptoFile()));
			}

			LOGGER.debug("Encrypting outgoing message...");

			final WSSecEncrypt encrypt = new WSSecEncrypt();
			encrypt.setUserInfo(getKeyAlias());

			final Document doc = soapMessage.getDocument();
			final WSSecHeader secHeader = new WSSecHeader();
			secHeader.insertSecurityHeader(doc);
			encrypt.setDocument(doc);

			encrypt.build(doc, getCrypto(), secHeader);

			soapMessage.setDocument(doc);
		} catch (final WSSecurityException e) {
			LOGGER.error("failed to encrypt ", e);
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
