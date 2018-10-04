package gov.va.ascent.framework.log;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.boot.test.rule.OutputCapture;

import com.github.lalyos.jfiglet.FigletFont;

import gov.va.ascent.framework.AbstractBaseLogTester;
import gov.va.ascent.framework.exception.AscentRuntimeException;

public class AscentLoggerTest extends AbstractBaseLogTester {

	private AscentLogger LOGGER = super.getLogger(AscentLoggerTest.class);

	private final AscentBanner banner = AscentBanner.newBanner("TEST BANNER", Level.INFO);
	private static final String MESSAGE = "Test message";
	private static final Marker MARKER = AscentLogMarkers.TEST.getMarker();
	private static final AscentRuntimeException EXCEPTION = new AscentRuntimeException("TestException");

	/** The output capture. */
	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Assert output that was directed through the console.
	 *
	 * @param level the level
	 * @param banner the banner (use @{code null} for non-banner assertions)
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void assertConsoleBanner(final Level level, final AscentBanner banner, final String message, final Exception e)
			throws IOException {
		final String outString = outputCapture.toString();

		if (banner != null) {
			String expected =
					FigletFont.convertOneLine(AscentBanner.FONT_FILE, level.name() + ": " + banner.getBannerText());
			Assert.assertTrue(StringUtils.contains(StringUtils.normalizeSpace(outString), StringUtils.normalizeSpace(expected)));
		}
		// always assert the message
		Assert.assertTrue(outString.contains(message));

		if (e != null) {
			Assert.assertTrue(outString.contains(EXCEPTION.getClass().getSimpleName()));
		}
	}

	/**
	 * Assert output that was directed through the console.
	 *
	 * @param level the level
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void assertConsole(final Level level, final String message, final Exception e) throws IOException {
		assertConsoleBanner(level, null, message, e);
	}

	/**
	 * Enable TRACE log level logging. By default TRACE is turned off.
	 * <p>
	 * ALWAYS {@link #disableTrace()} when TRACE level testing is done.
	 */
	private void enableTrace() {
		LOGGER.getLoggerBoundImpl().setLevel(ch.qos.logback.classic.Level.TRACE);
	}

	/** Disable TRACE log level logging. By default TRACE is turned off. */
	private void disableTrace() {
		LOGGER.getLoggerBoundImpl().setLevel(ch.qos.logback.classic.Level.DEBUG);
	}

//	@Test
//	public final void testGetLogger() throws IOException {
//		fail("Not yet implemented");
//	}

	@Test
	public final void testGetLoggerInterfaceImpl() {
		org.slf4j.Logger slf4j = LOGGER.getLoggerInterfaceImpl();
		assertNotNull(slf4j);
	}

	@Test
	public final void testGetLoggerBoundImpl() {
		ch.qos.logback.classic.Logger logback = LOGGER.getLoggerBoundImpl();
		assertNotNull(logback);
	}

	@Test
	public final void testDebugBannerString() throws IOException {
		banner.setLevel(Level.DEBUG);
		LOGGER.debug(banner, MESSAGE);
		assertConsoleBanner(Level.DEBUG, banner, MESSAGE, null);
	}

	@Test
	public final void testDebugBannerStringObject() throws IOException {
		banner.setLevel(Level.DEBUG);
		LOGGER.debug(banner, "{}", MESSAGE);
		assertConsoleBanner(Level.DEBUG, banner, MESSAGE, null);
	}

	@Test
	public final void testDebugBannerStringObjectObject() throws IOException {
		banner.setLevel(Level.DEBUG);
		LOGGER.debug(banner, "{} {}", MESSAGE, MESSAGE);
		assertConsoleBanner(Level.DEBUG, banner, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testDebugBannerStringObjectArray() throws IOException {
		banner.setLevel(Level.DEBUG);
		LOGGER.debug(banner, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsoleBanner(Level.DEBUG, banner, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testDebugBannerStringThrowable() throws IOException {
		banner.setLevel(Level.DEBUG);
		LOGGER.debug(banner, MESSAGE, EXCEPTION);
		assertConsoleBanner(Level.DEBUG, banner, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testInfoBannerString() throws IOException {
		banner.setLevel(Level.INFO);
		LOGGER.info(banner, MESSAGE);
		assertConsoleBanner(Level.INFO, banner, MESSAGE, null);
	}

	@Test
	public final void testInfoBannerStringObject() throws IOException {
		banner.setLevel(Level.INFO);
		LOGGER.info(banner, "{}", MESSAGE);
		assertConsoleBanner(Level.INFO, banner, MESSAGE, null);
	}

	@Test
	public final void testInfoBannerStringObjectObject() throws IOException {
		banner.setLevel(Level.INFO);
		LOGGER.info(banner, "{} {}", MESSAGE, MESSAGE);
		assertConsoleBanner(Level.INFO, banner, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testInfoBannerStringObjectArray() throws IOException {
		banner.setLevel(Level.INFO);
		LOGGER.info(banner, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsoleBanner(Level.INFO, banner, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testInfoBannerStringThrowable() throws IOException {
		banner.setLevel(Level.INFO);
		LOGGER.info(banner, MESSAGE, EXCEPTION);
		assertConsoleBanner(Level.INFO, banner, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testWarnBannerString() throws IOException {
		banner.setLevel(Level.WARN);
		LOGGER.warn(banner, MESSAGE);
		assertConsoleBanner(Level.WARN, banner, MESSAGE, null);
	}

	@Test
	public final void testWarnBannerStringObject() throws IOException {
		banner.setLevel(Level.WARN);
		LOGGER.warn(banner, "{}", MESSAGE);
		assertConsoleBanner(Level.WARN, banner, MESSAGE, null);
	}

	@Test
	public final void testWarnBannerStringObjectArray() throws IOException {
		banner.setLevel(Level.WARN);
		LOGGER.warn(banner, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsoleBanner(Level.WARN, banner, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testWarnBannerStringObjectObject() throws IOException {
		banner.setLevel(Level.WARN);
		LOGGER.warn(banner, "{} {}", MESSAGE, MESSAGE);
		assertConsoleBanner(Level.WARN, banner, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testWarnBannerStringThrowable() throws IOException {
		banner.setLevel(Level.WARN);
		LOGGER.warn(banner, MESSAGE, EXCEPTION);
		assertConsoleBanner(Level.WARN, banner, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testErrorBannerString() throws IOException {
		banner.setLevel(Level.ERROR);
		LOGGER.error(banner, MESSAGE);
		assertConsoleBanner(Level.ERROR, banner, MESSAGE, null);
	}

	@Test
	public final void testErrorBannerStringObject() throws IOException {
		banner.setLevel(Level.ERROR);
		LOGGER.error(banner, "{}", MESSAGE);
		assertConsoleBanner(Level.ERROR, banner, MESSAGE, null);
	}

	@Test
	public final void testErrorBannerStringObjectObject() throws IOException {
		banner.setLevel(Level.ERROR);
		LOGGER.error(banner, "{} {}", MESSAGE, MESSAGE);
		assertConsoleBanner(Level.ERROR, banner, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testErrorBannerStringObjectArray() throws IOException {
		banner.setLevel(Level.ERROR);
		LOGGER.error(banner, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsoleBanner(Level.ERROR, banner, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testErrorBannerStringThrowable() throws IOException {
		banner.setLevel(Level.ERROR);
		LOGGER.error(banner, MESSAGE, EXCEPTION);
		assertConsoleBanner(Level.ERROR, banner, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testGetName() throws IOException {
		String name = LOGGER.getName();
		assertNotNull(name);
		assertTrue(AscentLoggerTest.class.getName().equals(name));
	}

	@Test
	public final void testIsTraceEnabled() throws IOException {
		boolean is = LOGGER.isTraceEnabled();
		assertTrue(!is);
	}

	@Test
	public final void testTraceString() throws IOException {
		// trace is disabled
		LOGGER.trace(MESSAGE);
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testTraceStringObject() throws IOException {
		// trace is disabled
		LOGGER.trace("{}", MESSAGE);
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testTraceStringObjectObject() throws IOException {
		// trace is disabled
		LOGGER.trace("{} {}", MESSAGE, MESSAGE);
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testTraceStringObjectArray() throws IOException {
		// trace is disabled
		LOGGER.trace("{} {}", new Object[] { MESSAGE, MESSAGE });
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testTraceStringThrowable() throws IOException {
		// trace is disabled
		LOGGER.trace(MESSAGE, EXCEPTION);
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testLogString() throws IOException {
		enableTrace();
		LOGGER.log(Level.TRACE, MESSAGE);
		assertConsole(Level.TRACE, MESSAGE, null);
		disableTrace();
	}

	@Test
	public final void testLogStringObject() throws IOException {
		LOGGER.log(Level.DEBUG, "{}", MESSAGE);
		assertConsole(Level.DEBUG, MESSAGE, null);
	}

	@Test
	public final void testLogStringObjectObject() throws IOException {
		LOGGER.log(Level.INFO, "{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.INFO, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testLogStringObjectArray() throws IOException {
		LOGGER.log(Level.WARN, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.WARN, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testLogStringThrowable() throws IOException {
		LOGGER.log(Level.ERROR, MESSAGE, EXCEPTION);
		assertConsole(Level.ERROR, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testIsTraceEnabledMarker() throws IOException {
		boolean is = LOGGER.isTraceEnabled(MARKER);
		assertTrue(!is);
	}

	@Test
	public final void testTraceMarkerString() throws IOException {
		// trace is disabled
		LOGGER.trace(MARKER, MESSAGE);
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testTraceMarkerStringObject() throws IOException {
		// trace is disabled
		LOGGER.trace(MARKER, "{}", MESSAGE);
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testTraceMarkerStringObjectObject() throws IOException {
		// trace is disabled
		LOGGER.trace(MARKER, "{} {}", MESSAGE, MESSAGE);
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testTraceMarkerStringObjectArray() throws IOException {
		// trace is disabled
		LOGGER.trace(MARKER, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testTraceMarkerStringThrowable() throws IOException {
		// trace is disabled
		LOGGER.trace(MARKER, "{} {}", EXCEPTION);
		assertTrue("".equals(outputCapture.toString()));
	}

	@Test
	public final void testIsDebugEnabled() throws IOException {
		boolean is = LOGGER.isDebugEnabled();
		assertTrue(is);
	}

	@Test
	public final void testDebugString() throws IOException {
		LOGGER.debug(MESSAGE);
		assertConsole(Level.DEBUG, MESSAGE, null);
	}

	@Test
	public final void testDebugStringObject() throws IOException {
		LOGGER.debug("{}", MESSAGE);
		assertConsole(Level.DEBUG, MESSAGE, null);
	}

	@Test
	public final void testDebugStringObjectObject() throws IOException {
		LOGGER.debug("{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.DEBUG, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testDebugStringObjectArray() throws IOException {
		LOGGER.debug("{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.DEBUG, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testDebugStringThrowable() throws IOException {
		LOGGER.debug(MESSAGE, EXCEPTION);
		assertConsole(Level.DEBUG, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testIsDebugEnabledMarker() throws IOException {
		boolean is = LOGGER.isDebugEnabled(MARKER);
		assertTrue(is);
	}

	@Test
	public final void testDebugMarkerString() throws IOException {
		LOGGER.debug(MARKER, MESSAGE);
		assertConsole(Level.DEBUG, MESSAGE, null);
	}

	@Test
	public final void testDebugMarkerStringObject() throws IOException {
		LOGGER.debug(MARKER, "{}", MESSAGE);
		assertConsole(Level.DEBUG, MESSAGE, null);
	}

	@Test
	public final void testDebugMarkerStringObjectObject() throws IOException {
		LOGGER.debug(MARKER, "{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.DEBUG, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testDebugMarkerStringObjectArray() throws IOException {
		LOGGER.debug(MARKER, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.DEBUG, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testDebugMarkerStringThrowable() throws IOException {
		LOGGER.debug(MARKER, MESSAGE, EXCEPTION);
		assertConsole(Level.DEBUG, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testIsInfoEnabled() throws IOException {
		boolean is = LOGGER.isInfoEnabled();
		assertTrue(is);
	}

	@Test
	public final void testInfoString() throws IOException {
		LOGGER.info(MESSAGE);
		assertConsole(Level.INFO, MESSAGE, null);
	}

	@Test
	public final void testInfoStringObject() throws IOException {
		LOGGER.info("{}", MESSAGE);
		assertConsole(Level.INFO, MESSAGE, null);
	}

	@Test
	public final void testInfoStringObjectObject() throws IOException {
		LOGGER.info("{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.INFO, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testInfoStringObjectArray() throws IOException {
		LOGGER.info("{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.INFO, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testInfoStringThrowable() throws IOException {
		LOGGER.info(MESSAGE, EXCEPTION);
		assertConsole(Level.INFO, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testIsInfoEnabledMarker() throws IOException {
		boolean is = LOGGER.isInfoEnabled(MARKER);
		assertTrue(is);
	}

	@Test
	public final void testInfoMarkerString() throws IOException {
		LOGGER.info(MARKER, MESSAGE);
		assertConsole(Level.INFO, MESSAGE, null);
	}

	@Test
	public final void testInfoMarkerStringObject() throws IOException {
		LOGGER.info(MARKER, "{}", MESSAGE);
		assertConsole(Level.INFO, MESSAGE, null);
	}

	@Test
	public final void testInfoMarkerStringObjectObject() throws IOException {
		LOGGER.info(MARKER, "{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.INFO, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testInfoMarkerStringObjectArray() throws IOException {
		LOGGER.info(MARKER, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.INFO, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testInfoMarkerStringThrowable() throws IOException {
		LOGGER.info(MARKER, MESSAGE, EXCEPTION);
		assertConsole(Level.INFO, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testIsWarnEnabled() throws IOException {
		boolean is = LOGGER.isWarnEnabled();
		assertTrue(is);
	}

	@Test
	public final void testWarnString() throws IOException {
		LOGGER.warn(MESSAGE);
		assertConsole(Level.WARN, MESSAGE, null);
	}

	@Test
	public final void testWarnStringObject() throws IOException {
		LOGGER.warn("{}", MESSAGE);
		assertConsole(Level.WARN, MESSAGE, null);
	}

	@Test
	public final void testWarnStringObjectArray() throws IOException {
		LOGGER.warn("{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.WARN, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testWarnStringObjectObject() throws IOException {
		LOGGER.warn("{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.WARN, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testWarnStringThrowable() throws IOException {
		LOGGER.warn(MESSAGE, EXCEPTION);
		assertConsole(Level.WARN, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testIsWarnEnabledMarker() throws IOException {
		boolean is = LOGGER.isWarnEnabled(MARKER);
		assertTrue(is);
	}

	@Test
	public final void testWarnMarkerString() throws IOException {
		LOGGER.warn(MARKER, MESSAGE);
		assertConsole(Level.WARN, MESSAGE, null);
	}

	@Test
	public final void testWarnMarkerStringObject() throws IOException {
		LOGGER.warn(MARKER, "{}", MESSAGE);
		assertConsole(Level.WARN, MESSAGE, null);
	}

	@Test
	public final void testWarnMarkerStringObjectObject() throws IOException {
		LOGGER.warn(MARKER, "{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.WARN, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testWarnMarkerStringObjectArray() throws IOException {
		LOGGER.warn(MARKER, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.WARN, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testWarnMarkerStringThrowable() throws IOException {
		LOGGER.warn(MARKER, MESSAGE, EXCEPTION);
		assertConsole(Level.WARN, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testIsErrorEnabled() throws IOException {
		boolean is = LOGGER.isErrorEnabled();
		assertTrue(is);
	}

	@Test
	public final void testErrorString() throws IOException {
		LOGGER.error(MESSAGE);
		assertConsole(Level.ERROR, MESSAGE, null);
	}

	@Test
	public final void testErrorStringObject() throws IOException {
		LOGGER.error("{}", MESSAGE);
		assertConsole(Level.ERROR, MESSAGE, null);
	}

	@Test
	public final void testErrorStringObjectObject() throws IOException {
		LOGGER.error("{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.ERROR, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testErrorStringObjectArray() throws IOException {
		LOGGER.error("{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.ERROR, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testErrorStringThrowable() throws IOException {
		LOGGER.error(MESSAGE, EXCEPTION);
		assertConsole(Level.ERROR, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testIsErrorEnabledMarker() throws IOException {
		boolean is = LOGGER.isErrorEnabled(MARKER);
		assertTrue(is);
	}

	@Test
	public final void testErrorMarkerString() throws IOException {
		LOGGER.error(MARKER, MESSAGE);
		assertConsole(Level.ERROR, MESSAGE, null);
	}

	@Test
	public final void testErrorMarkerStringObject() throws IOException {
		LOGGER.error(MARKER, "{}", MESSAGE);
		assertConsole(Level.ERROR, MESSAGE, null);
	}

	@Test
	public final void testErrorMarkerStringObjectObject() throws IOException {
		LOGGER.error(MARKER, "{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.ERROR, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testErrorMarkerStringObjectArray() throws IOException {
		LOGGER.error(MARKER, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.ERROR, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testErrorMarkerStringThrowable() throws IOException {
		LOGGER.error(MARKER, MESSAGE, EXCEPTION);
		assertConsole(Level.ERROR, MESSAGE, EXCEPTION);
	}

	@Test
	public final void testLogMarkerString() throws IOException {
		enableTrace();
		LOGGER.log(Level.TRACE, MARKER, MESSAGE);
		assertConsole(Level.TRACE, MESSAGE, null);
		disableTrace();
	}

	@Test
	public final void testLogMarkerStringObject() throws IOException {
		LOGGER.log(Level.DEBUG, MARKER, "{}", MESSAGE);
		assertConsole(Level.DEBUG, MESSAGE, null);
	}

	@Test
	public final void testLogMarkerStringObjectObject() throws IOException {
		LOGGER.log(Level.INFO, MARKER, "{} {}", MESSAGE, MESSAGE);
		assertConsole(Level.INFO, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testLogMarkerStringObjectArray() throws IOException {
		LOGGER.log(Level.WARN, MARKER, "{} {}", new Object[] { MESSAGE, MESSAGE });
		assertConsole(Level.WARN, MESSAGE + " " + MESSAGE, null);
	}

	@Test
	public final void testLogMarkerStringThrowable() throws IOException {
		LOGGER.log(Level.ERROR, MARKER, MESSAGE, EXCEPTION);
		assertConsole(Level.ERROR, MESSAGE, EXCEPTION);
	}

}
