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

public class VAServiceTimeStampWss4jSecurityInterceptorTest {
	private static final String TTL_STR = "300";

	/** The property name whose value would be the crypto provider */
	private static final String APACHE_PROVIDER = "org.apache.ws.security.crypto.provider";
	/** The property name whose value would be the keystore type originator (provider of this type of keystore) */
	private static final String APACHE_KS_PROVIDER = "org.apache.ws.security.crypto.merlin.keystore.provider";
	/** The property name whose value would be the keystore type */
	private static final String APACHE_KS_TYPE = "org.apache.ws.security.crypto.merlin.keystore.type";
	/** The property name whose value would be the keystore password */
	private static final String APACHE_KS_PW = "org.apache.ws.security.crypto.merlin.keystore.password";
	/** The property name whose value would be the alias of the desired certificate in the keystore */
	private static final String APACHE_KS_ALIAS = "org.apache.ws.security.crypto.merlin.keystore.alias";
	/** The property name whose value would be the path to the keystore file */
	private static final String APACHE_KS_FILE = "org.apache.ws.security.crypto.merlin.keystore.file";
	/** The property name whose value would be time-stamp of the TTL (time to live) */
	private static final String TIMESTAMP_TTL = "vetservices-partner-efolder.ws.client.security.timestamp.ttl";

	@Test
	public void testGettersAndSetters() {
		VAServiceTimeStampWss4jSecurityInterceptor interceptor = new VAServiceTimeStampWss4jSecurityInterceptor() {

			@Override
			public CryptoProperties retrieveCryptoProps() {
				CryptoProperties props = new CryptoProperties() {

					private static final long serialVersionUID = 4980226916621404426L;

					@Override
					public String getCryptoEncryptionAlias() {
						return this.getProperty(APACHE_KS_ALIAS);
					}

					@Override
					public String getCryptoDefaultAlias() {
						return this.getProperty(APACHE_KS_ALIAS);
					}

					@Override
					public String getCryptoKeystorePw() {
						return this.getProperty(APACHE_KS_PW);
					}

					@Override
					public String getTimeStampTtl() {
						return this.getProperty(TIMESTAMP_TTL);
					}

				};
				props.setProperty(APACHE_PROVIDER, "org.apache.ws.security.components.crypto.Merlin");
				props.setProperty(APACHE_KS_PROVIDER, "SUN");
				props.setProperty(APACHE_KS_TYPE, "jks");
				props.setProperty(APACHE_KS_PW, "changeit");
				props.setProperty(APACHE_KS_ALIAS, "ebn_vbms_cert");
				props.setProperty(APACHE_KS_FILE, "/encryption/EFolderService/vbmsKeystore.jks");
				props.setProperty(TIMESTAMP_TTL, "300");
				return props;
			}
		};
		interceptor.setTimeStampTtl(TTL_STR);
		Assert.assertEquals(TTL_STR, interceptor.getTimeStampTtl());
	}

	@Test
	public void testSecureMessage() throws IOException, ParserConfigurationException, SAXException {
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage("src/test/resources/testFiles/security/soapMessage.xml");

		VAServiceTimeStampWss4jSecurityInterceptor interceptor = new VAServiceTimeStampWss4jSecurityInterceptor() {

			@Override
			public CryptoProperties retrieveCryptoProps() {
				CryptoProperties props = new CryptoProperties() {

					private static final long serialVersionUID = 4980226916621404426L;

					@Override
					public String getCryptoEncryptionAlias() {
						return this.getProperty(APACHE_KS_ALIAS);
					}

					@Override
					public String getCryptoDefaultAlias() {
						return this.getProperty(APACHE_KS_ALIAS);
					}

					@Override
					public String getCryptoKeystorePw() {
						return this.getProperty(APACHE_KS_PW);
					}

					@Override
					public String getTimeStampTtl() {
						return this.getProperty(TIMESTAMP_TTL);
					}

				};
				props.setProperty(APACHE_PROVIDER, "org.apache.ws.security.components.crypto.Merlin");
				props.setProperty(APACHE_KS_PROVIDER, "SUN");
				props.setProperty(APACHE_KS_TYPE, "jks");
				props.setProperty(APACHE_KS_PW, "changeit");
				props.setProperty(APACHE_KS_ALIAS, "ebn_vbms_cert");
				props.setProperty(APACHE_KS_FILE, "/encryption/EFolderService/vbmsKeystore.jks");
				props.setProperty(TIMESTAMP_TTL, "300");
				return props;
			}
		};
		interceptor.setTimeStampTtl(TTL_STR);
		interceptor.secureMessage(sm, null);
		Assert.assertTrue(sm.getSoapHeader().examineHeaderElements(new QName("Security")).hasNext());

		String xml = WSInterceptorTestUtil.getRawXML(sm);
		String strCreated = StringUtils.substringBetween(xml, "<wsu:Created>", "</wsu:Created>");
		String strExpires = StringUtils.substringBetween(xml, "<wsu:Expires>", "</wsu:Expires>");

		Assert.assertEquals(Integer.parseInt(TTL_STR) * 1000,
				DatatypeConverter.parseDate(strExpires).getTimeInMillis() - DatatypeConverter.parseDate(strCreated).getTimeInMillis());

	}

}
