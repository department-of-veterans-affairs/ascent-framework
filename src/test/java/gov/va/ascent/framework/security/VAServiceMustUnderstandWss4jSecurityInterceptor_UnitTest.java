package gov.va.ascent.framework.security;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;
import org.xml.sax.SAXException;

public class VAServiceMustUnderstandWss4jSecurityInterceptor_UnitTest {

	private VAServiceMustUnderstandWss4jSecurityInterceptor interceptor = new VAServiceMustUnderstandWss4jSecurityInterceptor();

	@Test
	public void testSecureMessage() throws IOException, ParserConfigurationException, SAXException {
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage("src/test/resources/testFiles/security/soapMessage.xml");
		
		VAServiceTimeStampWss4jSecurityInterceptor tsInterceptor = new VAServiceTimeStampWss4jSecurityInterceptor();
		//tsInterceptor.setSecurementActions("UsernameToken");
		//tsInterceptor.setSecurementUsername("Username");
		//tsInterceptor.setSecurementPassword("Password");
		//tsInterceptor.setSecurementPasswordType("PasswordText");
		//tsInterceptor.setSecurementUsernameTokenElements("Created");
		tsInterceptor.setTimeStamp("60");
		//tsInterceptor.setValidateRequest(false);
		//tsInterceptor.setValidateResponse(false);
		
		tsInterceptor.secureMessage(sm, null);
		System.out.println(WSInterceptorTestUtil.getRawXML(sm));
		Assert.assertTrue(WSInterceptorTestUtil.getRawXML(sm).indexOf("mustUnderstand", 0)>0);
		
		interceptor.secureMessage(sm, null);
		System.out.println(WSInterceptorTestUtil.getRawXML(sm));
		Assert.assertTrue(WSInterceptorTestUtil.getRawXML(sm).indexOf("mustUnderstand", 0)<0);
	}

}
