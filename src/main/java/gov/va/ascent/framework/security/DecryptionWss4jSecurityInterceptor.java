package gov.va.ascent.framework.security;

import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;

public abstract class DecryptionWss4jSecurityInterceptor extends AbstractWss4jSecurityInterceptor {

	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(DecryptionWss4jSecurityInterceptor.class);

	public DecryptionWss4jSecurityInterceptor() {
		try {
			final Crypto crypto = CryptoFactory.getInstance(retrieveCryptoProps());
			setValidationDecryptionCrypto(crypto);
			setValidationSignatureCrypto(crypto);
		} catch (WSSecurityException e) {
			LOGGER.error("Error: Decryption Validation Crypto Factory Bean" + e.getMessage(), e);
		}

		this.setValidationActions(
				WSHandlerConstants.ENCRYPT + " " + WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE);
		final KeyStoreCallbackHandler keyStoreCallbackHandler = new KeyStoreCallbackHandler();
		keyStoreCallbackHandler.setPrivateKeyPassword(retrieveCryptoProps().getCryptoKeystorePw());
		this.setValidationCallbackHandler(keyStoreCallbackHandler);
	}
}
