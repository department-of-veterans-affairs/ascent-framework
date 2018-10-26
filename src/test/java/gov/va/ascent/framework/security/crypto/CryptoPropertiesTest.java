package gov.va.ascent.framework.security.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class CryptoPropertiesTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetCryptoProperties() {
		/*
		 * propertiesMap.setProperty("org.apache.ws.security.crypto.provider", securityCryptoProvider);
		 * propertiesMap.setProperty("org.apache.ws.security.crypto.merlin.keystore.provider", cryptoKeystoreTypeOriginator);
		 * propertiesMap.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", cryptoKeystoreType);
		 * propertiesMap.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", cryptoKeystorePassword);
		 * propertiesMap.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", cryptoKeystoreAlias);
		 * propertiesMap.setProperty("org.apache.ws.security.crypto.merlin.keystore.file", cryptoKeystoreFile);
		 */
		// Spy AbstractEncryptionWss4jSecurityInterceptor and set the properties
		CryptoProperties cryptoProperties = Mockito.spy(CryptoProperties.class);
		ReflectionTestUtils.setField(cryptoProperties, "securityCryptoProvider", "org.apache.ws.security.components.crypto.Merlin");
		ReflectionTestUtils.setField(cryptoProperties, "cryptoKeystoreTypeOriginator", "SUN");
		ReflectionTestUtils.setField(cryptoProperties, "cryptoKeystoreType", "jks");
		ReflectionTestUtils.setField(cryptoProperties, "cryptoKeystorePassword", "changeit");
		ReflectionTestUtils.setField(cryptoProperties, "cryptoKeystoreAlias", "ebn_vbms_cert");
		ReflectionTestUtils.setField(cryptoProperties, "cryptoKeystoreFile", "/encryption/EFolderService/vbmsKeystore.jks");

		Map<Object, Object> mapCrypto = cryptoProperties.getCryptoProperties();

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
