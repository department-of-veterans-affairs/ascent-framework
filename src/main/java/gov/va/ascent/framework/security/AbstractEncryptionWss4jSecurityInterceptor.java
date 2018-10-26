package gov.va.ascent.framework.security;

import java.util.Properties;

import org.apache.ws.security.components.crypto.Crypto;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import gov.va.ascent.framework.security.crypto.CryptoProperties;

/**
 * An abstract implementation of spring's Wss4jSecurityInterceptor that adds
 * common encryption properties for encryption related implementations.
 */
public abstract class AbstractEncryptionWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {

	/** The crypto. */
	private Crypto crypto;

	/** The key alias. */
	private String keyAlias;

	/** The key password. */
	private String keyPassword;

	/** The crypto properties */
	private CryptoProperties cryptoProperties;

	/**
	 * Retrieves properties to set to create a crypto file
	 *
	 * @return
	 */
	protected Properties retrieveCryptoProps() {
		if (this.cryptoProperties == null) {
			this.cryptoProperties = new CryptoProperties();
		}
		return cryptoProperties.getCryptoProperties();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		//
	}

	/**
	 * Gets the crypto.
	 *
	 * @return the crypto
	 */
	public final Crypto getCrypto() {
		return crypto;
	}

	/**
	 * Sets the crypto.
	 *
	 * @param crypto the new crypto
	 */
	public final void setCrypto(final Crypto crypto) {
		this.crypto = crypto;
	}

	/**
	 * Gets the key alias.
	 *
	 * @return the key alias
	 */
	public final String getKeyAlias() {
		return keyAlias;
	}

	/**
	 * Sets the key alias.
	 *
	 * @param keyAlias the new key alias
	 */
	public final void setKeyAlias(final String keyAlias) {
		this.keyAlias = keyAlias;
	}

	/**
	 * Gets the key password.
	 *
	 * @return the key password
	 */
	public final String getKeyPassword() {
		return keyPassword;
	}

	/**
	 * Sets the key password.
	 *
	 * @param keyPassword the new key password
	 */
	public final void setKeyPassword(final String keyPassword) {
		this.keyPassword = keyPassword;
	}
}
