package gov.va.ascent.framework.security;

import java.util.Properties;

import org.apache.ws.security.components.crypto.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;

/**
 * An abstract implementation of spring's Wss4jSecurityInterceptor that adds
 * common encryption properties for encryption related implementations.
 */
public abstract class BaseEncryptionWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {
	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(BaseEncryptionWss4jSecurityInterceptor.class);

	/** For logging, bullet-like dash */
	private static final String DASH = "- ";
	/** For logging, variable replacement brackets */
	private static final String REPL = " {} ";
	/** For logging, end of line for multi-line log statement */
	private static final String EOL = " ; " + System.lineSeparator();

	/** The property name whose value would be the crypto provider */
	private static final String APACHE_PROVIDER = "org.apache.ws.security.crypto.provider";
	/** The property name whose value would be the keystore type originator (provider of this type of keystore) */
	private static final String APACHE_KS_PROVIDER = "org.apache.ws.security.crypto.merlin.keystore.provider";
	/** The property name whose value would be the keystore type */
	private static final String APACHE_KS_TYPE = "org.apache.ws.security.crypto.merlin.keystore.type";
	/** The property name whose value would be the keystore password */
	private static final String APACHE_KS_PW = "org.apache.ws.security.crypto.merlin.keystore.password";
	/** The property name whose value would be the alias of the desired certificate in the keystore */
	private static final String APACHE_KS_ALIAS = "org.apache.ws.security.crypto.merlin.keystore.alias";
	/** The property name whose value would be the path to the keystore file */
	private static final String APACHE_KS_FILE = "org.apache.ws.security.crypto.merlin.keystore.file";

	/** Fully qualified name of the class that will provide cryptography capabilities. */
	@Value("${ascent.security.crypto.provider}")
	private String securityCryptoProvider;

	/** The short name of the originator (or provider) of the keystore file (e.g. SUN) */
	@Value("${ascent.security.crypto.keystore.originator}")
	private String cryptoKeystoreTypeOriginator;

	/** The type of keystore file that contains the crypto certificates */
	@Value("${ascent.security.crypto.keystore.type}")
	private String cryptoKeystoreType;

	/** The password for the crypto keystore */
	@Value("${ascent.security.crypto.keystore.password}")
	private String cryptoKeystorePassword;

	/** The alias for the crypto certificate to be used */
	@Value("${ascent.security.crypto.keystore.alias}")
	private String cryptoKeystoreAlias;

	/** The crypto keystore file to use */
	@Value("${ascent.security.crypto.keystore.keystore}")
	private String cryptoKeystoreFile;

	/** The crypto. */
	private Crypto crypto;

	/** The key alias. */
	private String keyAlias;

	/** The key password. */
	private String keyPassword;

	/**
	 * Retrieves properties to set to create a crypto file
	 *
	 * @return
	 */
	public Properties retrieveCryptoProps() {

		LOGGER.info("Retrieving crypto properties:\n"
				+ DASH + APACHE_PROVIDER + REPL + securityCryptoProvider + EOL
				+ DASH + APACHE_KS_PROVIDER + REPL + cryptoKeystoreTypeOriginator + EOL
				+ DASH + APACHE_KS_TYPE + REPL + cryptoKeystoreType + EOL
				+ DASH + APACHE_KS_PW + REPL + cryptoKeystorePassword + EOL
				+ DASH + APACHE_KS_ALIAS + REPL + cryptoKeystoreAlias + EOL
				+ DASH + APACHE_KS_FILE + REPL + cryptoKeystoreFile + " .");

		Properties props = new Properties();
		props.setProperty(APACHE_PROVIDER, securityCryptoProvider);
		props.setProperty(APACHE_KS_PROVIDER, cryptoKeystoreTypeOriginator);
		props.setProperty(APACHE_KS_TYPE, cryptoKeystoreType);
		props.setProperty(APACHE_KS_PW, cryptoKeystorePassword);
		props.setProperty(APACHE_KS_ALIAS, cryptoKeystoreAlias);
		props.setProperty(APACHE_KS_FILE, cryptoKeystoreFile);

		return props;
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
