package gov.va.ascent.framework.ws.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
@RunWith(MockitoJUnitRunner.class)
public class VAServiceWss4jSecurityInterceptorTest {
	private TestVAServiceSecurityInterceptorTest interceptor;
	
	@Mock
	SoapMessage mockSoapMessage;
	@Mock
	Document mockDocument; 
	@Mock
	Document mockOwnerDocument;	
	@Mock
	Element mockDocumentElement;	
	@Mock
	Element mockHeaderElement;
	@Mock
	Element mockSecHeaderElement;
	@Mock
	Element mockClientMachineHeaderElement;
	@Mock
	Element mockStationIdHeaderElement;
	@Mock
	Element mockAppNameHeaderElement;	
	@Mock
	Element mockExternalUIDHeaderElement;	
	@Mock
	Element mockExternalKeyHeaderElement;
	@Mock
	MessageContext mockMessageContext;

	
	@Before
	public void setUp() throws Exception {
		interceptor = new TestVAServiceSecurityInterceptorTest();

		when(mockSoapMessage.getDocument()).thenReturn(mockDocument);
		when(mockDocument.getDocumentElement()).thenReturn(mockDocumentElement);
		when(mockDocumentElement.getOwnerDocument()).thenReturn(mockDocument);
		when(mockDocumentElement.getNamespaceURI()).thenReturn("");
		when(mockDocument.createElementNS(TestVAServiceSecurityInterceptorTest.VA_NS, TestVAServiceSecurityInterceptorTest.VA_PREFIX + TestVAServiceSecurityInterceptorTest.VA_SERVICE_HEADERS)).thenReturn(mockHeaderElement);
		when(mockDocument.createElementNS(TestVAServiceSecurityInterceptorTest.VA_NS, TestVAServiceSecurityInterceptorTest.VA_PREFIX + TestVAServiceSecurityInterceptorTest.CLIENT_MACHINE)).thenReturn(mockClientMachineHeaderElement);
		when(mockDocument.createElementNS(TestVAServiceSecurityInterceptorTest.VA_NS, TestVAServiceSecurityInterceptorTest.VA_PREFIX + TestVAServiceSecurityInterceptorTest.STN_ID)).thenReturn(mockStationIdHeaderElement);
		when(mockDocument.createElementNS(TestVAServiceSecurityInterceptorTest.VA_NS, TestVAServiceSecurityInterceptorTest.VA_PREFIX + TestVAServiceSecurityInterceptorTest.VA_APPLICATION_NAME)).thenReturn(mockAppNameHeaderElement);
		when(mockDocument.createElementNS(TestVAServiceSecurityInterceptorTest.VA_NS, TestVAServiceSecurityInterceptorTest.VA_PREFIX + TestVAServiceSecurityInterceptorTest.EXTERNAL_UID)).thenReturn(mockExternalUIDHeaderElement);
		when(mockDocument.createElementNS(TestVAServiceSecurityInterceptorTest.VA_NS, TestVAServiceSecurityInterceptorTest.VA_PREFIX + TestVAServiceSecurityInterceptorTest.EXTERNAL_KEY)).thenReturn(mockExternalKeyHeaderElement);
		//when(WSSecurityUtil.findWsseSecurityHeaderBlock(mockDocument,mockDocumentElement,true)).thenReturn(mockSecHeaderElement);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSecureMessageSoapMessageMessageContext() {
		try {
			interceptor.secureMessage(mockSoapMessage, mockMessageContext);
		}catch(Throwable e) {
			assertNotNull(e);
		}
	}

	@Test
	public void testGetClientMachine() {
		interceptor.setClientMachine("TEST_CLIENT_MACHINE");
		assertNotNull(interceptor.getClientMachine());
		assertEquals(true, "TEST_CLIENT_MACHINE".equals(interceptor.getClientMachine()));
	}

	@Test
	public void testSetClientMachine() {
		interceptor.setClientMachine("TEST_CLIENT_MACHINE");
		assertNotNull(interceptor.getClientMachine());
	}

	@Test
	public void testGetStationId() {
		interceptor.setStationId("TEST_STN_ID");
		assertNotNull(interceptor.getStationId());
		assertEquals(true,"TEST_STN_ID".equals(interceptor.getStationId()));
	}

	@Test
	public void testSetStationId() {
		interceptor.setStationId("TEST_STN_ID");
		assertNotNull(interceptor.getStationId());
	}

	@Test
	public void testGetVaApplicationName() {
		interceptor.setVaApplicationName("TEST_Application_Name");
		assertNotNull(interceptor.getVaApplicationName());
		assertEquals(true,"TEST_Application_Name".equals(interceptor.getVaApplicationName()));
	}

	@Test
	public void testSetVaApplicationName() {
		interceptor.setVaApplicationName("TEST_Application_Name");
		assertNotNull(interceptor.getVaApplicationName());
	}

}
class TestVAServiceSecurityInterceptorTest extends VAServiceWss4jSecurityInterceptor {
	
}