package gov.va.ascent.framework.exception;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.event.Level;
import org.springframework.test.context.junit4.SpringRunner;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;

@RunWith(SpringRunner.class)
public class ExceptionHandlingUtilsTest {

	@SuppressWarnings("rawtypes")
	@Mock
	private ch.qos.logback.core.Appender mockAppender;

	// Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
	@Captor
	private ArgumentCaptor<ch.qos.logback.classic.spi.LoggingEvent> captorLoggingEvent;

	AscentLogger logger = AscentLoggerFactory.getLogger(ExceptionHandlingUtilsTest.class);

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		logger.setLevel(Level.DEBUG);
		logger.getLoggerBoundImpl().addAppender(mockAppender);
	}

	@After
	public void tearDown() {

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLoggingUtils() throws Exception {
		// setup
		Object[] args = new Object[2];
		args[0] = "Arg One";
		args[1] = 42L;

		Level originalLevel = logger.getLevel();
		logger.setLevel(Level.WARN);

		ExceptionHandlingUtils.logException("Catcher", this.getClass().getMethod("testLoggingUtils"), args,
				new Throwable("test throw"));
		verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
		final List<ch.qos.logback.classic.spi.LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
		Assert.assertTrue(loggingEvents.get(0).toString().startsWith(
				"[WARN] Catcher caught exception, handling it as configured.  Here are details [java.lang.Throwable thrown by gov.va.ascent.framework.exception.ExceptionHandlingUtilsTest.testLoggingUtils] args [[Arg One, 42]]."));
		Assert.assertEquals("java.lang.Throwable", loggingEvents.get(0).getThrowableProxy().getClassName());
		Assert.assertEquals(ch.qos.logback.classic.Level.WARN, loggingEvents.get(0).getLevel());

		logger.setLevel(originalLevel);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLoggingWarnOff() throws Exception {
		final AscentLogger logger = AscentLoggerFactory.getLogger(ExceptionHandlingUtilsTest.class);
		Level originalLevel = logger.getLevel();
		logger.setLevel(Level.ERROR);

		ExceptionHandlingUtils.logException("Catcher", myMethod(), null, new Throwable("test throw"));
		verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
		final List<ch.qos.logback.classic.spi.LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();

		Assert.assertEquals(
				"[ERROR] Catcher caught exception, handling it as configured.  Here are details [java.lang.Throwable thrown by gov.va.ascent.framework.exception.ExceptionHandlingUtilsTest.someMethod] args [null].",
				loggingEvents.get(0).toString());
		Assert.assertEquals(ch.qos.logback.classic.Level.ERROR, loggingEvents.get(0).getLevel());

		logger.setLevel(originalLevel);
	}

	public Method myMethod() throws NoSuchMethodException {
		return getClass().getDeclaredMethod("someMethod");
	}

	public void someMethod() {
		// do nothing
	}

}
