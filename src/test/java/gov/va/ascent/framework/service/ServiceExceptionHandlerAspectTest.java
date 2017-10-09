package gov.va.ascent.framework.service;

import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
@RunWith(MockitoJUnitRunner.class)
public class ServiceExceptionHandlerAspectTest {

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private MethodSignature signature;
    
    @Mock
    private JoinPoint.StaticPart staticPart;
    
    
    final Set<Class<? extends Throwable>> exclusionSet = new HashSet<>();
    
    @Mock
    private Throwable throwable;
    
    @Mock
    private RuntimeException appRuntimeException;
    
    
    private Object[] value;
	
	@Before
	public void setUp() throws Exception {
		value = new Object[2];
		value[0] = "arg 1";
		value[1] = "arg 2";
		when(joinPoint.getArgs()).thenReturn(value);
		when(joinPoint.getStaticPart()).thenReturn(staticPart);
        when(staticPart.getSignature()).thenReturn(signature);		
		when(signature.getMethod()).thenReturn(myMethod());
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testAfterThrowing() throws Throwable {
		try {
			ServiceExceptionHandlerAspect mockServiceExceptionHandlerAspect = new ServiceExceptionHandlerAspect();
			mockServiceExceptionHandlerAspect.afterThrowing(joinPoint, throwable);
	
			Assert.fail("expected exception");
		}catch(Throwable throwable) {
			Assert.assertTrue(throwable instanceof RuntimeException);
			Assert.assertNotNull(throwable.getCause());
			Assert.assertTrue(throwable.getMessage().contains("Unique ID: ["));			
		}
	}
	
    public Method myMethod() throws NoSuchMethodException{
        return getClass().getDeclaredMethod("someMethod");
    }

    public void someMethod() {
        // do nothing
    }
}
