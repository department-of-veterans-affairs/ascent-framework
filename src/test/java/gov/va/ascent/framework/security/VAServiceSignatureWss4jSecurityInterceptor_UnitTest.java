package gov.va.ascent.framework.security;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.callback.CallbackHandler;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.saml.SAMLIssuerImpl;
import org.apache.wss4j.common.saml.SAMLCallback;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.xml.sax.SAXException;

public class VAServiceSignatureWss4jSecurityInterceptor_UnitTest {

	private static final String TRUST_STORE_FILE = "src/test/resources/encryption/EFolderService/vaebnTruststore.jks";
	private static final String PROPERTIES_FILE = "apache-trusted-crypto.properties";
	private static final String KEY_STORE_FILE = "src/test/resources/encryption/EFolderService/vbmsKeystore.jks";

	private static final String SOAP_MESSAGE_FILE = "src/test/resources/testFiles/security/soapMessage.xml";
	
	/**
	 * The security crypto provider
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.provider}")
	private String securityCryptoProvider;

	/**
	 * The security.crypto.merlin.keystore.type
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.merlin.keystore.type}")
	private String securityCryptoMerlinKeystoreType;
	
	/**
	 * The security.crypto.merlin.keystore.password.
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.merlin.keystore.password}")
	private String securityCryptoMerlinKeystorePassword;
	
	/**
	 * The security.crypto.merlin.keystore.alias
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.merlin.keystore.alias}")
	private String securityCryptoMerlinKeystoreAlias;
	
	/**
	 * The securityCryptoMerlinKeystoreFile
	 */
	@Value("${ascent-framework.org.apache.ws.security.crypto.merlin.keystore.file}")
	private String securityCryptoMerlinKeystoreFile;

	@Mock
	VAServiceSignatureWss4jSecurityInterceptor interceptor = new VAServiceSignatureWss4jSecurityInterceptor();
	
	//@Test
	public void testSecureMessage() throws Exception {
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		Map<Object, Object> propsMap = retrieveCryptoProps();
        interceptor.setCrypto(CryptoFactory.getInstance(Crypto.class, propsMap));
		interceptor.secureMessage(sm, null);
		Assert.assertTrue(sm.getSoapHeader()
				.examineHeaderElements(new QName("Security")).hasNext());

		/*String xml = WSInterceptorTestUtil.getRawXML(sm);
		String strCreated = StringUtils.substringBetween(xml, "<wsu:Created>",
				"</wsu:Created>");
		String strExpires = StringUtils.substringBetween(xml, "<wsu:Expires>",
				"</wsu:Expires>");*/
		
		//Assert.assertEquals( Integer.parseInt(TTL_STR)*1000, DatatypeConverter.parseDate(strExpires).getTimeInMillis()-DatatypeConverter.parseDate(strCreated).getTimeInMillis());

	}
	

	/**
	 * Retrieves properties to set to create a crypto file
	 * @return
	 */
	private Map<Object, Object> retrieveCryptoProps() {
		Map<Object, Object> propsMap = new HashMap<Object, Object>();
		propsMap.put("org.apache.ws.security.crypto.provider", securityCryptoProvider);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.type", securityCryptoMerlinKeystoreType);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.password", securityCryptoMerlinKeystorePassword);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.alias", securityCryptoMerlinKeystoreAlias);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.file", securityCryptoMerlinKeystoreFile);
		return propsMap;
	}
	
	private Crypto createCrypto() throws Exception {
		
        WebServiceTemplate template = new WebServiceTemplate();
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		messageFactory.afterPropertiesSet();
		template.setMessageFactory(messageFactory);
        Properties prop = new Properties();
		prop.load(this.getClass().getResourceAsStream(PROPERTIES_FILE));
		CryptoFactoryBean cryptoFactory = new CryptoFactoryBean();
		//cryptoFactory.setKeyStoreLocation(new ClassPathResource(KEY_STORE_FILE));
		cryptoFactory.setConfiguration(prop);
		//cryptoFactory.setKeyStorePassword("password");
		cryptoFactory.afterPropertiesSet();
		Crypto crypto = (Crypto) cryptoFactory.getObject();
	
/*		SAMLIssuerImpl issuer = new SAMLIssuerImpl();
		issuer.setIssuerCrypto(crypto);
		issuer.setIssuerKeyName("selfsigned");
		issuer.setIssuerKeyPassword("password");
		issuer.setIssuerName("selfsigned");
		issuer.setSendKeyValue(false);
		issuer.setSignAssertion(true);
		
		Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
		securityInterceptor.setSecurementActions("Timestamp SAMLTokenSigned");
		securityInterceptor.setSecurementSignatureCrypto(crypto);
		securityInterceptor.setSecurementUsername("selfsigned");
		securityInterceptor.setSecurementPassword("password");
		securityInterceptor.setSamlIssuer(issuer);
		securityInterceptor.afterPropertiesSet();*/
		
		return crypto;

	}

	

}
