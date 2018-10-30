package gov.va.ascent.framework.security;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.ws.security.WSSecurityException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import gov.va.ascent.framework.exception.AscentRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { BaseYamlConfig.class })
public class VAServiceSAMLWss4jSecurityInterceptorTest {

	private static final String SAML_FILE = "src/test/resources/encryption/EFolderService/SamlTokenEBN-UAT.xml";
	private static final String NONEXISTENT_FILE = "someFileNameThatDoesNotExist.xml";

	private static final String SOAP_MESSAGE_FILE = "src/test/resources/testFiles/security/soapMessageMustUnderstand.xml";

	@Mock
	VAServiceSAMLWss4jSecurityInterceptor interceptor = new VAServiceSAMLWss4jSecurityInterceptor();

	@Before
	public void setup() {
		interceptor.setValidationActions("NoSecurity");
		interceptor.setValidateRequest(false);
		interceptor.setValidateResponse(false);
		interceptor.setSecurementUsername("selfsigned");
		interceptor.setSecurementPassword("password");
		interceptor.setSamlFile(SAML_FILE);
		try {
			interceptor.afterPropertiesSet();
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}
	}

	@Test
	public void testGettersAndSetters() {
		final VAServiceSAMLWss4jSecurityInterceptor interceptor = new VAServiceSAMLWss4jSecurityInterceptor();
		interceptor.setSamlFile(SAML_FILE);
		Assert.assertEquals(SAML_FILE, interceptor.getSamlFile());
	}

	@Test
	public void testSecureMessage() {

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}

		final MessageContext messageContextMock = mock(MessageContext.class);
		interceptor.secureMessage(sm, messageContextMock);
		Assert.assertNotNull(sm.getDocument());

		final VAServiceSAMLWss4jSecurityInterceptor spiedInterceptor = Mockito.spy(interceptor);
		try {
			doReturn(null).when(spiedInterceptor).getSAMLAssertionAsElement();
		} catch (final WSSecurityException e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}
		spiedInterceptor.secureMessage(sm, messageContextMock);
		Assert.assertNotNull(sm.getDocument());

		try {
			doThrow(new WSSecurityException("Testing")).when(spiedInterceptor).getSAMLAssertionAsElement();
		} catch (final WSSecurityException e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}
		try {
			spiedInterceptor.secureMessage(sm, messageContextMock);
		} catch (AscentRuntimeException e) {
			assertTrue((e.getCause() instanceof WSSecurityException) && e.getCause().getMessage().equals("Testing"));
		}

		Assert.assertNotNull(sm.getDocument());
	}

	@Test
	public void testGetSAMLAssertionAsElementWithValidFile() {
		interceptor.setSamlFile(SAML_FILE);
		try {
			interceptor.afterPropertiesSet();
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}

		try {
			interceptor.getSAMLAssertionAsElement();
		} catch (final WSSecurityException e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}
	}

	@Test
	public void testGetSAMLAssertionAsElement() {
		interceptor.setSamlFile(NONEXISTENT_FILE);
		try {
			interceptor.afterPropertiesSet();
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}

		try {
			interceptor.getSAMLAssertionAsElement();
		} catch (final WSSecurityException e) {
			e.printStackTrace();
			fail("Should not throw exception here.");
		}
	}
}
