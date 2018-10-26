package gov.va.ascent.framework.security.crypto;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;

public class CryptoProperties {
	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(CryptoProperties.class);

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

	/**
	 * Retrieves properties to set to create a crypto file
	 *
	 * @return
	 */
	public Properties getCryptoProperties() {

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

}
