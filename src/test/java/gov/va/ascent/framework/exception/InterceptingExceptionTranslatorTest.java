package gov.va.ascent.framework.exception;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.AbstractBaseLogTester;

public class InterceptingExceptionTranslatorTest extends AbstractBaseLogTester {

	@Rule
	public ExpectedException exceptions = ExpectedException.none();

	private Logger LOG = super.getLogger(InterceptingExceptionTranslator.class);

	@Test
	public void testAscentRunTimeExceptionDefault() throws Exception {
		InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();
		interceptingExceptionTranslator.setDefaultExceptionType(AscentRuntimeException.class);

		Throwable throwable = new Throwable("Cause Unit Test");

		exceptions.expect(AscentRuntimeException.class);
		exceptions.expectMessage("Unit Test");
		exceptions.expectCause(Matchers.<Throwable> equalTo(throwable));

		interceptingExceptionTranslator.afterThrowing(this.getClass().getMethod("testAscentRunTimeExceptionDefault"), null, null,
				throwable);
	}

	@Test
	public void testAscentRunTimeExceptionMapNullAndDefaultExceptionTypeNull() throws Exception {
		super.getAppender().clear();

		InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();

		Throwable throwable = new Throwable("Cause Unit Test");

		interceptingExceptionTranslator.afterThrowing(
				this.getClass().getMethod("testAscentRunTimeExceptionMapNullAndDefaultExceptionTypeNull"), null, null, throwable);

		Assert.assertEquals("InterceptingExceptionTranslator caught exception, handling it as configured."
				+ "  Here are details [java.lang.Throwable thrown by gov.va.ascent.framework.exception."
				+ "InterceptingExceptionTranslatorTest.testAscentRunTimeExceptionMapNullAndDefaultExceptionTypeNull]"
				+ " args [null].", super.getAppender().get(0).getMessage());
	}

	@Test
	public void testExcludeThrowableDebugStatementException() throws Exception {
		super.getAppender().clear();

		InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();
		interceptingExceptionTranslator.setDefaultExceptionType(RuntimeException.class);

		Map<String, Class<? extends RuntimeException>> exceptionMap = new HashMap<>();
		exceptionMap.put("AscentRuntimeException", RuntimeException.class);
		interceptingExceptionTranslator.setExceptionMap(exceptionMap);

		Set<String> exclusion = new HashSet<>();
		exclusion.add("java.lang.Throwable");
		interceptingExceptionTranslator.setExclusionSet(exclusion);

		Throwable throwable = new Throwable("Cause Unit Test");

		interceptingExceptionTranslator.afterThrowing(this.getClass().getMethod("testExcludeThrowableDebugStatementException"), null,
				null, throwable);

		Assert.assertEquals("Exception translator caught exception [class java.lang.Throwable]"
				+ " however per configuration not translating this exception.", super.getAppender().get(0).getMessage());

	}

	@Test
	public void testExcludeThrowableExceptionNoDebugStatement() throws Exception {
		super.getAppender().clear();

		// Setup
		LOG.setLevel(Level.ERROR);
		InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();
		interceptingExceptionTranslator.setDefaultExceptionType(RuntimeException.class);

		Set<String> exclusion = new HashSet<>();
		// Test OR statement by matching on package name
		exclusion.add("java.lang");
		interceptingExceptionTranslator.setExclusionSet(exclusion);
		Throwable throwable = new Throwable("Cause Unit Test");

		// Test
		interceptingExceptionTranslator.afterThrowing(this.getClass().getMethod("testExcludeThrowableExceptionNoDebugStatement"), null,
				null, throwable);

		// Nothing should have been logged because exception was excluded
		Assert.assertTrue(super.getAppender().isEmpty());

	}

	@Test
	public void testResolvableException() throws Exception {
		InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();
		interceptingExceptionTranslator.setDefaultExceptionType(RuntimeException.class);
		Map<String, Class<? extends RuntimeException>> exceptionMap = new HashMap<>();
		exceptionMap.put("java.lang.RuntimeException", AscentRuntimeException.class);

		interceptingExceptionTranslator.setExceptionMap(exceptionMap);

		Throwable throwable = new RuntimeException("Cause Unit Test");

		exceptions.expect(AscentRuntimeException.class);
		exceptions.expectCause(Matchers.<Throwable> equalTo(throwable));

		interceptingExceptionTranslator.afterThrowing(this.getClass().getMethod("testResolvableException"), null, null, throwable);

	}
}
