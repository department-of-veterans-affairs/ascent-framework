package gov.va.ascent.framework.ws.client.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import gov.va.ascent.framework.transfer.AbstractTransferObject;
import gov.va.ascent.framework.ws.client.remote.test.mocks.TestAbstractRemoteServiceCallMockResponse;

public class BaseRemoteServiceCallAspect_UnitTest {

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	@Mock
	private MethodSignature signature;

	@Mock
	private JoinPoint.StaticPart staticPart;

	private BaseRemoteServiceCallAspect mockBaseRemoteServiceCallAspect;

	@Before
	public void setUp() throws Exception {
		mockBaseRemoteServiceCallAspect = new BaseRemoteServiceCallAspect();
	}

	@Test
	public void testBaseRemoteServiceCallAspect() {
		assertNotNull("mockBaseRemoteServiceCallAspect cannot be null", mockBaseRemoteServiceCallAspect);
	}

	@Test
	public void testStandardRemoteServiceCallMethod() {
		try {
			BaseRemoteServiceCallAspect.standardRemoteServiceCallMethod();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Threw exceptoin " + e.getMessage());
		}
	}

	@Test
	public void testGetDefaultAuditableInstance() throws NoSuchMethodException {
		BaseRemoteServiceCallAspect.getDefaultAuditableInstance(myMethod());
		BaseRemoteServiceCallAspect.getDefaultAuditableInstance(null);
	}

	public Method myMethod() throws NoSuchMethodException {
		return getClass().getDeclaredMethod("someResponseMethod",String.class);
	}

	public AbstractTransferObject someResponseMethod(String simpleParam) {
		return new TestAbstractRemoteServiceCallMockResponse();
	}
}
