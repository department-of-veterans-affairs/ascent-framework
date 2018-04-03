package gov.va.ascent.framework.ws.client.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ascent.framework.audit.RequestResponseAuditData;
import gov.va.ascent.framework.audit.RequestResponseLogSerializer;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.transfer.AbstractTransferObject;
import gov.va.ascent.framework.ws.client.remote.test.mocks.TestAbstractRemoteServiceCallMockRequest;
import gov.va.ascent.framework.ws.client.remote.test.mocks.TestAbstractRemoteServiceCallMockResponse;

@RunWith(MockitoJUnitRunner.class)
public class RemoteServiceCallAspectTest {

	private static final String REQUEST_KEY_VALUE = "abstract-remote-service-call-mock-data";
	private static final String RESPONSE_VALUE = "some-value";

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	@Mock
	private MethodSignature signature;

	@Mock
	private JoinPoint.StaticPart staticPart;

	@Mock
	private RemoteServiceCallAspect mockRemoteServiceCallAspect;

	private TestAbstractRemoteServiceCallMockRequest request;
	private TestAbstractRemoteServiceCallMockResponse response;

	@Spy
	private WebServiceTemplate webserviceTemplate;

	private Object[] argValues;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Throwable {
		// make request & response from mock transfer objects
		request = new TestAbstractRemoteServiceCallMockRequest();
		request.setSomeKeyVariable(REQUEST_KEY_VALUE);

		response = new TestAbstractRemoteServiceCallMockResponse();
		response.setSomeData(RESPONSE_VALUE);

		// mock the joinpoint arguments
		argValues = new Object[3];
		argValues[0] = webserviceTemplate;
		argValues[1] = request;
		argValues[2] = request.getClass();

		// modify behavior of spied webservice template when it executes marshalSendAndReceive
		doReturn(response).when(webserviceTemplate).marshalSendAndReceive(request);

		// modify behavior of mocked elements within the aspect itself
		//		doNothing().when(mockRemoteServiceCallAspect).writeAudit(isA(MessageSeverity.class), isA(Method.class), isA(RequestResponseAuditData.class));
		doCallRealMethod().when(mockRemoteServiceCallAspect).aroundAdvice(any(ProceedingJoinPoint.class), any(WebServiceTemplate.class),
				any(AbstractTransferObject.class), any(Class.class)); // any(Class.class) should have <? extends AbstractTransferObject> but wildcards not allowed
		when(proceedingJoinPoint.getArgs()).thenReturn(argValues);
		// allows the aspect's writeAudit method to work
		when(proceedingJoinPoint.getStaticPart()).thenReturn(staticPart);
		when(staticPart.getSignature()).thenReturn(signature);
		when(signature.getMethod()).thenReturn(auditingMethod());
	}

	@Test
	public void testAroundAdvice() {
		try {
			assertNotNull(mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, webserviceTemplate, request, request.getClass()));
			assertNull(((TestAbstractRemoteServiceCallMockRequest) mockRemoteServiceCallAspect.aroundAdvice(
					proceedingJoinPoint, webserviceTemplate, request,
					request.getClass())).getSomeKeyVariable());

		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail("FAIL mockRemoteServiceCallAspect.aroundAdvice() threw exception " + throwable.getMessage());
		}
	}

	@Test
	public void testAroundAdvice_WithMissingArgs() throws Throwable {
		/*
		 * Test null args
		 */

		argValues[0] = webserviceTemplate;
		argValues[1] = request;
		argValues[2] = null;
		try {
			mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, webserviceTemplate, request, null);
		} catch (Throwable throwable) {
			assertTrue(IllegalArgumentException.class.isAssignableFrom(throwable.getClass()));
		}

		argValues[0] = webserviceTemplate;
		argValues[1] = null;
		argValues[2] = request.getClass();
		try {
			mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, webserviceTemplate, null, request.getClass());
		} catch (Throwable throwable) {
			assertTrue(IllegalArgumentException.class.isAssignableFrom(throwable.getClass()));
		}

		argValues[0] = null;
		argValues[1] = request;
		argValues[2] = request.getClass();
		try {
			mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, null, request, request.getClass());
		} catch (Throwable throwable) {
			assertTrue(IllegalArgumentException.class.isAssignableFrom(throwable.getClass()));
		}

		/*
		 * Test completely not there args
		 */

		argValues = new Object[2];
		argValues[0] = webserviceTemplate;
		argValues[1] = request;
		try {
			mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, webserviceTemplate, request, null);
		} catch (Throwable throwable) {
			assertTrue(IllegalArgumentException.class.isAssignableFrom(throwable.getClass()));
		}

		argValues = new Object[1];
		argValues[0] = webserviceTemplate;
		try {
			mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, webserviceTemplate, null, request.getClass());
		} catch (Throwable throwable) {
			assertTrue(IllegalArgumentException.class.isAssignableFrom(throwable.getClass()));
		}

		argValues = new Object[0];
		try {
			mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, webserviceTemplate, null, request.getClass());
		} catch (Throwable throwable) {
			assertTrue(IllegalArgumentException.class.isAssignableFrom(throwable.getClass()));
		}

		argValues = new Object[3];
		argValues[0] = webserviceTemplate;
		argValues[1] = request;
		argValues[2] = request.getClass();
		try {
			mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, null, request, request.getClass());
		} catch (Throwable throwable) {
			assertTrue(IllegalArgumentException.class.isAssignableFrom(throwable.getClass()));
		}

		/*
		 * Reset args to valid array
		 */
		// mock the joinpoint arguments
		argValues = new Object[3];
		argValues[0] = webserviceTemplate;
		argValues[1] = request;
		argValues[2] = request.getClass();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAroundAdvice_WithJoinpointProceedReturningNull() throws Throwable {
		argValues[0] = webserviceTemplate;
		argValues[1] = request;
		argValues[2] = request.getClass();

		// mock the method that the Aspect uses to execute RemoteSeviceCall[Mock|Impl].callRemoteService(..)
		when(proceedingJoinPoint.proceed()).thenReturn(null);

		AbstractTransferObject abstractTransferObject = null;
		try {
			abstractTransferObject = (AbstractTransferObject) mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, null, request, request.getClass());
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail("FAIL RemoteServiceCallAspect.aroundAdvice(...) unexpectdely threw exception when joinPoint.proceed() intentionally returned null.");
		}

		assertNotNull(abstractTransferObject);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAroundAdvice_WithFailedJoinpointProceed() throws Throwable {
		argValues[0] = webserviceTemplate;
		argValues[1] = request;
		argValues[2] = request.getClass();

		// mock the method that the Aspect uses to execute RemoteSeviceCall[Mock|Impl].callRemoteService(..)
		when(proceedingJoinPoint.proceed()).thenThrow(ArithmeticException.class);

		try {
			mockRemoteServiceCallAspect.aroundAdvice(proceedingJoinPoint, null, request, request.getClass());
			fail("FAIL RemoteServiceCallAspect.aroundAdvice(...) failed to generate an exception in joinPoint.proceed() as intended.");
		} catch (ArithmeticException e) {
			// no-op, this exception is expected per above mock
			e.printStackTrace();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			assertTrue(IllegalArgumentException.class.isAssignableFrom(throwable.getClass()));
		}
	}

	@Test
	public void testGetRequestResponseLogSerializer() {
		doCallRealMethod().when(mockRemoteServiceCallAspect).getRequestResponseLogSerializer();
		assertNull(mockRemoteServiceCallAspect.getRequestResponseLogSerializer());
	}

	@Test
	public void testWriteAudit() throws NoSuchMethodException {
		RequestResponseAuditData auditDataObject = new RequestResponseAuditData();
		auditDataObject.setRequest(Arrays.asList(request));
		auditDataObject.setResponse(response);

		doCallRealMethod().when(mockRemoteServiceCallAspect).writeAudit(isA(MessageSeverity.class), isA(Method.class), isA(RequestResponseAuditData.class));
		RequestResponseLogSerializer mockRequestResponseLogSerializer = mock(RequestResponseLogSerializer.class);
		doReturn(mockRequestResponseLogSerializer).when(mockRemoteServiceCallAspect).getRequestResponseLogSerializer();

		/*
		 * Test ERROR / FATAL audit logging
		 */

		try {
			mockRemoteServiceCallAspect.writeAudit(MessageSeverity.ERROR, this.auditingMethod(), auditDataObject);
		} catch (Throwable e) {
			fail("FAIL RemoteServiceCallAspect.writeAudit unexpectedly threw an exception");
			e.printStackTrace();
		}

		try {
			mockRemoteServiceCallAspect.writeAudit(MessageSeverity.FATAL, this.auditingMethod(), auditDataObject);
		} catch (Throwable e) {
			fail("FAIL RemoteServiceCallAspect.writeAudit unexpectedly threw an exception");
			e.printStackTrace();
		}

		/*
		 * Test INFO or lower audit logging
		 */

		try {
			mockRemoteServiceCallAspect.writeAudit(MessageSeverity.INFO, this.auditingMethod(), auditDataObject);
		} catch (Throwable e) {
			fail("FAIL RemoteServiceCallAspect.writeAudit unexpectedly threw an exception");
			e.printStackTrace();
		}
	}

	/** required for Audit log events */
	public Method auditingMethod() throws NoSuchMethodException{
		return getClass().getDeclaredMethod("someResponseMethod", String.class);
	}

	/** required to make auditingMethod() work */
	public String someResponseMethod(String irrelevant) {
		return irrelevant;
	}
}