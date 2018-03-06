package gov.va.ascent.framework.log;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.rule.OutputCapture;

import com.github.lalyos.jfiglet.FigletFont;

import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.log.LogUtil.Level;


public class LogUtil_UnitTest {
	
	private Logger LOGGER = (Logger) org.slf4j.LoggerFactory.getLogger(LogUtil_UnitTest.class);

    /** The output capture. */
    @Rule
    public OutputCapture outputCapture = new OutputCapture();
    
    @After
    public void tearDown(){
        LogTestAppender.events.clear();
    }

    /**
     * Test debug.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testDebug() throws IOException {
        final String unitTestBanner = "debug banner";
        final String unitTestMessage = "debug message";
        LogUtil.logDebugWithBanner(LOGGER, unitTestBanner, unitTestMessage);
        assertConsole(Level.DEBUG, unitTestBanner, unitTestMessage);
    }

     /**
     * Test info.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testInfo() throws IOException {
        final String unitTestBanner = "info banner";
        final String unitTestMessage = "info message";
        LogUtil.logInfoWithBanner(LOGGER, unitTestBanner, unitTestMessage);
        assertConsole(Level.INFO, unitTestBanner, unitTestMessage);
    }

    /**
     * Test warning.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWarning() throws IOException {
        final String unitTestBanner = "warning banner";
        final String unitTestMessage = "warning message";
        LogUtil.logWarningWithBanner(LOGGER, unitTestBanner, unitTestMessage);
        assertConsole(Level.WARN, unitTestBanner, unitTestMessage);
    }

    /**
     * Test error.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testError() throws IOException {
        final String unitTestBanner = "error banner!";
        final String unitTestMessage = "error message";
        LogUtil.logErrorWithBanner(LOGGER, unitTestBanner, unitTestMessage);
        assertConsole(Level.ERROR, unitTestBanner, unitTestMessage);
    }


    /**
     * Test error.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateBannerString()  throws IOException {
        final String unitTestBanner = null;
        final String unitTestMessage = "error message";
        LogUtil.logErrorWithBanner(LOGGER, unitTestBanner, unitTestMessage);
        assertConsole(Level.ERROR, "", unitTestMessage);
    }
    
    /** 
     * Assert console.
     *
     * @param level the level
     * @param banner the banner
     * @param message the message
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void assertConsole(final Level level, final String banner, final String message) throws IOException {
        final String expected =
                "\n" + FigletFont.convertOneLine(LogUtil.FONT_FILE, level.name() + ": " + banner.toUpperCase());
        final String outString = outputCapture.toString();
        Assert.assertTrue(outString.contains(expected));
        Assert.assertTrue(outString.contains(message));
    }

    /**
     * Test debug is disabled.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testDebugDisabled() throws IOException {
        final String unitTestBanner = "debug banner";
        final String unitTestMessage = "debug message";
        LOGGER.setLevel(ch.qos.logback.classic.Level.OFF);
        LogUtil.logDebugWithBanner(LOGGER, unitTestBanner, unitTestMessage);
        assertTrue(outputCapture.toString().equals(""));
        LOGGER.setLevel(ch.qos.logback.classic.Level.ALL);
    }
   
    /**
     * Test debug is disabled.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testInfoDisabled() throws IOException {
        final String unitTestBanner = "debug banner";
        final String unitTestMessage = "debug message";
        LOGGER.setLevel(ch.qos.logback.classic.Level.OFF);
        LogUtil.logInfoWithBanner(LOGGER, unitTestBanner, unitTestMessage);
        assertTrue(outputCapture.toString().equals(""));
        LOGGER.setLevel(ch.qos.logback.classic.Level.ALL);
    }
    
   
}
