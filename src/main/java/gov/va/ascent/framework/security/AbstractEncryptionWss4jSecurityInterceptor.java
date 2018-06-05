package gov.va.ascent.framework.security;

import java.util.HashMap;
import java.util.Map;

import org.apache.ws.security.components.crypto.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

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

	/** The crypto file. */
	private String cryptoFile;
	
	/**
	 * The security crypto provider
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.provider}")
	private String securityCryptoProvider;

	/**
	 * The security.crypto.merlin.keystore.type
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.merlin.keystore.type}")
	private String securityCryptoMerlinKeystoreType;

	/**
	 * The security.crypto.merlin.keystore.password.
	 */
	@Value("${partner.client.keystorePassword}")
	private String securityCryptoMerlinKeystorePassword;

	/**
	 * The security.crypto.merlin.keystore.alias
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.merlin.keystore.alias}")
	private String securityCryptoMerlinKeystoreAlias;

	/**
	 * The securityCryptoMerlinKeystoreFile
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.merlin.keystore.file}")
	private String securityCryptoMerlinKeystoreFile;

	/**
	 * Retrieves properties to set to create a crypto file
	 * 
	 * @return
	 */
	protected Map<Object, Object> retrieveCryptoProps() {
		final Map<Object, Object> propsMap = new HashMap<>();
		propsMap.put("org.apache.ws.security.crypto.provider", securityCryptoProvider);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.type", securityCryptoMerlinKeystoreType);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.password", securityCryptoMerlinKeystorePassword);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.alias", securityCryptoMerlinKeystoreAlias);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.file", securityCryptoMerlinKeystoreFile);
		return propsMap;
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

	/**
	 * Gets the crypto file.
	 *
	 * @return the crypto file
	 */
	public final String getCryptoFile() {
		return cryptoFile;
	}

	/**
	 * Sets the crypto file.
	 *
	 * @param cryptoFile the new crypto file
	 */
	public final void setCryptoFile(final String cryptoFile) {
		this.cryptoFile = cryptoFile;
	}
}
