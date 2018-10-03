package gov.va.ascent.framework.audit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import gov.va.ascent.framework.security.PersonTraits;

/**
 * Audit Logger Test class that shows how to hook logback with mockito to see
 * the logging activity
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
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
		Method method = AuditLoggerTest.class.getMethod("auditDebug", null);
		AuditLogger.debug(
				new AuditEventData(AuditEvents.REQUEST_RESPONSE,
						method.getName(),
						method.getDeclaringClass().getName()),
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
		Method method = AuditLoggerTest.class.getMethod("auditInfo", null);
		AuditLogger.info(
				new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
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
		Method method = AuditLoggerTest.class.getMethod("auditWarn", null);
		AuditLogger.warn(
				new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
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
		Method method = AuditLoggerTest.class.getMethod("auditError", null);
		AuditLogger.error(
				new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
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

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void auditErrorPersonTraits() throws NoSuchMethodException, SecurityException {
		// given and when
		PersonTraits personTraits = new PersonTraits("user", "password",
				AuthorityUtils.createAuthorityList("ROLE_TEST"));
		Authentication auth = new UsernamePasswordAuthenticationToken(personTraits,
				personTraits.getPassword(), personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		Method method = AuditLoggerTest.class.getMethod("auditError", null);
		AuditLogger.error(
				new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
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

	@SuppressWarnings("unchecked")
	@Test
	public void largeMessage() throws NoSuchMethodException, SecurityException {
		// docker max message size including JSON formatting and AuditEventData is 16374
		String largeMessage = StringUtils.repeat("test ", 3275); // 5 * 3275 = 16 KB message

		Method method = AuditLoggerTest.class.getMethod("largeMessage", null);
		AuditEventData eventData =
				new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName());
		AuditLogger.info(eventData, largeMessage);

		// Now verify our logging interactions
		verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());

		List<LoggingEvent> events = captorLoggingEvent.getAllValues();
		for (LoggingEvent event : events) {
			assertThat(event.getFormattedMessage(), org.hamcrest.CoreMatchers.startsWith("SPLIT LOG SEQ#"));
			assertTrue(event.getFormattedMessage().length() < 16374);
		}
	}
}
