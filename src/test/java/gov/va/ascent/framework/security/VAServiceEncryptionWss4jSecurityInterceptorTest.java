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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.xml.sax.SAXException;

import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.config.BaseYamlConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { BaseYamlConfig.class})
public class VAServiceEncryptionWss4jSecurityInterceptorTest {
	
	private static final String SOAP_MESSAGE_FILE = "src/test/resources/testFiles/security/soapMessage.xml";
	
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
	@Value("${ascent-framework.org.apache.ws.security.crypto.merlin.keystore.password}")
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

	@Mock
	VAServiceEncryptionWss4jSecurityInterceptor encryptionWss4jSecurityInterceptor = new VAServiceEncryptionWss4jSecurityInterceptor();
	
	@Before
	public final void setUp() throws Exception {
	}

	@After
	public final void tearDown() {

	}

	@Test
	public void testSecureMessage() throws WSSecurityException  { 

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			fail("VAServiceEncryptionWss4jSecurityInterceptor : testSecureMessage test method fail");
		}
		Properties props = retrieveCryptoProps();
		Crypto crypto = CryptoFactory.getInstance(props);
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
	public void testSecureMessageNoCrypto() throws WSSecurityException  { 

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
	
	/**
	 * Retrieves properties to set to create a crypto file
	 * @return
	 */
	private Properties retrieveCryptoProps() {
		Properties props = new Properties();
		props.put("org.apache.ws.security.crypto.provider", securityCryptoProvider);
		props.put("org.apache.ws.security.crypto.merlin.keystore.type", securityCryptoMerlinKeystoreType);
		props.put("org.apache.ws.security.crypto.merlin.keystore.password", securityCryptoMerlinKeystorePassword);
		props.put("org.apache.ws.security.crypto.merlin.keystore.alias", securityCryptoMerlinKeystoreAlias);
		props.put("org.apache.ws.security.crypto.merlin.keystore.file", securityCryptoMerlinKeystoreFile);
		return props;
	}

	@Test
	public void testValidateMessage() {

		final VAServiceEncryptionWss4jSecurityInterceptor encryptionWss4jSecurityInterceptor =
				new VAServiceEncryptionWss4jSecurityInterceptor();

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
