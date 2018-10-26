package gov.va.ascent.framework.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.xml.sax.SAXException;

import gov.va.ascent.framework.security.crypto.CryptoProperties;

public class VAServiceEncryptionWss4jSecurityInterceptorTest {

	private static final String SOAP_MESSAGE_FILE = "src/test/resources/testFiles/security/soapMessage.xml";

	/** The security.crypto.merlin.keystore.alias */
	private String securityCryptoMerlinKeystoreAlias;

	private Properties propsCrypto;

	private VAServiceEncryptionWss4jSecurityInterceptor encryptionWss4jSecurityInterceptor =
			Mockito.spy(VAServiceEncryptionWss4jSecurityInterceptor.class);

	@Before
	public final void setUp() throws Exception {
		CryptoProperties cryptoProps = Mockito.spy(CryptoProperties.class);
		ReflectionTestUtils.setField(cryptoProps, "securityCryptoProvider", "org.apache.ws.security.components.crypto.Merlin");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreTypeOriginator", "SUN");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreType", "jks");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystorePassword", "changeit");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreAlias", "ebn_vbms_cert");
		ReflectionTestUtils.setField(cryptoProps, "cryptoKeystoreFile", "/encryption/EFolderService/vbmsKeystore.jks");

		ReflectionTestUtils.setField(encryptionWss4jSecurityInterceptor, "cryptoProperties", cryptoProps);

		propsCrypto = encryptionWss4jSecurityInterceptor.retrieveCryptoProps();

		securityCryptoMerlinKeystoreAlias = (String) propsCrypto.get("org.apache.ws.security.crypto.merlin.keystore.alias");
	}

	@After
	public final void tearDown() {

	}

	@Test
	public void testSecureMessage() throws WSSecurityException {

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			fail("VAServiceEncryptionWss4jSecurityInterceptor : testSecureMessage test method fail");
		}
		Crypto crypto = CryptoFactory.getInstance(propsCrypto);
		crypto.setDefaultX509Identifier(securityCryptoMerlinKeystoreAlias);
		encryptionWss4jSecurityInterceptor.setCrypto(crypto);
		encryptionWss4jSecurityInterceptor.setKeyAlias(securityCryptoMerlinKeystoreAlias);
		encryptionWss4jSecurityInterceptor.setValidationActions("Encrypt");
		encryptionWss4jSecurityInterceptor.setValidateRequest(false);
		encryptionWss4jSecurityInterceptor.setValidateResponse(false);
		encryptionWss4jSecurityInterceptor.setSecurementUsername("selfsigned");
		encryptionWss4jSecurityInterceptor.setSecurementPassword("password");
		encryptionWss4jSecurityInterceptor.afterPropertiesSet();
		MessageContext messageContextMock = mock(MessageContext.class);
		encryptionWss4jSecurityInterceptor.secureMessage(sm, messageContextMock);

		assertNotNull(sm);

	}

	@Test
	public void testSecureMessageNoCrypto() throws WSSecurityException {

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			fail("VAServiceEncryptionWss4jSecurityInterceptor : testSecureMessage test method fail");
		}
		encryptionWss4jSecurityInterceptor.setKeyAlias(securityCryptoMerlinKeystoreAlias);
		encryptionWss4jSecurityInterceptor.setValidationActions("Encrypt");
		encryptionWss4jSecurityInterceptor.setValidateRequest(false);
		encryptionWss4jSecurityInterceptor.setValidateResponse(false);
		encryptionWss4jSecurityInterceptor.setSecurementUsername("selfsigned");
		encryptionWss4jSecurityInterceptor.setSecurementPassword("password");
		encryptionWss4jSecurityInterceptor.afterPropertiesSet();
		MessageContext messageContextMock = mock(MessageContext.class);
		encryptionWss4jSecurityInterceptor.secureMessage(sm, messageContextMock);

		assertNotNull(sm);

	}

	@Test
	public void testValidateMessage() {

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}

		encryptionWss4jSecurityInterceptor.validateMessage(sm, null);
	}
}
