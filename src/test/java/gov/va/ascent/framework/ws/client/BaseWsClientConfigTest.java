package gov.va.ascent.framework.ws.client;

import gov.va.ascent.framework.exception.InterceptingExceptionTranslator;
import gov.va.ascent.framework.log.PerformanceLogMethodInterceptor;
import gov.va.ascent.framework.ws.security.VAServiceWss4jSecurityInterceptor;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import javax.xml.soap.SOAPException;

import static org.junit.Assert.assertTrue;
@RunWith(MockitoJUnitRunner.class)
public class BaseWsClientConfigTest {

	@Mock
	Marshaller mockMarshaller;
	@Mock
	Unmarshaller mockUnmarshaller;
	@Mock
	ClientInterceptor mockClientInterceptor;
	ClientInterceptor intercpetors[]= {mockClientInterceptor};
	@Mock
	HttpResponseInterceptor mockHttpResponseInterceptor;
	HttpResponseInterceptor respInterceptors[] = {mockHttpResponseInterceptor};
	@Mock
	HttpRequestInterceptor mockHttpRequestInterceptor;
	HttpRequestInterceptor reqInterceptors[] = {mockHttpRequestInterceptor};
	@Mock
	WebServiceMessageFactory mockWebServiceMessageFactory;
	@Mock
	Resource mockResource;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateDefaultWebServiceTemplateStringIntIntMarshallerUnmarshaller() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		assertTrue(test.createDefaultWebServiceTemplate("http://dummyservice/endpoint", 30, 30, mockMarshaller, mockUnmarshaller) instanceof WebServiceTemplate);
	}

	@Test
	public void testCreateDefaultWebServiceTemplateStringIntIntMarshallerUnmarshallerClientInterceptorArray() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		assertTrue(test.createDefaultWebServiceTemplate("http://dummyservice/endpoint", 30, 30, mockMarshaller, mockUnmarshaller, intercpetors) instanceof WebServiceTemplate);		
	}

	@Test
	public void testCreateDefaultWebServiceTemplateStringIntIntMarshallerUnmarshallerHttpRequestInterceptorArrayHttpResponseInterceptorArrayClientInterceptorArray() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		assertTrue(test.createDefaultWebServiceTemplate("http://dummyservice/endpoint", 30, 30, mockMarshaller, mockUnmarshaller, reqInterceptors, respInterceptors, intercpetors) instanceof WebServiceTemplate);
	}

	@Test
	public void testCreateSAAJWebServiceTemplate() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		try{
			assertTrue(test.createSAAJWebServiceTemplate("http://dummyservice/endpoint", 30, 30, mockMarshaller, mockUnmarshaller, intercpetors) instanceof WebServiceTemplate);
		}catch(SOAPException e) {
			
		}
	}

	@Test
	public void testCreateWebServiceTemplate() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		assertTrue(test.createWebServiceTemplate("http://dummyservice/endpoint", 30, 30, mockMarshaller, mockUnmarshaller, reqInterceptors, respInterceptors, intercpetors, mockWebServiceMessageFactory) instanceof WebServiceTemplate);
	}

	@Test
	public void testGetBeanNameAutoProxyCreator() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		String beanNames[] = {"TestBeanName"};
		String interceptorNames[] = {"TestInterceptorName"};
		assertTrue(test.getBeanNameAutoProxyCreator(beanNames, interceptorNames) instanceof BeanNameAutoProxyCreator);
	}

	@Test
	public void testGetInterceptingExceptionTranslator() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		try{
			assertTrue(test.getInterceptingExceptionTranslator("Exception", "java.lang") instanceof InterceptingExceptionTranslator);
		}catch(Exception e) {
			
		}
	}

	@Test
	public void testGetMarshaller() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		Resource resources[] = {mockResource};
		try {
			test.getMarshaller("com.oracle.xmlns.internal.webservices.jaxws_databinding", resources, true);
		}catch(Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
			Assert.assertNotNull(e.getMessage());		
		}
	}
	//pkg-gov.va.ascent.demo.partner.person.ws.transfer
	//resource-xsd/PersonService/PersonWebService.xsd
	@Test
	public void testGetPerformanceLogMethodInterceptor() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		assertTrue(test.getPerformanceLogMethodInterceptor(5) instanceof PerformanceLogMethodInterceptor);
	}

	@Test
	public void testGetVAServiceWss4jSecurityInterceptor() {
		BaseWsClientConfig test = new BaseWsClientConfig();
		assertTrue(test.getVAServiceWss4jSecurityInterceptor("testuser", "test123", "EVSS") instanceof VAServiceWss4jSecurityInterceptor);
	}

}
