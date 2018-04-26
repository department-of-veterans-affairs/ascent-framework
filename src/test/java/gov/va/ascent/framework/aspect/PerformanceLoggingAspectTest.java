package gov.va.ascent.framework.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.AbstractBaseLogTester;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceLoggingAspectTest extends AbstractBaseLogTester {

	private Logger AspectLoggingLOG = super.getLogger(PerformanceLoggingAspect.class);
	private Logger AspectLoggingTestLOG = super.getLogger(PerformanceLoggingAspectTest.class);

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	@Mock
	private MethodSignature signature;

	@Mock
	private JoinPoint.StaticPart staticPart;

	@Override
	@Before
	public void setup() throws Throwable {
		when(proceedingJoinPoint.toLongString()).thenReturn("ProceedingJoinPointLongString");
		when(proceedingJoinPoint.getStaticPart()).thenReturn(staticPart);
		when(staticPart.getSignature()).thenReturn(signature);
		when(signature.getMethod()).thenReturn(myMethod());
	}

	@Override
	@After
	public void tearDown() {
	}

	@Test
	public void testAroundAdviceDebugOn() throws Throwable {
		super.getAppender().clear();

		PerformanceLoggingAspect.aroundAdvice(proceedingJoinPoint);

		assertEquals("PerformanceLoggingAspect executing around method:ProceedingJoinPointLongString",
				super.getAppender().get(0).getMessage());
		assertEquals("enter [PerformanceLoggingAspectTest.someMethod]", super.getAppender().get(1).getMessage());
		assertEquals("PerformanceLoggingAspect after method was called.", super.getAppender().get(2).getMessage());
		assertTrue(
				super.getAppender().get(3).getMessage().contains("exit [PerformanceLoggingAspectTest.someMethod] in elapsed time ["));
		assertEquals(Level.INFO, super.getAppender().get(3).getLevel());

	}

	@Test
	public void testAroundAdviceDebugOff() throws Throwable {
		super.getAppender().clear();
		AspectLoggingLOG.setLevel(Level.INFO);
		AspectLoggingTestLOG.setLevel(Level.INFO);

		PerformanceLoggingAspect.aroundAdvice(proceedingJoinPoint);

		assertTrue(
				super.getAppender().get(0).getMessage().contains("exit [PerformanceLoggingAspectTest.someMethod] in elapsed time ["));
		assertEquals(Level.INFO, super.getAppender().get(0).getLevel());

	}

	@Test
	public void testAroundAdviceThrowError() throws Throwable {
		super.getAppender().clear();
		AspectLoggingLOG.setLevel(Level.ERROR);
		AspectLoggingTestLOG.setLevel(Level.ERROR);

		when(proceedingJoinPoint.proceed()).thenThrow(new Throwable("Unit Testing"));

		try {
			PerformanceLoggingAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable e) {
			assertEquals("PerformanceLoggingAspect encountered uncaught exception. Throwable Cause.",
					super.getAppender().get(0).getMessage());
			assertEquals(Level.ERROR, super.getAppender().get(0).getLevel());
		}

	}

	public Method myMethod() throws NoSuchMethodException {
		return getClass().getDeclaredMethod("someMethod");
	}

	public void someMethod() {
		// do nothing
	}
}
