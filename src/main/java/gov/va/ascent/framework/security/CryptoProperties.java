package gov.va.ascent.framework.security;

import java.util.Properties;

public abstract class CryptoProperties extends Properties {

	private static final long serialVersionUID = 7915711218403848542L;

	/**
	 * The alias name for the encryption certificate.
	 *
	 * @return String the alias for encryption
	 */
	public abstract String getCryptoEncryptionAlias();

	/**
	 * The alias name for the default certificate used for signing and decryption.
	 *
	 * @return String the default alias
	 */
	public abstract String getCryptoDefaultAlias();

	/**
	 * The path and filename where the keystore file is located.
	 *
	 * @return String the path to the keystore file
	 */
	public abstract String getCryptoKeystoreFile();

	/**
	 * The keystore pw.
	 *
	 * @return String the keystore pw
	 */
	public abstract String getCryptoKeystorePw();

	/**
	 * The Time Stamp TTL.
	 *
	 * @return String the Time Stamp TTL.
	 */
	public abstract String getTimeStampTtl();

}