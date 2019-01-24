package gov.va.ascent.framework.rest.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPart;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gov.va.ascent.framework.AbstractBaseLogTester;
import gov.va.ascent.framework.audit.AuditEvents;
import gov.va.ascent.framework.audit.Auditable;
import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.service.ServiceRequest;
import gov.va.ascent.framework.service.ServiceResponse;

@RunWith(MockitoJUnitRunner.class)
public class RestProviderHttpResponseCodeAspectTest extends AbstractBaseLogTester {

	private final AscentLogger restProviderLog = super.getLogger(RestProviderHttpResponseCodeAspect.class);

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
	private final RestProviderHttpResponseCodeAspect requestResponseAspect = new RestProviderHttpResponseCodeAspect();

	private final TestServiceRequest mockRequestObject = new TestServiceRequest();
	private final Object[] mockArray = { mockRequestObject };

	private final List<Message> detailedMsg = new ArrayList<Message>();

	@Before
	public void setUp() throws Exception {
		super.getAppender().clear();

		final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		final MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

		httpServletRequest.setContentType("multipart/form-data");
		final MockPart userData = new MockPart("userData", "userData", "{\"name\":\"test aida\"}".getBytes());
		httpServletRequest.addPart(userData);

		httpServletRequest.addHeader("TestHeader", "TestValue");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest, httpServletResponse));

		super.getAppender().clear();
		restProviderLog.setLevel(Level.DEBUG);
		try {
			when(proceedingJoinPoint.getArgs()).thenReturn(mockArray);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());

			final Message msg = new Message(MessageSeverity.FATAL, "FatalKey", "Fatal Message");
			detailedMsg.add(msg);
			when(proceedingJoinPoint.proceed()).thenReturn(responseEntity);
			when(responseEntity.getBody()).thenReturn(serviceResponse);
			when(serviceResponse.getMessages()).thenReturn(detailedMsg);
		} catch (final Throwable e) {

		}

	}

	@Override
	@After
	public void tearDown() {
	}

	@Test
	public void testMultipartFormData() {

		final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		final MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

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
		} catch (final Throwable throwable) {

		}
		assertNotNull(returnObject);
	}

	@Test
	public void testMultipartmixed() {

		final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		final MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

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
		} catch (final Throwable throwable) {

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
		} catch (final Throwable throwable) {

		}
		assertNotNull(returnObject);
	}

	@Test
	public void testServiceResponseReturnTypes() {
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			final ServiceResponse serviceResp = new ServiceResponse();
			serviceResp.addMessage(MessageSeverity.FATAL, "Test KEY", "Test Error");
			when(proceedingJoinPoint.proceed()).thenReturn(serviceResp);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());

			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (final Throwable throwable) {

		}
		assertNotNull(returnObject);
	}

	@Test
	public void testConstructorWithParam() {
		final MessagesToHttpStatusRulesEngine ruleEngine = new MessagesToHttpStatusRulesEngine();
		ruleEngine.addRule(new MessageSeverityMatchRule(MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR));
		ruleEngine.addRule(new MessageSeverityMatchRule(MessageSeverity.ERROR, HttpStatus.BAD_REQUEST));
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect(ruleEngine);
		assertNotNull(restProviderHttpResponseCodeAspect);

	}

	@Test
	public void testAroundAdvice() {
		try {
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			assertNull(restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint));
		} catch (final Throwable e) {

		}

	}

	@Test
	public void testAroundAdviceCatchAscentExceptionLogging() {
		super.getAppender().clear();

		restProviderLog.setLevel(Level.ERROR);
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenThrow(new AscentRuntimeException());
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());

			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (final Throwable throwable) {

		}
		assertTrue(((ServiceResponse) returnObject).getMessages().size() > 0);
	}

	@Test
	public void testAroundAdviceCatchExceptionLogging() {
		super.getAppender().clear();
		restProviderLog.setLevel(Level.ERROR);

		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		Object returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenThrow(new Throwable("Unit Test Throwable converted to AscentRuntimException"));
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());
			when(proceedingJoinPoint.getTarget()).thenReturn(new TestClass());
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (final Throwable throwable) {

		}

		assertTrue(((ServiceResponse) returnObject).getMessages().size() > 0);
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
		} catch (final Throwable throwable) {
			assertTrue(throwable instanceof RuntimeException);
		}
	}

	@Test
	public void testAnnotatedMethodRequestResponseRunTimeException() {

		try {
			final Object[] array = { null, new Object() };
			when(proceedingJoinPoint.getArgs()).thenReturn(array);
			when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Unit Test Exception"));
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			restProviderHttpResponseCodeAspect.logAnnotatedMethodRequestResponse(proceedingJoinPoint);
		} catch (final Throwable throwable) {
			assertTrue(throwable instanceof RuntimeException);
		}

	}

	@Test
	public void testAnnotatedMethodRequestResponseRunTimeExceptionArrayZero() {

		try {
			final Object[] array = new Object[0];
			when(proceedingJoinPoint.getArgs()).thenReturn(array);
			when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Unit Test Exception"));
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			restProviderHttpResponseCodeAspect.logAnnotatedMethodRequestResponse(proceedingJoinPoint);
		} catch (final Throwable throwable) {
			assertTrue(throwable instanceof RuntimeException);
		}

	}

	@Test
	public void testGetReturnResponse() {
		final RestProviderHttpResponseCodeAspect aspect = new RestProviderHttpResponseCodeAspect();
		Method method = null;
		Object retval = null;
		try {
			method = aspect.getClass().getDeclaredMethod("getReturnResponse", boolean.class, Object.class);
			method.setAccessible(true);
			retval = method.invoke(aspect, Boolean.TRUE, new ResponseEntity<ServiceResponse>(HttpStatus.valueOf(200)));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			fail("Should not have exception here");
		}
		assertNull(retval);

		try {
			retval = method.invoke(aspect, Boolean.FALSE, "hello");
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			fail("Should not have exception here");
		}
		assertNotNull(retval);
		assertTrue("hello".equals(retval));
	}

	public Method myMethod() throws NoSuchMethodException {
		return getClass().getDeclaredMethod("someMethod");
	}

	public void someMethod() {
		// do nothing
	}

	public Method myAnnotatedMethod() throws NoSuchMethodException {
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
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8736731329416969081L;
	private String text;

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}
}
