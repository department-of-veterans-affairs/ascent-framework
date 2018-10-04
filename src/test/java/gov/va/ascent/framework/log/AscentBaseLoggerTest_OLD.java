package gov.va.ascent.framework.log;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.boot.test.rule.OutputCapture;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import gov.va.ascent.framework.AbstractBaseLogTester;

@RunWith(MockitoJUnitRunner.class)
public class AscentBaseLoggerTest_OLD extends AbstractBaseLogTester {

	private static int LEN_16KB = 16384;
	private static int LEN_14KB = 14336;
	private static final String TEST_MESSAGE = "Test log message";
	private static final String LONG_MESSAGE = StringUtils.repeat("A ", LEN_16KB / 2);

	/** The output capture, all as a string (even for multi-line output). */
	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	/** For mocking the logger appender to capture individual line output */
	@SuppressWarnings("rawtypes")
	@Mock
	private Appender mockAppender;

	/** Arg captor to capture args of calls to the appender */
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	/** Test logger class */
	class TestLogger extends AscentBaseLogger {
		public TestLogger(Logger logger) {
			super(logger);
		}
	}

	/** Attach the mock appender to the root logger */
	@SuppressWarnings("unchecked")
	private void attachMockAppender() {
		final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(mockAppender);
	}

	/** Detach the mock appender from the root logger */
	@SuppressWarnings("unchecked")
	private void detachMockAppender() {
		final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.detachAppender(mockAppender);
	}

	/** Test logger instance */
	TestLogger logger;

	@Before
	public void setUp() throws Exception {
		logger = new TestLogger(LoggerFactory.getLogger(TestLogger.class));
	}

	@Ignore
	@Test
	public final void testAscentBaseLogger() throws NoSuchMethodException, SecurityException {
		assertNotNull(logger);
	}

	@Ignore
	@Test
	public final void testLogMessageTrace() {
		logger.trace(TEST_MESSAGE);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains(TEST_MESSAGE));
		assertTrue(captured.contains(Level.DEBUG.name()));
	}

	@Ignore
	@Test
	public final void testLogMessageDebug() {
		logger.debug(TEST_MESSAGE);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains(TEST_MESSAGE));
		assertTrue(captured.contains(Level.DEBUG.name()));
	}

	@Ignore
	@Test
	public final void testLogMessageInfo() {
		logger.info(TEST_MESSAGE);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains(TEST_MESSAGE));
		assertTrue(captured.contains(Level.INFO.name()));
	}

	@Ignore
	@Test
	public final void testLogMessageWarn() {
		logger.warn(TEST_MESSAGE);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains(TEST_MESSAGE));
		assertTrue(captured.contains(Level.WARN.name()));
	}

	@Ignore
	@Test
	public final void testLogMessageError() {
		logger.error(TEST_MESSAGE);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains(TEST_MESSAGE));
		assertTrue(captured.contains(Level.ERROR.name()));
	}

	@Ignore
	@Test
	public final void testLogError() {
		logger.log(Level.ERROR, TEST_MESSAGE);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains(TEST_MESSAGE));
		assertTrue(captured.contains(Level.ERROR.name()));
	}

	@Ignore
	@Test
	public final void testLogMessageNullSeverity() {
		logger.log(null, TEST_MESSAGE);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains(TEST_MESSAGE));
		assertTrue(captured.contains(Level.DEBUG.name()));
	}

	@Ignore
	@Test
	public final void testLogMessageNullMessage() {
		logger.log(Level.INFO, null);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains("null"));
		assertTrue(captured.contains(Level.INFO.name()));
	}

	@Ignore
	@Test
	public final void testLogMessageTraceNullSeverityNullMessage() {
		logger.log(null, null);
		String captured = outputCapture.toString();
		assertNotNull(captured);
		assertTrue(captured.contains("null"));
		assertTrue(captured.contains(Level.DEBUG.name()));
	}

	@SuppressWarnings("unchecked")
	@Ignore
	@Test
	public final void testLogMessageLongMessage() {
		try {
			attachMockAppender();

			logger.log(Level.INFO, LONG_MESSAGE);

			verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
			List<LoggingEvent> events = captorLoggingEvent.getAllValues();
			for (LoggingEvent event : events) {
				assertThat(event.getFormattedMessage(), org.hamcrest.CoreMatchers.startsWith("SPLIT LOG SEQ#"));
				assertTrue(event.getFormattedMessage().length() < LEN_16KB);
			}
		} finally {
			detachMockAppender();
		}
	}
}
