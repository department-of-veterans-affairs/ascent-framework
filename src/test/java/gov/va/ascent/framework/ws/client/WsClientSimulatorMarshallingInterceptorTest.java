package gov.va.ascent.framework.ws.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@RunWith(MockitoJUnitRunner.class)
public class WsClientSimulatorMarshallingInterceptorTest {

	@Mock
	MethodInvocation mockMethodInvocationWithObjectArg;
	
	@Mock
	MethodInvocation mockMethodInvocationWithJaxBElementArg;	
	
	@SuppressWarnings("rawtypes")
	@Mock
	JAXBElement mockJaxbElement;
	@Mock
	Jaxb2Marshaller mockJaxbMarshaller;
	Object obj[]={new Object()};
	
	Object jaxbObj[] = new Object[1];
	
	Object respObj = new Object();
	@Before
	public void setUp() throws Exception {
		jaxbObj[0] = mockJaxbElement;
		when(mockMethodInvocationWithObjectArg.getArguments()).thenReturn(obj);
		when(mockMethodInvocationWithJaxBElementArg.getArguments()).thenReturn(jaxbObj);
		when(mockJaxbElement.getValue()).thenReturn("UnitTest");
		try{
			
			when(mockMethodInvocationWithJaxBElementArg.proceed()).thenReturn(respObj);
		}catch(Throwable e) {
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWsClientSimulatorMarshallingInterceptorMapOfStringJaxb2Marshaller() {
		Map<String, Jaxb2Marshaller> marshallerForPackageMap = new HashMap<String, Jaxb2Marshaller>();
		new WsClientSimulatorMarshallingInterceptor(marshallerForPackageMap);
	}

	@Test
	public void testWsClientSimulatorMarshallingInterceptorMapOfStringJaxb2MarshallerMapOfStringObject() {
		Map<String, Jaxb2Marshaller> marshallerForPackageMap = new HashMap<String, Jaxb2Marshaller>();
		Map<String,Object> objectFactoryForPackageMap = new HashMap<String,Object>();
		new WsClientSimulatorMarshallingInterceptor(marshallerForPackageMap, objectFactoryForPackageMap);
	}

	@Test
	public void testPostConstruct() {
		Map<String, Jaxb2Marshaller> marshallerForPackageMap = new HashMap<String, Jaxb2Marshaller>();
		WsClientSimulatorMarshallingInterceptor wscsmi = new WsClientSimulatorMarshallingInterceptor(marshallerForPackageMap);
		wscsmi.postConstruct();
	}

	@Test
	public void testInvokeForObjectArg() {
		Map<String, Jaxb2Marshaller> marshallerForPackageMap = new HashMap<String, Jaxb2Marshaller>();
		WsClientSimulatorMarshallingInterceptor wscsmi = new WsClientSimulatorMarshallingInterceptor(marshallerForPackageMap);
		try {
			assertNull(wscsmi.invoke(mockMethodInvocationWithObjectArg));
		}catch(Throwable e) {
			
		}
	}
	
	@Test
	public void testInvokeForJAXBElement() {
		Map<String, Jaxb2Marshaller> marshallerForPackageMap = new HashMap<String, Jaxb2Marshaller>();
		marshallerForPackageMap.put("java.lang", mockJaxbMarshaller);
		WsClientSimulatorMarshallingInterceptor wscsmi = new WsClientSimulatorMarshallingInterceptor(marshallerForPackageMap);
		try {
			assertNotNull(wscsmi.invoke(mockMethodInvocationWithJaxBElementArg));
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}	

}
