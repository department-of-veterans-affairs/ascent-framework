package gov.va.ascent.framework.log;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceLogMethodInterceptorTest {

    private Logger LOG = (Logger) org.slf4j.LoggerFactory.getLogger(PerformanceLogMethodInterceptorTest.class);

    @Mock
    MethodInvocation invocation;

    PerformanceLogMethodInterceptor performanceLogMethodInterceptor = new PerformanceLogMethodInterceptor();

    @Before
    public void setUp(){
        LOG.setLevel(Level.DEBUG);
        performanceLogMethodInterceptor = new PerformanceLogMethodInterceptor();
    }

    @After
    public void tearDown(){
        LogTestAppender.events.clear();
        LOG.setLevel(Level.DEBUG);
    }


    @Test
    public void testInvokeDebug() throws Throwable {

        mockInvocationOf("getString", null);
        assertEquals("enter [Helper.getString]", LogTestAppender.events.get(0).getMessage());
        assertEquals(Level.DEBUG, LogTestAppender.events.get(0).getLevel());
        assertTrue(LogTestAppender.events.get(1).getMessage().contains("exit [Helper.getString] in elapsed time ["));
        assertEquals(Level.DEBUG, LogTestAppender.events.get(1).getLevel());
        assertTrue(2 == LogTestAppender.events.size());

    }

    @Test
    public void testInvokeInfo() throws Throwable {
        LOG.setLevel(Level.INFO);
        mockInvocationOf("getString", null);
        assertTrue(LogTestAppender.events.get(0).getMessage().contains("exit [Helper.getString] in elapsed time ["));
        assertEquals(Level.INFO, LogTestAppender.events.get(0).getLevel());
        assertTrue(1 == LogTestAppender.events.size());

    }

    @Test
    public void testInvokeGreaterThanWarningThreshold() throws Throwable {
        LOG.setLevel(Level.INFO);
        performanceLogMethodInterceptor.setWarningThreshhold(-1);
        mockInvocationOf("getString", null);
        assertTrue(LogTestAppender.events.get(0).getMessage().contains("PERFORMANCE WARNING response for [Helper.getString] in elapsed time ["));
        assertEquals(Level.WARN, LogTestAppender.events.get(0).getLevel());
        assertTrue(1 == LogTestAppender.events.size());

    }

    @Test
    public void testInvokeWarningThresholdOnMethod() throws Throwable {
        LOG.setLevel(Level.INFO);
        Map<String, Integer> classMethodThresholds = new HashMap<>();
        classMethodThresholds.put("Helper.getString", -1);
        performanceLogMethodInterceptor.setClassAndMethodSpecificWarningThreshold(classMethodThresholds);
        mockInvocationOf("getString", null);
        assertTrue(LogTestAppender.events.get(0).getMessage().contains("PERFORMANCE WARNING response for [Helper.getString] in elapsed time ["));
        assertEquals(Level.WARN, LogTestAppender.events.get(0).getLevel());
        assertTrue(1 == LogTestAppender.events.size());
        assertEquals(new Integer(1500), performanceLogMethodInterceptor.getWarningThreshhold());

    }

    private MethodInvocation mockInvocationOf(String methodName, Object returnValue) throws Throwable {

        when(invocation.getMethod()).thenReturn(Helper.class.getMethod(methodName));
        when(performanceLogMethodInterceptor.invoke(invocation)).thenReturn(returnValue);

        return invocation;
    }

    interface Helper {

        String getString();

    }

}
