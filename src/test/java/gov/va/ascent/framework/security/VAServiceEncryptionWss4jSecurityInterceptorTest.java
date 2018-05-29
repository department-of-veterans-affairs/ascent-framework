package gov.va.ascent.framework.security;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;

public class VAServiceEncryptionWss4jSecurityInterceptorTest {

	@Before
	public final void setUp() throws Exception {
	}

	@After
	public final void tearDown() {

	}

	@Test
	public void testValidateMessage() { // throws IOException, ParserConfigurationException, SAXException, WSSecurityException {

		final VAServiceEncryptionWss4jSecurityInterceptor encryptionWss4jSecurityInterceptor =
				new VAServiceEncryptionWss4jSecurityInterceptor();

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage("src/test/resources/testFiles/security/soapMessage.xml");
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}

		encryptionWss4jSecurityInterceptor.validateMessage(sm, null);
	}
}
