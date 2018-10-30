package gov.va.ascent.framework.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractWss4jSecurityInterceptorTest {

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
	/** The property name whose value would be time-stamp of the TTL (time to live) */
	private static final String TIMESTAMP_TTL = "vetservices-partner-efolder.ws.client.security.timestamp.ttl";

	class Tester extends AbstractWss4jSecurityInterceptor {
		public Tester() {
			super();
		}

		@Override
		public CryptoProperties retrieveCryptoProps() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private Tester tester;

	@Before
	public void setUp() throws Exception {
		tester = new Tester();
	}

	@Test
	public final void testAfterPropertiesSet() {
		try {
			tester.afterPropertiesSet();
			assertTrue(true);
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}
	}

	@Test
	public final void testRetrieveCryptoProps() {

		/*
		 * CryptoProperties cryptoProps = Mockito.spy(CryptoProperties.class);
		 * ReflectionTestUtils.setField(cryptoProps, "securityCryptoProvider", "org.apache.ws.security.components.crypto.Merlin");
		 * ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreTypeOriginator", "SUN");
		 * ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreType", "jks");
		 * ReflectionTestUtils.setField(cryptoProps, "cryptoKeystorePassword", "changeit");
		 * ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreAlias", "ebn_vbms_cert");
		 * ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreFile", "/encryption/EFolderService/vbmsKeystore.jks");
		 *
		 * ReflectionTestUtils.setField(tester, "cryptoProperties", cryptoProps);
		 *
		 * Map<Object, Object> mapCrypto = tester.retrieveCryptoProps();
		 *
		 * assertNotNull(mapCrypto);
		 * assertEquals("org.apache.ws.security.components.crypto.Merlin", mapCrypto.get("org.apache.ws.security.crypto.provider"));
		 * assertEquals("SUN", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.provider"));
		 * assertEquals("jks", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.type"));
		 * assertEquals("changeit", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.password"));
		 * assertEquals("ebn_vbms_cert", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.alias"));
		 * assertEquals("/encryption/EFolderService/vbmsKeystore.jks",
		 * mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.file"));
		 */
		// Spy BaseEncryptionWss4jSecurityInterceptor and set the properties
		AbstractWss4jSecurityInterceptor interceptor = Mockito.spy(AbstractWss4jSecurityInterceptor.class);
		CryptoProperties props = new CryptoProperties() {

			private static final long serialVersionUID = 4980226916621404426L;

			@Override
			public String getCryptoEncryptionAlias() {
				return this.getProperty(APACHE_KS_ALIAS);
			}

			@Override
			public String getCryptoDefaultAlias() {
				return this.getProperty(APACHE_KS_ALIAS);
			}

			@Override
			public String getCryptoKeystoreFile() {
				return this.getProperty(APACHE_KS_FILE);
			}

			@Override
			public String getCryptoKeystorePw() {
				return this.getProperty(APACHE_KS_PW);
			}

			@Override
			public String getTimeStampTtl() {
				return this.getProperty(TIMESTAMP_TTL);
			}

		};
		props.setProperty(APACHE_PROVIDER, "org.apache.ws.security.components.crypto.Merlin");
		props.setProperty(APACHE_KS_PROVIDER, "SUN");
		props.setProperty(APACHE_KS_TYPE, "jks");
		props.setProperty(APACHE_KS_PW, "changeit");
		props.setProperty(APACHE_KS_ALIAS, "ebn_vbms_cert");
		props.setProperty(APACHE_KS_FILE, "/encryption/EFolderService/vbmsKeystore.jks");
		props.setProperty(TIMESTAMP_TTL, "300");

		when(interceptor.retrieveCryptoProps()).thenReturn(props);
		Map<Object, Object> mapCrypto = interceptor.retrieveCryptoProps();

		assertNotNull(mapCrypto);
		assertEquals("org.apache.ws.security.components.crypto.Merlin", mapCrypto.get("org.apache.ws.security.crypto.provider"));
		assertEquals("SUN", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.provider"));
		assertEquals("jks", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.type"));
		assertEquals("changeit", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.password"));
		assertEquals("ebn_vbms_cert", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.alias"));
		assertEquals("/encryption/EFolderService/vbmsKeystore.jks",
				mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.file"));
	}

}
