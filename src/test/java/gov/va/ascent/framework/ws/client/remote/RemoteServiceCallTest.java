package gov.va.ascent.framework.ws.client.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ascent.framework.transfer.AbstractTransferObject;
import gov.va.ascent.framework.ws.client.remote.test.mocks.TestAbstractRemoteServiceCallMockRequest;
import gov.va.ascent.framework.ws.client.remote.test.mocks.TestAbstractRemoteServiceCallMockResponse;

public class RemoteServiceCallTest {

	@Test
	public void testCallRemoteService() {

		try {
			TestClassSimple test = new TestClassSimple();
			assertNotNull(test);

			AbstractTransferObject testResponse = test.callRemoteService(new WebServiceTemplate(),
					new TestAbstractRemoteServiceCallMockRequest(), TestAbstractRemoteServiceCallMockRequest.class);
			assertNotNull(testResponse);
			assertTrue(TestAbstractRemoteServiceCallMockResponse.class.isAssignableFrom(testResponse.getClass()));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not instantiate an implementation of RemoteServiceCall interface");
		}
	}

	@Test
	public void testCallRemoteService_WithException() {

		try {
			TestClassException test = new TestClassException();
			test.callRemoteService(new WebServiceTemplate(), new TestAbstractRemoteServiceCallMockRequest(),
					TestAbstractRemoteServiceCallMockRequest.class);
			fail("RemoteServiceCall_UnitTest.testCallRemoteService_WithException() did not throw exception as intended.");
		} catch (ArithmeticException e) {
			// no-op, exception thrown as expected
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not instantiate an implementation of RemoteServiceCall interface");
		}
	}

	class TestClassSimple implements RemoteServiceCall {

		public final static String TEST_BEAN_NAME = "test.RemoteServiceCallTest.RemoteServiceCall";

		@Override
		public AbstractTransferObject callRemoteService(WebServiceTemplate webserviceTemplate, AbstractTransferObject request,
				Class<? extends AbstractTransferObject> requestClass) {
			return new TestAbstractRemoteServiceCallMockResponse();
		}

	}

	class TestClassException implements RemoteServiceCall {

		@Override
		public AbstractTransferObject callRemoteService(WebServiceTemplate webserviceTemplate, AbstractTransferObject request,
				Class<? extends AbstractTransferObject> requestClass) {
			throw new ArithmeticException();
		}

	}

}
