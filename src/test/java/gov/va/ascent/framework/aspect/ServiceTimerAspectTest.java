package gov.va.ascent.framework.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.service.ServiceTimerAspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTimerAspectTest {

	private Logger AspectLoggingLOG = (Logger) org.slf4j.LoggerFactory.getLogger(ServiceTimerAspectTest.class);
	private Logger AspectLoggingTestLOG = (Logger) org.slf4j.LoggerFactory.getLogger(ServiceTimerAspectTest.class);

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	@Mock
	private MethodSignature signature;

	@Mock
	private JoinPoint.StaticPart staticPart;

	@Before
	public void setup() throws Throwable {
		AspectLoggingLOG.setLevel(Level.DEBUG);
		AspectLoggingTestLOG.setLevel(Level.DEBUG);
		when(proceedingJoinPoint.toLongString()).thenReturn("ProceedingJoinPointLongString");
		when(proceedingJoinPoint.getStaticPart()).thenReturn(staticPart);
		when(staticPart.getSignature()).thenReturn(signature);
		when(signature.getMethod()).thenReturn(myMethod());
	}

	@After
	public void tearDown() {
		AspectLoggingTestAppender.events.clear();
		AspectLoggingLOG.setLevel(Level.DEBUG);
		AspectLoggingTestLOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testAroundAdviceDebugOn() throws Throwable {
		ServiceTimerAspect serviceTimerAspect = new ServiceTimerAspect();
		serviceTimerAspect.aroundAdvice(proceedingJoinPoint);

		assertEquals("PerformanceLoggingAspect executing around method:ProceedingJoinPointLongString",
				AspectLoggingTestAppender.events.get(0).getMessage());
		assertEquals("enter [ServiceTimerAspectTest.someMethod]", AspectLoggingTestAppender.events.get(1).getMessage());
		assertEquals("PerformanceLoggingAspect after method was called.",
				AspectLoggingTestAppender.events.get(2).getMessage());
		assertEquals(Level.INFO, AspectLoggingTestAppender.events.get(3).getLevel());
	}

	public Method myMethod() throws NoSuchMethodException {
		return getClass().getDeclaredMethod("someMethod");
	}

	public void someMethod() {
		// do nothing
	}
}
