package gov.va.ascent.framework.security;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.saml.SAMLIssuerImpl;
import org.apache.ws.security.saml.ext.SAMLCallback;
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
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.xml.sax.SAXException;

import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.config.BaseYamlConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { BaseYamlConfig.class})
public class VAServiceSAMLWss4jSecurityInterceptor_UnitTest {

	private static final String SAML_FILE = "encryption/EFolderService/SamlTokenEBN-UAT.xml";
	
	private static final String SOAP_MESSAGE_FILE = "src/test/resources/testFiles/security/soapMessageMustUnderstand.xml";
	
	@Mock
	VAServiceSAMLWss4jSecurityInterceptor interceptor = new VAServiceSAMLWss4jSecurityInterceptor();

	@Test
	public void testGettersAndSetters() {
		VAServiceSAMLWss4jSecurityInterceptor interceptor = new VAServiceSAMLWss4jSecurityInterceptor();
		interceptor.setSamlFile(SAML_FILE);
		Assert.assertEquals(SAML_FILE, interceptor.getSamlFile());
	}

	@Test
	public void testSecureMessage() throws Exception {


		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
        interceptor.setValidationActions("NoSecurity");
        interceptor.setValidateRequest(false);
        interceptor.setValidateResponse(false);
        interceptor.setSecurementUsername("selfsigned");
        interceptor.setSecurementPassword("password");
        interceptor.setSamlFile(SAML_FILE);
        interceptor.afterPropertiesSet();   
        MessageContext messageContextMock = mock(MessageContext.class);
        interceptor.secureMessage(sm, messageContextMock);

		Assert.assertNotNull(sm.getDocument());

	}
	

}
