package gov.va.ascent.framework.exception;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class ExceptionHandlingUtilsTest {

    @SuppressWarnings("rawtypes")
    @Mock
    private Appender mockAppender;
    // Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @SuppressWarnings("unchecked")
	@Before
    public void setUp(){
        final Logger logger = (Logger) LoggerFactory.getLogger(ExceptionHandlingUtilsTest.class);
        logger.setLevel(Level.DEBUG);
        logger.addAppender(mockAppender);
    }

    @After
    public void tearDown(){

    }

    @SuppressWarnings("unchecked")
	@Test
    public void testLoggingUtils() throws Exception {
        // setup
        Object[] args = new Object[2];
        args[0] = "Arg One";
        args[1] = 42L;

        ExceptionHandlingUtils.logException("Catcher", this.getClass().getMethod("testLoggingUtils"),args, new Throwable("test throw"));
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
        Assert.assertEquals("[WARN] Catcher caught exception, handling it as configured.  Here are details [java.lang.Throwable thrown by gov.va.ascent.framework.exception.ExceptionHandlingUtilsTest.testLoggingUtils] args [[Arg One, 42]].",
                loggingEvents.get(0).toString());
        Assert.assertEquals("java.lang.Throwable", loggingEvents.get(0).getThrowableProxy().getClassName());
        Assert.assertEquals(Level.WARN, loggingEvents.get(0).getLevel());

    }

    @SuppressWarnings("unchecked")
	@Test
    public void testLoggingWarnOff() throws Exception {
        final Logger logger = (Logger) LoggerFactory.getLogger(ExceptionHandlingUtilsTest.class);
        logger.setLevel(Level.ERROR);

        ExceptionHandlingUtils.logException("Catcher", myMethod(),null, new Throwable("test throw"));
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();

        Assert.assertEquals("[ERROR] Catcher caught exception, handling it as configured.  Here are details [java.lang.Throwable thrown by gov.va.ascent.framework.exception.ExceptionHandlingUtilsTest.someMethod] args [null].",
                loggingEvents.get(0).toString());
        Assert.assertEquals(Level.ERROR, loggingEvents.get(0).getLevel());


    }

    public Method myMethod() throws NoSuchMethodException{
        return getClass().getDeclaredMethod("someMethod");
    }

    public void someMethod() {
        // do nothing
    }

}
