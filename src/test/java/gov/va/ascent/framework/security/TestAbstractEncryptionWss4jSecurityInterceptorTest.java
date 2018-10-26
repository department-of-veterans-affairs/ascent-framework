package gov.va.ascent.framework.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.MerlinDevice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import gov.va.ascent.framework.security.crypto.CryptoProperties;

public class TestAbstractEncryptionWss4jSecurityInterceptorTest {

	class Tester extends AbstractEncryptionWss4jSecurityInterceptor {
		public Tester() {
			super();
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
	public final void testSettersAndGetters() {
		final Crypto crypto = new MerlinDevice();

		tester.setCrypto(crypto);
		tester.setKeyAlias("test.alias");
		tester.setKeyPassword("test.password");

		assertNotNull(tester.getCrypto());
		assertTrue(StringUtils.isNotBlank(tester.getKeyAlias()));
		assertTrue(StringUtils.isNotBlank(tester.getKeyPassword()));
	}

	@Test
	public final void testRetrieveCryptoProps() {
		CryptoProperties cryptoProps = Mockito.spy(CryptoProperties.class);
		ReflectionTestUtils.setField(cryptoProps, "securityCryptoProvider", "org.apache.ws.security.components.crypto.Merlin");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreTypeOriginator", "SUN");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreType", "jks");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystorePassword", "changeit");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreAlias", "ebn_vbms_cert");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreFile", "/encryption/EFolderService/vbmsKeystore.jks");

		ReflectionTestUtils.setField(tester, "cryptoProperties", cryptoProps);

		Map<Object, Object> mapCrypto = tester.retrieveCryptoProps();

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
