package gov.va.ascent.framework.security;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import java.util.Properties;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
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
import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.config.BaseYamlConfig;



@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { BaseYamlConfig.class})
public class VAServiceSignatureWss4jSecurityInterceptor_UnitTest {

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

	@Mock
	VAServiceSignatureWss4jSecurityInterceptor interceptor = new VAServiceSignatureWss4jSecurityInterceptor();
	
	@Test
	public void testSecureMessage() throws Exception {
		
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		Properties props = retrieveCryptoProps();
		Crypto crypto = CryptoFactory.getInstance(props);
		crypto.setDefaultX509Identifier(securityCryptoMerlinKeystoreAlias);
        interceptor.setCrypto(crypto);
        interceptor.setKeyAlias(securityCryptoMerlinKeystoreAlias);
        interceptor.setValidationActions("Signature");
        interceptor.setValidateRequest(false);
        interceptor.setValidateResponse(false);
        interceptor.setSecurementUsername("selfsigned");
        interceptor.setSecurementPassword("password");
        interceptor.afterPropertiesSet();   
        MessageContext messageContextMock = mock(MessageContext.class);
     	interceptor.secureMessage(sm, messageContextMock);
		
		assertNotNull(sm);

	}
	
	@Test
	public void testSecureMessageNoCrypto() throws Exception {
		
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
        interceptor.setKeyAlias(securityCryptoMerlinKeystoreAlias);
        interceptor.setValidationActions("Signature");
        interceptor.setValidateRequest(false);
        interceptor.setValidateResponse(false);
        interceptor.setSecurementUsername("selfsigned");
        interceptor.setSecurementPassword("password");
        interceptor.afterPropertiesSet();   
        MessageContext messageContextMock = mock(MessageContext.class);
     	interceptor.secureMessage(sm, messageContextMock);
		
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
	
	
}
