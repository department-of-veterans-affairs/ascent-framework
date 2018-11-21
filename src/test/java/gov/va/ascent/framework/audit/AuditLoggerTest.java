package gov.va.ascent.framework.audit;

import static gov.va.ascent.framework.log.AscentBaseLogger.MAX_TOTAL_LOG_LEN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import gov.va.ascent.framework.audit.AuditEventData;
import gov.va.ascent.framework.audit.AuditEvents;
import gov.va.ascent.framework.audit.AuditLogger;
import gov.va.ascent.framework.log.AscentBaseLogger;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
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
	private ch.qos.logback.core.Appender mockAppender;

	// Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
	@Captor
	private ArgumentCaptor<ch.qos.logback.classic.spi.LoggingEvent> captorLoggingEvent;

	// added the mockAppender to the root logger
	@SuppressWarnings("unchecked")
	// It's not quite necessary but it also shows you how it can be done
	@Before
	public void setup() {
		AscentLoggerFactory.getLogger(AscentLogger.ROOT_LOGGER_NAME).getLoggerBoundImpl().addAppender(mockAppender);
	}

	// Always have this teardown otherwise we can stuff up our expectations.
	// Besides, it's
	// good coding practice
	@SuppressWarnings("unchecked")
	@After
	public void teardown() {
		AscentLoggerFactory.getLogger(AscentLogger.ROOT_LOGGER_NAME).getLoggerBoundImpl().detachAppender(mockAppender);
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
	public void auditNullEventData() {
		AuditLogger.info(null, "test");
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		assertTrue("UNKNOWN".equals(loggingEvent.getMDCPropertyMap().get("event")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditNullEventDataEvent() {
		AuditEventData data = new AuditEventData(null, "test", this.getClass().getSimpleName());
		assertTrue(AuditEvents.UNKNOWN.equals(data.getEvent()));

		ReflectionTestUtils.setField(data, "event", null);
		assertNull(data.getEvent());

		AuditLogger.info(data, "test");
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		assertTrue("UNKNOWN".equals(loggingEvent.getMDCPropertyMap().get("event")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditNullEventDataActivity() {
		AuditEventData data = new AuditEventData(AuditEvents.SERVICE_AUDIT, null, this.getClass().getSimpleName());
		assertTrue("Unknown".equals(data.getActivity()));

		ReflectionTestUtils.setField(data, "activity", null);
		assertNull(data.getActivity());

		AuditLogger.info(data, "test");
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		assertTrue("Unknown".equals(loggingEvent.getMDCPropertyMap().get("activity")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditNullEventDataClass() {
		AuditEventData data = new AuditEventData(AuditEvents.SERVICE_AUDIT, "test", null);
		assertTrue("Unknown".equals(data.getAuditClass()));

		ReflectionTestUtils.setField(data, "auditClass", null);
		assertNull(data.getAuditClass());

		AuditLogger.info(data, "test");
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		assertTrue("Unknown".equals(loggingEvent.getMDCPropertyMap().get("audit_class")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditNullEventDataUser() {
		AuditEventData data = new AuditEventData(AuditEvents.SERVICE_AUDIT, "test", this.getClass().getSimpleName());
		assertTrue("Unknown".equals(data.getUser()));

		ReflectionTestUtils.setField(data, "user", null);
		assertNull(data.getUser());

		AuditLogger.info(data, "test");
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		assertTrue("Unknown".equals(loggingEvent.getMDCPropertyMap().get("user")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditNullEventDataTokenid() {
		AuditEventData data = new AuditEventData(AuditEvents.SERVICE_AUDIT, "test", this.getClass().getSimpleName());
		assertTrue(StringUtils.EMPTY.equals(data.getTokenId()));

		ReflectionTestUtils.setField(data, "tokenId", "123456");
		assertTrue(data.getTokenId().equals("123456"));

		AuditLogger.info(data, "test");
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		assertTrue("123456".equals(loggingEvent.getMDCPropertyMap().get("tokenId")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditDebug() throws NoSuchMethodException, SecurityException {
		// given
		Method method = AuditLoggerTest.class.getMethod("auditDebug", (Class<?>[]) null);
		AuditLogger.debug(new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
				"Audit DEBUG Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(ch.qos.logback.classic.Level.DEBUG));
		assertThat(loggingEvent.getFormattedMessage(), is("Audit DEBUG Activity Detail"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditInfo() throws NoSuchMethodException, SecurityException {
		// given
		Method method = AuditLoggerTest.class.getMethod("auditInfo", (Class<?>[]) null);
		AuditLogger.info(new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
				"Audit INFO Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(ch.qos.logback.classic.Level.INFO));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), is("Audit INFO Activity Detail"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditWarn() throws NoSuchMethodException, SecurityException {
		// given
		Method method = AuditLoggerTest.class.getMethod("auditWarn", (Class<?>[]) null);
		AuditLogger.warn(new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
				"Audit WARN Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println(ReflectionToStringBuilder.toString(loggingEvent.getMDCPropertyMap().values()));
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(ch.qos.logback.classic.Level.WARN));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), is("Audit WARN Activity Detail"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auditError() throws NoSuchMethodException, SecurityException {
		// given and when
		Method method = AuditLoggerTest.class.getMethod("auditError", (Class<?>[]) null);
		AuditLogger.error(new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
				"Audit ERROR Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(ch.qos.logback.classic.Level.ERROR));
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
		Method method = AuditLoggerTest.class.getMethod("auditError", (Class<?>[]) null);
		AuditLogger.error(
				new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName()),
				"Audit ERROR Activity Detail");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());
		// Having a genricised captor means we don't need to cast
		final ch.qos.logback.classic.spi.LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(ch.qos.logback.classic.Level.ERROR));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), is("Audit ERROR Activity Detail"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void largeMessage() throws NoSuchMethodException, SecurityException {
		// docker max message size including JSON formatting and AuditEventData is 16374
		String message = StringUtils.repeat("test ", 3275);

		Method method = AuditLoggerTest.class.getMethod("largeMessage", (Class<?>[]) null);
		AuditEventData eventData =
				new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName());
		AuditLogger.info(eventData, message);

		String stackTrace = null;
		int messageLength = message == null ? 0 : message.length();
		int stackTraceLength = 0;
		int mdcReserveLength = gov.va.ascent.framework.log.AscentBaseLogger.MDC_RESERVE_LENGTH;

		int captureCount = 0;

		AscentBaseLogger logger = AscentLoggerFactory.getLogger(AscentLogger.ROOT_LOGGER_NAME);
		if (mdcReserveLength + messageLength + stackTraceLength > MAX_TOTAL_LOG_LEN) {
			if (messageLength >= gov.va.ascent.framework.log.AscentBaseLogger.MAX_MSG_LENGTH) {
				String[] splitMessages = ReflectionTestUtils.invokeMethod(logger, "splitMessages", message);
				captureCount = splitMessages.length;
			} else {
				captureCount = 1;
			}

			if (stackTraceLength >= gov.va.ascent.framework.log.AscentBaseLogger.MAX_STACK_TRACE_TEXT_LENGTH) {
				String[] splitstackTrace = ReflectionTestUtils.invokeMethod(logger, "splitStackTraceText", stackTrace);
				captureCount = captureCount + splitstackTrace.length;
			} else if (stackTraceLength != 0) {
				captureCount = captureCount + 1;
			}

		} else {
			captureCount = 1;
		}

		// Now verify our logging interactions
		verify(mockAppender, times(captureCount)).doAppend(captorLoggingEvent.capture());

		List<ch.qos.logback.classic.spi.LoggingEvent> events = captorLoggingEvent.getAllValues();
		for (ch.qos.logback.classic.spi.LoggingEvent event : events) {
			assertThat(event.getFormattedMessage(), org.hamcrest.CoreMatchers.containsString("test "));
			assertTrue(event.getFormattedMessage().length() < 16374);
		}
	}
}
