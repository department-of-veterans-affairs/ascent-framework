package gov.va.ascent.framework.security;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoBase;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.components.crypto.Merlin;
import org.junit.Assert;
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
import org.springframework.ws.soap.SoapMessage;
import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.config.BaseYamlConfig;



@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { BaseYamlConfig.class})
public class VAServiceSignatureWss4jSecurityInterceptor_UnitTest {


	private static final String PROPERTIES_FILE = "apache-trusted-crypto.properties";
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
	VAServiceSignatureWss4jSecurityInterceptor interceptor = new VAServiceSignatureWss4jSecurityInterceptor();
	
	@Test
	public void testSecureMessage() throws Exception {
		
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		Map<Object, Object> propsMap = retrieveCryptoProps();
		Crypto crypto = CryptoFactory.getInstance(Merlin.class, propsMap);
        interceptor.setCrypto(crypto);
   
        interceptor.setSecurementActions("Timestamp SAMLTokenSigned");
        //interceptor.setSecurementSignatureCrypto(crypto);
        interceptor.setSecurementUsername("selfsigned");
        interceptor.setSecurementPassword("password");
        interceptor.afterPropertiesSet();   
        
		interceptor.secureMessage(sm, null);
		
		Assert.assertTrue(sm.getSoapHeader()
				.examineHeaderElements(new QName("Security")).hasNext());

	}
	

	/**
	 * Retrieves properties to set to create a crypto file
	 * @return
	 */
	private Map<Object, Object> retrieveCryptoProps() {
		Map<Object, Object> propsMap = new HashMap<Object, Object>();
		propsMap.put("org.apache.ws.security.crypto.provider", securityCryptoProvider);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.type", securityCryptoMerlinKeystoreType);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.password", securityCryptoMerlinKeystorePassword);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.alias", securityCryptoMerlinKeystoreAlias);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.file", securityCryptoMerlinKeystoreFile);
		return propsMap;
	}
		

}
