/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ascent.framework.audit;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 *
 * @author rthota
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseAuditAspectTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;
    @Mock
    private MethodSignature signature;

    
    @Mock
    private JoinPoint.StaticPart staticPart;
    private Object[] value;

    
	@Before
	public void setUp() throws Exception {
		value = new Object[1];
		value[0] = "";
		when(proceedingJoinPoint.getArgs()).thenReturn(value);
		when(proceedingJoinPoint.getStaticPart()).thenReturn(staticPart);
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(myMethod());
	}
    /**
     * Test of auditableAnnotation method, of class BaseAuditAspect.
     */
    @Test
    public void testAuditableAnnotation() {
        System.out.println("auditableAnnotation");
        BaseAuditAspect.auditableAnnotation();
    }

    /**
     * Test of auditableExecution method, of class BaseAuditAspect.
     */
    @Test
    public void testAuditableExecution() {
        System.out.println("auditableExecution");
        BaseAuditAspect.auditableExecution();
    }

    /**
     * Test of auditRestController method, of class BaseAuditAspect.
     */
    @Test
    public void testAuditRestController() {
        System.out.println("auditRestController");
        BaseAuditAspect.auditRestController();
    }

    /**
     * Test of auditPublicServiceResponseRestMethod method, of class BaseAuditAspect.
     */
    @Test
    public void testAuditPublicServiceResponseRestMethod() {
        System.out.println("auditPublicServiceResponseRestMethod");
        BaseAuditAspect.auditPublicServiceResponseRestMethod();
    }

    /**
     * Test of getMethodAndArgumentsAsString method, of class BaseAuditAspect.
     */
    @Test
    public void testGetMethodAndArgumentsAsString() {
        System.out.println("getMethodAndArgumentsAsString");
        BaseAuditAspect instance = new BaseAuditAspect();
        String expResult = "someMethod()";
        String result = instance.getMethodAndArgumentsAsString(proceedingJoinPoint);
        assertEquals(expResult, result);

    }

    /**
     * Test of getMethodName method, of class BaseAuditAspect.
     */
    @Test
    public void testGetMethodName() {
        System.out.println("getMethodName");
        BaseAuditAspect instance = new BaseAuditAspect();
        String expResult = "someMethod";
        String result = instance.getMethodName(proceedingJoinPoint);
        assertEquals(expResult, result);
    }

    /**
     * Test of getResultAsString method, of class BaseAuditAspect.
     */
    @Test
    public void testGetResultAsString() {
        System.out.println("getResultAsString");
        Object result_2 = "test";
        BaseAuditAspect instance = new BaseAuditAspect();
        String expResult = " returned test";
        String result = instance.getResultAsString(result_2);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getExceptionAsString method, of class BaseAuditAspect.
     */
    @Test
    public void testGetExceptionAsString() {
        System.out.println("getExceptionAsString");
        Throwable ex = new Throwable();
        long duration = 0L;
        BaseAuditAspect instance = new BaseAuditAspect();
        String expResult = " threw Throwable after 0 msecs with message null";
        String result = instance.getExceptionAsString(ex, duration);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDefaultAuditableInstance method, of class BaseAuditAspect.
     */
    @Test
    public void testGetDefaultAuditableInstance() {
        System.out.println("getDefaultAuditableInstance");
        Method method = null;
        AuditEvents expResult = AuditEvents.REQUEST_RESPONSE;
        Auditable result = BaseAuditAspect.getDefaultAuditableInstance(method);
        assertEquals(expResult, result.event());
        assertEquals("", result.activity());
    }

    /**
     * Test of getAuditableInstance method, of class BaseAuditAspect.
     */
    @Test
    public void testGetAuditableInstance() {
        System.out.println("getAuditableInstance");
        Auditable auditable = null;
        Method method = null;
        Auditable expResult = null;
        Auditable result = BaseAuditAspect.getAuditableInstance(auditable, method);
        assertEquals(expResult, result);
    }
    
    public Method myMethod() throws NoSuchMethodException{
        return getClass().getDeclaredMethod("someMethod");
    }

    public void someMethod() {
        // do nothing
    }    
    
}
