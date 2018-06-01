package gov.va.ascent.framework.security;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.ws.security.WSSecurityException;
import org.junit.Assert;
import org.junit.Ignore;
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
import org.xml.sax.SAXException;
import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.config.BaseYamlConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { BaseYamlConfig.class})
public class VAServiceMustUnderstandWss4jSecurityInterceptor_UnitTest {

	private VAServiceMustUnderstandWss4jSecurityInterceptor interceptor = new VAServiceMustUnderstandWss4jSecurityInterceptor();
	
	private static final String SOAP_MESSAGE_FILE = "src/test/resources/testFiles/security/soapMessageMustUnderstand.xml";

	@Test
	public void testSecureMessage() throws IOException, ParserConfigurationException, SAXException {
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		MessageContext messageContextMock = mock(MessageContext.class);
		interceptor.secureMessage(sm, messageContextMock);
		Assert.assertTrue(WSInterceptorTestUtil.getRawXML(sm).indexOf("mustUnderstand", 0)<0);
	}
	
	@Ignore
    @Test  
	public void testSecureMessageWSSecurityException() throws IOException, ParserConfigurationException, SAXException {

		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		MessageContext messageContextMock = mock(MessageContext.class);
		VAServiceMustUnderstandWss4jSecurityInterceptor interceptor =  new VAServiceMustUnderstandWss4jSecurityInterceptor();
	    VAServiceMustUnderstandWss4jSecurityInterceptor interceptorSpy =  Mockito.spy(interceptor);
	    doThrow(WSSecurityException.class)
	     .when(interceptorSpy).secureMessage(sm, messageContextMock);
	    
	    interceptorSpy.secureMessage(sm, messageContextMock);
		
	}

}
