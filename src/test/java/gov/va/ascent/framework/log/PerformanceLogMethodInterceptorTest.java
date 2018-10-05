package gov.va.ascent.framework.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.event.Level;

import gov.va.ascent.framework.AbstractBaseLogTester;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceLogMethodInterceptorTest extends AbstractBaseLogTester {

	/** The underlying logger of AscentLogger */
	private AscentLogger LOG = super.getLogger(PerformanceLogMethodInterceptorTest.class);

	@Mock
	MethodInvocation invocation;

	PerformanceLogMethodInterceptor performanceLogMethodInterceptor = new PerformanceLogMethodInterceptor();

	@Before
	public void setUp() {
		performanceLogMethodInterceptor = new PerformanceLogMethodInterceptor();
	}

	@Override
	@After
	public void tearDown() {
	}

	@Test
	public void testInvokeDebug() throws Throwable {
		super.getAppender().clear();

		mockInvocationOf("getString", null);
		assertEquals("enter [Helper.getString]", super.getAppender().get(0).getMessage());
		assertEquals(ch.qos.logback.classic.Level.DEBUG, super.getAppender().get(0).getLevel());
		assertTrue(super.getAppender().get(1).getMessage().contains("exit [Helper.getString] in elapsed time ["));
		assertEquals(ch.qos.logback.classic.Level.DEBUG, super.getAppender().get(1).getLevel());
		assertTrue(2 == super.getAppender().size());

	}

	@Test
	public void testInvokeInfo() throws Throwable {
		super.getAppender().clear();
		LOG.setLevel(Level.INFO);

		mockInvocationOf("getString", null);
		assertTrue(super.getAppender().get(0).getMessage().contains("exit [Helper.getString] in elapsed time ["));
		assertEquals(ch.qos.logback.classic.Level.INFO, super.getAppender().get(0).getLevel());
		assertTrue(1 == super.getAppender().size());

	}

	@Test
	public void testInvokeGreaterThanWarningThreshold() throws Throwable {
		super.getAppender().clear();
		LOG.setLevel(Level.INFO);

		performanceLogMethodInterceptor.setWarningThreshhold(-1);
		mockInvocationOf("getString", null);
		assertTrue(super.getAppender().get(0).getMessage()
				.contains("PERFORMANCE WARNING response for [Helper.getString] in elapsed time ["));
		assertEquals(ch.qos.logback.classic.Level.WARN, super.getAppender().get(0).getLevel());
		assertTrue(1 == super.getAppender().size());

	}

	@Test
	public void testInvokeWarningThresholdOnMethod() throws Throwable {
		super.getAppender().clear();
		LOG.setLevel(Level.INFO);

		Map<String, Integer> classMethodThresholds = new HashMap<>();
		classMethodThresholds.put("Helper.getString", -1);
		performanceLogMethodInterceptor.setClassAndMethodSpecificWarningThreshold(classMethodThresholds);
		mockInvocationOf("getString", null);
		assertTrue(super.getAppender().get(0).getMessage()
				.contains("PERFORMANCE WARNING response for [Helper.getString] in elapsed time ["));
		assertEquals(ch.qos.logback.classic.Level.WARN, super.getAppender().get(0).getLevel());
		assertTrue(1 == super.getAppender().size());
		assertEquals(new Integer(1500), performanceLogMethodInterceptor.getWarningThreshhold());

	}

	private MethodInvocation mockInvocationOf(String methodName, Object returnValue) throws Throwable {

		when(invocation.getMethod()).thenReturn(Helper.class.getMethod(methodName));
		when(performanceLogMethodInterceptor.invoke(invocation)).thenReturn(returnValue);

		return invocation;
	}

	interface Helper {

		String getString();

	}

}
