package gov.va.ascent.framework.audit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * Audit Logger Test class that shows how to hook logback with mockito to see
 * the logging activity
 */
@RunWith(MockitoJUnitRunner.class)
public class AuditLoggerTest {

	@SuppressWarnings("rawtypes")
	@Mock
	private Appender mockAppender;
	// Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	// added the mockAppender to the root logger
	@SuppressWarnings("unchecked")
	// It's not quite necessary but it also shows you how it can be done
	@Before
	public void setup() {
		final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(mockAppender);
	}

	// Always have this teardown otherwise we can stuff up our expectations.
	// Besides, it's
	// good coding practice
	@SuppressWarnings("unchecked")
	@After
	public void teardown() {
		final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.detachAppender(mockAppender);
	}

	@Test
	public void auditLoggerConstructor() throws Exception {
		Constructor<AuditLogger> auditLogger = AuditLogger.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(auditLogger.getModifiers()));
		auditLogger.setAccessible(true);
		auditLogger.newInstance((Object[]) null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditDebug() throws NoSuchMethodException, SecurityException {
		// given
		AuditLogger.debug(
				BaseAuditAspect.getDefaultAuditableInstance(AuditLoggerTest.class.getMethod("auditDebug", null)),
				"Audit DEBUG Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.DEBUG));
		assertThat(loggingEvent.getFormattedMessage(), is("Audit DEBUG Activity Detail"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditInfo() throws NoSuchMethodException, SecurityException {
		// given
		AuditLogger.info(
				BaseAuditAspect.getDefaultAuditableInstance(AuditLoggerTest.class.getMethod("auditInfo", null)),
				"Audit INFO Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.INFO));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), is("Audit INFO Activity Detail"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditWarn() throws NoSuchMethodException, SecurityException {
		// given
		AuditLogger.warn(
				BaseAuditAspect.getDefaultAuditableInstance(AuditLoggerTest.class.getMethod("auditWarn", null)),
				"Audit WARN Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println(ReflectionToStringBuilder.toString(loggingEvent.getMDCPropertyMap().values()));
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.WARN));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), is("Audit WARN Activity Detail"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditError() throws NoSuchMethodException, SecurityException {
		// given and when
		AuditLogger.error(
				BaseAuditAspect.getDefaultAuditableInstance(AuditLoggerTest.class.getMethod("auditError", null)),
				"Audit ERROR Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.ERROR));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), is("Audit ERROR Activity Detail"));
	}
}
