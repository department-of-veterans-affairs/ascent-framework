package gov.va.ascent.framework.security;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.ws.security.WSSecurityException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;
import org.xml.sax.SAXException;


public class VAServiceEncryptionWss4jSecurityInterceptorTest {

	@Before
	public final void setUp() throws Exception {
	}

	@After
	public final void tearDown() {
	
	}

	@Test
	public void testValidateMessage() throws IOException, ParserConfigurationException, SAXException, WSSecurityException {
		final VAServiceEncryptionWss4jSecurityInterceptor encryptionWss4jSecurityInterceptor =
				new VAServiceEncryptionWss4jSecurityInterceptor();
		final SoapMessage sm = WSInterceptorTestUtil.createSoapMessage("src/test/resources/testFiles/security/soapMessage.xml");

		encryptionWss4jSecurityInterceptor.validateMessage(sm, null);
	}
}
