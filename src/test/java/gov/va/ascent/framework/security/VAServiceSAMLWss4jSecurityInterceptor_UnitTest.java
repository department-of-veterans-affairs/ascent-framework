package gov.va.ascent.framework.security;

import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;
import org.xml.sax.SAXException;

public class VAServiceSAMLWss4jSecurityInterceptor_UnitTest {

	private static final String SAML_FILE = "src/test/resources/encryption/EFolderService/SamlTokenEBN-UAT.xml";

	@Test
	public void testGettersAndSetters() {
		VAServiceSAMLWss4jSecurityInterceptor interceptor = new VAServiceSAMLWss4jSecurityInterceptor();
		interceptor.setSamlFile(SAML_FILE);
		Assert.assertEquals(SAML_FILE, interceptor.getSamlFile());
	}

	//@Test
	public void testSecureMessage() throws IOException,
			ParserConfigurationException, SAXException {

		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage("src/test/resources/testFiles/security/soapMessage.xml");
		VAServiceSAMLWss4jSecurityInterceptor interceptor = new VAServiceSAMLWss4jSecurityInterceptor();
		interceptor.setSamlFile(SAML_FILE);
		interceptor.secureMessage(sm, null);
		Assert.assertTrue(sm.getSoapHeader()
				.examineHeaderElements(new QName("Security")).hasNext());

	}

	

}
