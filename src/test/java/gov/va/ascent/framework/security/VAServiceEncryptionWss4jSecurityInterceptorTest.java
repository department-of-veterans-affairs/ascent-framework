package gov.va.ascent.framework.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.xml.sax.SAXException;

public class VAServiceEncryptionWss4jSecurityInterceptorTest {

	private static final String SOAP_MESSAGE_FILE = "src/test/resources/testFiles/security/soapMessage.xml";

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

	/** The security.crypto.merlin.keystore.alias */
	private String securityCryptoMerlinKeystoreAlias;

	private CryptoProperties propsCrypto;

	private VAServiceEncryptionWss4jSecurityInterceptor interceptor = Mockito.spy(VAServiceEncryptionWss4jSecurityInterceptor.class);

	@Before
	public final void setUp() throws Exception {
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

		when(interceptor.retrieveCryptoProps()).thenReturn(props);
		propsCrypto = interceptor.retrieveCryptoProps();

		assertNotNull(propsCrypto);

		securityCryptoMerlinKeystoreAlias = (String) propsCrypto.get("org.apache.ws.security.crypto.merlin.keystore.alias");
	}

	@After
	public final void tearDown() {

	}

	@Test
	public void testSecureMessage() throws WSSecurityException {

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			fail("VAServiceEncryptionWss4jSecurityInterceptor : testSecureMessage test method fail");
		}
		Crypto crypto = CryptoFactory.getInstance(propsCrypto);
		crypto.setDefaultX509Identifier(securityCryptoMerlinKeystoreAlias);
		interceptor.setValidationActions("Encrypt");
		interceptor.setValidateRequest(false);
		interceptor.setValidateResponse(false);
		interceptor.setSecurementUsername("selfsigned");
		interceptor.setSecurementPassword("password");
		interceptor.afterPropertiesSet();
		MessageContext messageContextMock = mock(MessageContext.class);
		interceptor.secureMessage(sm, messageContextMock);

		assertNotNull(sm);

	}

	@Test
	public void testSecureMessageNoCrypto() throws WSSecurityException {

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			fail("VAServiceEncryptionWss4jSecurityInterceptor : testSecureMessage test method fail");
		}
		interceptor.setValidationActions("Encrypt");
		interceptor.setValidateRequest(false);
		interceptor.setValidateResponse(false);
		interceptor.setSecurementUsername("selfsigned");
		interceptor.setSecurementPassword("password");
		interceptor.afterPropertiesSet();
		MessageContext messageContextMock = mock(MessageContext.class);
		interceptor.secureMessage(sm, messageContextMock);

		assertNotNull(sm);

	}

	@Test
	public void testValidateMessage() {

		SoapMessage sm = null;
		try {
			sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}

		interceptor.validateMessage(sm, null);
	}
}
