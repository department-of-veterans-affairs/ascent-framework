package gov.va.ascent.framework.rest.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.audit.AuditEvents;
import gov.va.ascent.framework.audit.Auditable;
import gov.va.ascent.framework.exception.AscentRuntimeException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPart;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.service.ServiceRequest;
import gov.va.ascent.framework.service.ServiceResponse;

@RunWith(MockitoJUnitRunner.class)
public class RestProviderHttpResponseCodeAspectTest {

	private Logger restProviderLog = (Logger) org.slf4j.LoggerFactory.getLogger(RestProviderHttpResponseCodeAspect.class);
	
	private RestProviderHttpResponseCodeAspect restProviderHttpResponseCodeAspect;
    @Mock
	private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private ResponseEntity<ServiceResponse> responseEntity;
    
    @Mock
    private ServiceResponse serviceResponse;
	
    @Mock
    private MethodSignature mockSignature;
    
    
	@InjectMocks
	private RestProviderHttpResponseCodeAspect requestResponseAspect = new RestProviderHttpResponseCodeAspect();

    private TestServiceRequest mockRequestObject = new TestServiceRequest();
    private Object[] mockArray = {mockRequestObject};

    
    private List<Message> detailedMsg = new ArrayList<Message>();

	@Before
	public void setUp() throws Exception {
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
	    
		httpServletRequest.setContentType("multipart/form-data");
	   	final MockPart userData = new MockPart("userData", "userData", "{\"name\":\"test aida\"}".getBytes());
		httpServletRequest.addPart(userData);

		httpServletRequest.addHeader("TestHeader", "TestValue");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest, httpServletResponse));
		
		try{

		}catch(Throwable e) {

		}
		RestProviderHttpResponseCodeAspectLogAppender.events.clear();
		restProviderLog.setLevel(Level.DEBUG);
		try {
			when(proceedingJoinPoint.getArgs()).thenReturn(mockArray);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());
			
			Message msg = new Message(MessageSeverity.FATAL,"FatalKey","Fatal Message");
			detailedMsg.add(msg);
			when(proceedingJoinPoint.proceed()).thenReturn(responseEntity);
			when(responseEntity.getBody()).thenReturn(serviceResponse);
			when(serviceResponse.getMessages()).thenReturn(detailedMsg);
		}catch(Throwable e) {
			
		}

	}

	@After
	public void tearDown() throws Exception {
		RestProviderHttpResponseCodeAspectLogAppender.events.clear();
		restProviderLog.setLevel(Level.DEBUG);
	}
	
	@Test
	public void testMultipartFormData() {
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
	    
		httpServletRequest.setContentType("multipart/form-data");
	   	final MockPart userData = new MockPart("userData", "userData", "{\"name\":\"test aida\"}".getBytes());
		httpServletRequest.addPart(userData);
		httpServletRequest.addHeader("TestHeader", "TestValue");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest, httpServletResponse));
		
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenReturn(serviceResponse);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());			
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());
			
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable throwable) {

		}
		assertNotNull(returnObject);
	}

	@Test
	public void testMultipartmixed() {
		
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
	    
		httpServletRequest.setContentType("multipart/mixed");
	   	final MockPart userData = new MockPart("userData", "userData", "{\"name\":\"test aida\"}".getBytes());
		httpServletRequest.addPart(userData);
		httpServletRequest.addHeader("TestHeader", "TestValue");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest, httpServletResponse));
		
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenReturn(serviceResponse);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());			
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());
			
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable throwable) {

		}
		assertNotNull(returnObject);
	}
	@Test
	public void testServiceResponseReturnType() {
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenReturn(serviceResponse);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());			
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());
			
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable throwable) {

		}
		assertNotNull(returnObject);
	}
	
	
	@Test
	public void testServiceResponseReturnTypes() {
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			ServiceResponse serviceResp = new ServiceResponse();
			serviceResp.addMessage(MessageSeverity.FATAL, "Test KEY", "Test Error");
			when(proceedingJoinPoint.proceed()).thenReturn(serviceResp);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());			
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());
			
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable throwable) {

		}
		assertNotNull(returnObject);
	}
	
	@Test
	public void testConstructorWithParam() {
		MessagesToHttpStatusRulesEngine ruleEngine = new MessagesToHttpStatusRulesEngine();
		ruleEngine.addRule(
				new MessageSeverityMatchRule(MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR)); 
		ruleEngine.addRule(
				new MessageSeverityMatchRule(MessageSeverity.ERROR, 
						HttpStatus.BAD_REQUEST));
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect(ruleEngine);
		assertNotNull(restProviderHttpResponseCodeAspect);
		
	}
	
	@Test
	public void testAroundAdvice() {
		try {
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			assertNull(restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint));
		}catch(Throwable e) {
			
		}

	}
	
	@Test
	public void testAroundAdviceCatchAscentExceptionLogging()  {
		
		restProviderLog.setLevel(Level.ERROR);
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenThrow(new AscentRuntimeException());
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());			
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());
			
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable throwable) {

		}
		assertTrue(((ServiceResponse) returnObject).getMessages().size() > 0);
		assertEquals("RestProviderHttpResponseCodeAspect encountered uncaught exception in REST endpoint.",
				RestProviderHttpResponseCodeAspectLogAppender.events.get(0).getMessage());
	}
	
	@Test
	public void testAroundAdviceCatchExceptionLogging()  {
		restProviderLog.setLevel(Level.ERROR);
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenThrow(new Throwable("Unit Test Throwable converted to AscentRuntimException"));
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());			
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable throwable) {

		}
		
		assertTrue(((ServiceResponse) returnObject).getMessages().size() > 0);
		assertEquals("RestProviderHttpResponseCodeAspect encountered uncaught exception in REST endpoint.",
				RestProviderHttpResponseCodeAspectLogAppender.events.get(0).getMessage());
		assertEquals("gov.va.ascent.framework.exception.AscentRuntimeException", RestProviderHttpResponseCodeAspectLogAppender.events.get(0).getThrowableProxy().getClassName());
	}
	
	@Test
	public void testAnnotatedMethodRequestResponse() {
		Object obj;
		try {
			when(proceedingJoinPoint.proceed()).thenReturn(serviceResponse);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myAnnotatedMethod());			
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());
			
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			obj = restProviderHttpResponseCodeAspect.logAnnotatedMethodRequestResponse(proceedingJoinPoint);
			assertNotNull(obj);
		} catch (Throwable throwable) {
			assertTrue(throwable instanceof RuntimeException);
		}
	}

	@Test
	public void testAnnotatedMethodRequestResponseRunTimeException() {

		try {
			Object[] array = {null, new Object()};
			when(proceedingJoinPoint.getArgs()).thenReturn(array);
			when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Unit Test Exception"));
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			restProviderHttpResponseCodeAspect.logAnnotatedMethodRequestResponse(proceedingJoinPoint);
		} catch(Throwable throwable){
			assertTrue(throwable instanceof RuntimeException);
		}

	}

	@Test
	public void testAnnotatedMethodRequestResponseRunTimeExceptionArrayZero() {

		try {
			Object[] array = new Object[0];
			when(proceedingJoinPoint.getArgs()).thenReturn(array);
			when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Unit Test Exception"));
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			restProviderHttpResponseCodeAspect.logAnnotatedMethodRequestResponse(proceedingJoinPoint);
		} catch(Throwable throwable){
			assertTrue(throwable instanceof RuntimeException);
		}

	}

    public Method myMethod() throws NoSuchMethodException{
        return getClass().getDeclaredMethod("someMethod");
    }
    
    public void someMethod() {
        // do nothing
    }
    
    public Method myAnnotatedMethod() throws NoSuchMethodException{
        return getClass().getDeclaredMethod("annotatedMethod");
    }
    
    @Auditable(event = AuditEvents.REQUEST_RESPONSE, activity = "testActivity")
    public void annotatedMethod() {
    	 // do nothing
    }
    
	class TestClass {
		
	}
}

class TestServiceRequest extends ServiceRequest {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }	
}
