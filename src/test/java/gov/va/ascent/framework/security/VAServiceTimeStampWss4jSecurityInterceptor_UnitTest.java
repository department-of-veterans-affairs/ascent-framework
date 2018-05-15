package gov.va.ascent.framework.security;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;
import org.xml.sax.SAXException;

public class VAServiceTimeStampWss4jSecurityInterceptor_UnitTest {
	private static final String TTL_STR = "300";

	@Test
	public void testGettersAndSetters() {
		VAServiceTimeStampWss4jSecurityInterceptor interceptor = new VAServiceTimeStampWss4jSecurityInterceptor();
		interceptor.setTimeStamp(TTL_STR);
		Assert.assertEquals(TTL_STR, interceptor.getTimeStamp());
	}

	@Test
	public void testSecureMessage() throws IOException,
			ParserConfigurationException, SAXException {
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage("src/test/resources/testFiles/security/soapMessage.xml");

		VAServiceTimeStampWss4jSecurityInterceptor interceptor = new VAServiceTimeStampWss4jSecurityInterceptor();
		interceptor.setTimeStamp(TTL_STR);
		interceptor.setSecurementActions("Encrypt");
		interceptor.secureMessage(sm, null);
		Assert.assertTrue(sm.getSoapHeader()
				.examineHeaderElements(new QName("Security")).hasNext());

		String xml = WSInterceptorTestUtil.getRawXML(sm);
		String strCreated = StringUtils.substringBetween(xml, "<wsu:Created>",
				"</wsu:Created>");
		String strExpires = StringUtils.substringBetween(xml, "<wsu:Expires>",
				"</wsu:Expires>");
		
		Assert.assertEquals( Integer.parseInt(TTL_STR)*1000, DatatypeConverter.parseDate(strExpires).getTimeInMillis()-DatatypeConverter.parseDate(strCreated).getTimeInMillis());

	}

	

}
