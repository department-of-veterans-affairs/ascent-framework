package gov.va.ascent.framework.security;

import static org.mockito.Mockito.mock;

import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;
import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.config.BaseYamlConfig;



@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { BaseYamlConfig.class})
public class VAServiceSignatureWss4jSecurityInterceptor_UnitTest {

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
	
	@Test
	public void testSecureMessage() throws Exception {
		
		SoapMessage sm = WSInterceptorTestUtil.createSoapMessage(SOAP_MESSAGE_FILE);
		Properties props = retrieveCryptoProps();
		Crypto crypto = CryptoFactory.getInstance(props);
        interceptor.setCrypto(crypto);
        interceptor.setValidationActions("Signature");
        interceptor.setValidateRequest(false);
        interceptor.setValidateResponse(false);
        interceptor.setSecurementUsername("selfsigned");
        interceptor.setSecurementPassword("password");
        
        //interceptor.setValidationCallbackHandler(new SamlCallbackHandler());
        interceptor.afterPropertiesSet();   
        MessageContext messageContextMock = mock(MessageContext.class);
        //sm.setDocument(createDocument());
     	interceptor.secureMessage(sm, messageContextMock);
		
		Assert.assertTrue(sm.getSoapHeader()
				.examineHeaderElements(new QName("Security")).hasNext());

	}
	
	
	/**
	 * Create sample document 
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 */
	private Document createDocument() throws ParserConfigurationException {
		
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 dbf.setNamespaceAware(true);
		 DocumentBuilder builder = dbf.newDocumentBuilder();
	     Document doc = builder.newDocument();
        /* Element element = doc.createElement("root");
		 doc.appendChild(element);
		 Comment comment = doc.createComment("This is a comment");
		 doc.insertBefore(comment, element);
		 Element itemElement = doc.createElement("item");
		 element.appendChild(itemElement);
		 itemElement.setAttribute("myattr", "attrvalue");
		 itemElement.insertBefore(doc.createTextNode("text"), itemElement.getLastChild());*/
		 return doc;
	}
	
	
	/**
	 * Retrieves properties to set to create a crypto file
	 * @return
	 */
	private Properties retrieveCryptoProps() {
		Properties props = new Properties();
		props.put("org.apache.ws.security.crypto.provider", securityCryptoProvider);
		props.put("org.apache.ws.security.crypto.merlin.keystore.type", securityCryptoMerlinKeystoreType);
		props.put("org.apache.ws.security.crypto.merlin.keystore.password", securityCryptoMerlinKeystorePassword);
		props.put("org.apache.ws.security.crypto.merlin.keystore.alias", securityCryptoMerlinKeystoreAlias);
		props.put("org.apache.ws.security.crypto.merlin.keystore.file", securityCryptoMerlinKeystoreFile);
		return props;
	}
	

	/**
	 * Retrieves properties to set to create a crypto file
	 * @return
	 */
/*	private Map<Object, Object> retrieveCryptoProps() {
		Map<Object, Object> propsMap = new HashMap<Object, Object>();
		propsMap.put("org.apache.ws.security.crypto.provider", securityCryptoProvider);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.type", securityCryptoMerlinKeystoreType);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.password", securityCryptoMerlinKeystorePassword);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.alias", securityCryptoMerlinKeystoreAlias);
		propsMap.put("org.apache.ws.security.crypto.merlin.keystore.file", securityCryptoMerlinKeystoreFile);
		return propsMap;
	}*/
		

}
