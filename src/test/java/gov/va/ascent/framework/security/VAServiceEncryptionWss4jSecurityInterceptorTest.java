package gov.va.ascent.framework.security;

import org.apache.ws.security.WSSecurityException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VAServiceEncryptionWss4jSecurityInterceptorTest {
	boolean logCalled = false;
	Wss4jSecurityInterceptor interceptor;

	@Before
	public final void setUp() throws Exception {		


	}

	@After
	public final void tearDown() {
	
	}

	@Test
	public void testAuditFired() throws IOException, ParserConfigurationException, SAXException, WSSecurityException {
		final VAServiceEncryptionWss4jSecurityInterceptor encryptionWss4jSecurityInterceptor =
				new VAServiceEncryptionWss4jSecurityInterceptor();
		final SoapMessage sm = WSInterceptorTestUtil.createSoapMessage("src/test/resources/testFiles/security/soapMessage.xml");

		encryptionWss4jSecurityInterceptor.validateMessage(sm, null);
		assertFalse(logCalled);
	}

	@Test
	public void testNoAuditFired() throws IOException, ParserConfigurationException, SAXException, WSSecurityException {

		final VAServiceEncryptionWss4jSecurityInterceptor encryptionWss4jSecurityInterceptor =
				new VAServiceEncryptionWss4jSecurityInterceptor();

		final SoapMessage sm = WSInterceptorTestUtil.createSoapMessage("src/test/resources/testFiles/security/soapMessage.xml");
		encryptionWss4jSecurityInterceptor.setAuditEnabled(true);
		encryptionWss4jSecurityInterceptor.setAuditFullMessageContentsEnabled(true);
		encryptionWss4jSecurityInterceptor.validateMessage(sm, null);
		assertTrue(logCalled);
	}

}
