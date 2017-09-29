package gov.va.ascent.framework.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceLoggingAspectTest {

    private Logger AspectLoggingLOG = (Logger) org.slf4j.LoggerFactory.getLogger(PerformanceLoggingAspect.class);
    private Logger AspectLoggingTestLOG = (Logger) org.slf4j.LoggerFactory.getLogger(PerformanceLoggingAspectTest.class);

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private MethodSignature signature;

    @Mock
    private JoinPoint.StaticPart staticPart;


    @Before
    public void setup() throws Throwable{
        AspectLoggingLOG.setLevel(Level.DEBUG);
        AspectLoggingTestLOG.setLevel(Level.DEBUG);
        when(proceedingJoinPoint.toLongString()).thenReturn("ProceedingJoinPointLongString");
        when(proceedingJoinPoint.getStaticPart()).thenReturn(staticPart);
        when(staticPart.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(myMethod());
    }

    @After
    public void tearDown(){
        AspectLoggingTestAppender.events.clear();
        AspectLoggingLOG.setLevel(Level.DEBUG);
        AspectLoggingTestLOG.setLevel(Level.DEBUG);
    }


    @Test
    public void testAroundAdviceDebugOn() throws Throwable {
        PerformanceLoggingAspect.aroundAdvice(proceedingJoinPoint);

        assertEquals("PerformanceLoggingAspect executing around method:ProceedingJoinPointLongString",
                AspectLoggingTestAppender.events.get(0).getMessage());
        assertEquals("enter [PerformanceLoggingAspectTest.someMethod]",
                AspectLoggingTestAppender.events.get(1).getMessage());
        assertEquals("PerformanceLoggingAspect after method was called.",
                AspectLoggingTestAppender.events.get(2).getMessage());
        assertTrue(AspectLoggingTestAppender.events.get(3).getMessage()
                .contains("exit [PerformanceLoggingAspectTest.someMethod] in elapsed time ["));
        assertEquals(Level.INFO, AspectLoggingTestAppender.events.get(3).getLevel());

    }

    @Test
    public void testAroundAdviceDebugOff() throws Throwable {
        AspectLoggingLOG.setLevel(Level.INFO);
        AspectLoggingTestLOG.setLevel(Level.INFO);
        PerformanceLoggingAspect.aroundAdvice(proceedingJoinPoint);

        assertTrue(AspectLoggingTestAppender.events.get(0).getMessage()
                .contains("exit [PerformanceLoggingAspectTest.someMethod] in elapsed time ["));
        assertEquals(Level.INFO, AspectLoggingTestAppender.events.get(0).getLevel());

    }

    @Test
    public void testAroundAdviceThrowError() throws Throwable {
        AspectLoggingLOG.setLevel(Level.ERROR);
        AspectLoggingTestLOG.setLevel(Level.ERROR);
        when(proceedingJoinPoint.proceed()).thenThrow(new Throwable("Unit Testing"));

        try {
            PerformanceLoggingAspect.aroundAdvice(proceedingJoinPoint);
        } catch(Throwable e){
            assertEquals("PerformanceLoggingAspect encountered uncaught exception. Throwable Cause.",
                    AspectLoggingTestAppender.events.get(0).getMessage());
            assertEquals(Level.ERROR, AspectLoggingTestAppender.events.get(0).getLevel());
            assertEquals("PerformanceLoggingAspect encountered uncaught exception. Throwable Message.",
                    AspectLoggingTestAppender.events.get(1).getMessage());
            assertEquals(Level.ERROR, AspectLoggingTestAppender.events.get(1).getLevel());
        }


    }

    public Method myMethod() throws NoSuchMethodException{
        return getClass().getDeclaredMethod("someMethod");
    }

    public void someMethod() {
        // do nothing
    }
}
