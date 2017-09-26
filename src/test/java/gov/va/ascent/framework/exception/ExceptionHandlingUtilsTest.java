package gov.va.ascent.framework.exception;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ExceptionHandlingUtilsTest {

    private Logger LOG = (Logger) org.slf4j.LoggerFactory.getLogger(ExceptionHandlingUtilsTest.class);

    @Before
    public void setUp(){
        LOG.setLevel(Level.DEBUG);
    }

    @After
    public void tearDown(){
        TestAppender.events.clear();
        LOG.setLevel(Level.DEBUG);
    }

    @Test
    public void testLoggingUtils() throws Exception {
        // setup
        Object[] args = new Object[2];
        args[0] = "Arg One";
        args[1] = 42L;

        ExceptionHandlingUtils.logException("Catcher", this.getClass().getMethod("testLoggingUtils"),args, new Throwable("test throw"));

        Assert.assertEquals("[WARN] Catcher caught exception, handling it as configured.  Here are details [java.lang.Throwable thrown by gov.va.ascent.framework.exception.ExceptionHandlingUtilsTest.testLoggingUtils] args [[Arg One, 42]].",
                TestAppender.events.get(0).toString());
        Assert.assertEquals("java.lang.Throwable", TestAppender.events.get(0).getThrowableProxy().getClassName());
        Assert.assertEquals(Level.WARN, TestAppender.events.get(0).getLevel());

    }

    @Test
    public void testLoggingWarnOff() throws Exception {
        LOG.setLevel(Level.ERROR);

        ExceptionHandlingUtils.logException("Catcher", this.getClass().getMethod("testLoggingWarnOff"),null, new Throwable("test throw"));
        Assert.assertEquals("[ERROR] Catcher caught exception, handling it as configured.  Here are details [java.lang.Throwable thrown by gov.va.ascent.framework.exception.ExceptionHandlingUtilsTest.testLoggingWarnOff] args [null].",
                TestAppender.events.get(0).toString());
        Assert.assertEquals(Level.ERROR, TestAppender.events.get(0).getLevel());


    }

}
