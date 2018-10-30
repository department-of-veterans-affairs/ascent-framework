package gov.va.ascent.framework.ws.client.remote;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import gov.va.ascent.framework.AbstractBaseLogTester;
import gov.va.ascent.framework.audit.RequestResponseLogSerializer;


@RunWith(MockitoJUnitRunner.class)
public class RemoteServiceCallInterceptorTest extends AbstractBaseLogTester {


	
	@Mock
	MethodInvocation methodInvocation;
	
	@Mock
	RequestResponseLogSerializer asyncLogging;
	
	@InjectMocks
	RemoteServiceCallInterceptor remoteServiceCallInterceptor;

	@Before
	public void setUp() {
		remoteServiceCallInterceptor = new RemoteServiceCallInterceptor();
		MockitoAnnotations.initMocks(this);
	}

	@Override
	@After
	public void tearDown() {
	}

	
	@Test
	public void testInvoke() throws Throwable {
		

		Object[] args = new Object[3];
		args[0] = "0";
		args[1] = "1"; 
		args[2] = "2";
		when(methodInvocation.getArguments()).thenReturn(args);
		when(methodInvocation.getMethod()).thenReturn(Helper.class.getMethod("getString"));
	
		assertNull(remoteServiceCallInterceptor.invoke(methodInvocation));

	}
	
	@Test(expected=Exception.class)
	public void testInvokeException() throws Throwable {
		

		Object[] args = new Object[3];
		args[0] = "0";
		args[1] = "1"; 
		args[2] = "2";
		//doThrow(new Exception("Exception Test")).when(methodInvocation.proceed());
		when(methodInvocation.getArguments()).thenReturn(args);
		when(methodInvocation.getMethod()).thenReturn(Helper.class.getMethod("getString"));
		when(methodInvocation.proceed()).thenThrow(new Exception("Exception Test"));
		remoteServiceCallInterceptor.invoke(methodInvocation);
	}


	interface Helper {

		String getString();

	}


}
