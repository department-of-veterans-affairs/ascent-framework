package gov.va.ascent.framework.security;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.ws.security.WSSecurityException;
import org.apache.wss4j.dom.util.WSSecurityUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.config.BaseYamlConfig;
import gov.va.ascent.framework.exception.AscentRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { BaseYamlConfig.class })
public class VAServiceMustUnderstandWss4jSecurityInterceptor_UnitTest {

	private final VAServiceMustUnderstandWss4jSecurityInterceptor interceptor = new VAServiceMustUnderstandWss4jSecurityInterceptor();

	private static final String SOAP_MUSTUNDERSTAND_FILE = "src/test/resources/testFiles/security/soapMessageMustUnderstand.xml";
	private static final String SOAP_NO_MUSTUNDERSTAND_FILE = "src/test/resources/testFiles/security/soapMessageNoMustUnderstand.xml";
	private static final String SOAP_EMPTY_FILE = "src/test/resources/testFiles/security/soapEmpty.xml";

	@Test
	public void testSecureMessage() throws IOException, ParserConfigurationException, SAXException {
		// with "mustUnderstand"
		final SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MUSTUNDERSTAND_FILE);
		final MessageContext messageContextMock = mock(MessageContext.class);
		interceptor.secureMessage(sm, messageContextMock);
		Assert.assertTrue(WSInterceptorTestUtil.getRawXML(sm).indexOf("mustUnderstand", 0) < 0);

		// without "mustUnderstand"
		final SoapMessage smNo = WSInterceptorTestUtil.createSoapMessage(SOAP_NO_MUSTUNDERSTAND_FILE);
		interceptor.secureMessage(smNo, messageContextMock);
		Assert.assertTrue(WSInterceptorTestUtil.getRawXML(smNo).indexOf("mustUnderstand", 0) < 0);

		// without a security header
		final SoapMessage smEmpty = WSInterceptorTestUtil.createSoapMessage(SOAP_EMPTY_FILE);
		interceptor.secureMessage(smEmpty, messageContextMock);
		Assert.assertTrue(WSInterceptorTestUtil.getRawXML(smEmpty).indexOf("mustUnderstand", 0) < 0);
	}

	@Test
	public void testSecureMessageWSSecurityException() throws IOException, ParserConfigurationException, SAXException {

		final SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MUSTUNDERSTAND_FILE);
		final MessageContext messageContextMock = mock(MessageContext.class);

		final VAServiceMustUnderstandWss4jSecurityInterceptor interceptor = new VAServiceMustUnderstandWss4jSecurityInterceptor();
		final VAServiceMustUnderstandWss4jSecurityInterceptor interceptorSpy = Mockito.spy(interceptor);

		try {
			doThrow(new WSSecurityException("Testing")).when(interceptorSpy).removeAttributeWithSOAPNS(any(), any());
		} catch (final WSSecurityException e) {
			e.printStackTrace();
			fail("Should not have thrown exception here.");
		}

		Element securityHeader = null;
		try {
			securityHeader = WSSecurityUtil.findWsseSecurityHeaderBlock(
					sm.getDocument(), sm.getDocument().getDocumentElement(), null, false);
		} catch (final org.apache.wss4j.common.ext.WSSecurityException e) {
			e.printStackTrace();
			fail("Should not have thrown exception here.");
		}
		// confirm mustUnderstand is in the header before attempting to remove it
		assertTrue(securityHeader.hasAttribute("soapenv:mustUnderstand"));

		try {
			interceptorSpy.secureMessage(sm, messageContextMock);
		} catch (AscentRuntimeException e) {
			assertTrue((e.getCause() instanceof WSSecurityException) && e.getCause().getMessage().equals("Testing"));
		}

		// confirm mustUnderstand was NOT removed due to forced mock exception
		assertTrue(securityHeader.hasAttribute("soapenv:mustUnderstand"));

	}

	@Test
	public void testRemoveAttributeWithSOAPNS() {
		final VAServiceMustUnderstandWss4jSecurityInterceptor interceptor = new VAServiceMustUnderstandWss4jSecurityInterceptor();

		try {
			interceptor.removeAttributeWithSOAPNS(null, "soapenv:mustUnderstand");
		} catch (final WSSecurityException e) {
			fail("Should not have thrown exception here.");
		}
	}

}
