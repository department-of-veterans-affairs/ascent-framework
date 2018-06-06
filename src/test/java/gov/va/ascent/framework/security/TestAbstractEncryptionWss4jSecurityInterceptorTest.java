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
		tester.setCryptoFile("test.file");

		assertNotNull(tester.getCrypto());
		assertTrue(StringUtils.isNotBlank(tester.getKeyAlias()));
		assertTrue(StringUtils.isNotBlank(tester.getKeyPassword()));
		assertTrue(StringUtils.isNotBlank(tester.getCryptoFile()));

	}
	
	@Test
	public final void testRetrieveCryptoProps() {
		
		// Spy AbstractEncryptionWss4jSecurityInterceptor and set the properties
		AbstractEncryptionWss4jSecurityInterceptor abstractEncryptionWss4jInterceptor = Mockito.spy(AbstractEncryptionWss4jSecurityInterceptor.class);
		ReflectionTestUtils.setField(abstractEncryptionWss4jInterceptor, "securityCryptoProvider", "org.apache.ws.security.components.crypto.Merlin");
		ReflectionTestUtils.setField(abstractEncryptionWss4jInterceptor, "securityCryptoMerlinKeystoreType", "jks");
		ReflectionTestUtils.setField(abstractEncryptionWss4jInterceptor, "securityCryptoMerlinKeystorePassword", "changeit");
		ReflectionTestUtils.setField(abstractEncryptionWss4jInterceptor, "securityCryptoMerlinKeystoreAlias", "ebn_vbms_cert");
		ReflectionTestUtils.setField(abstractEncryptionWss4jInterceptor, "securityCryptoMerlinKeystoreFile", "/encryption/EFolderService/vbmsKeystore.jks");
		
		Map<Object, Object> mapCrypto = abstractEncryptionWss4jInterceptor.retrieveCryptoProps();
		
		assertNotNull(mapCrypto);
		assertEquals("org.apache.ws.security.components.crypto.Merlin", mapCrypto.get("org.apache.ws.security.crypto.provider"));
		assertEquals("jks", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.type"));
		assertEquals("changeit", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.password"));
		assertEquals("ebn_vbms_cert", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.alias"));
		assertEquals("/encryption/EFolderService/vbmsKeystore.jks", mapCrypto.get("org.apache.ws.security.crypto.merlin.keystore.file"));
	}

}
