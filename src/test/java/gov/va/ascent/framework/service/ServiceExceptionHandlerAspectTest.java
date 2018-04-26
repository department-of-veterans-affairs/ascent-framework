package gov.va.ascent.framework.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.exception.ExceptionToExceptionTranslationHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class ServiceExceptionHandlerAspectTest {

	private Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(ServiceExceptionHandlerAspect.class);

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
		logger.setLevel(Level.DEBUG);
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
			assertNotNull(throwable.getCause());
		}
	}

	@Test
	public void testAfterThrowingNoDebug() throws Throwable {
		logger.setLevel(Level.ERROR);
		ExceptionToExceptionTranslationHandler exceptionToExceptionTranslationHandler = new ExceptionToExceptionTranslationHandler();
		try {
			ServiceExceptionHandlerAspect mockServiceExceptionHandlerAspect = new ServiceExceptionHandlerAspect(exceptionToExceptionTranslationHandler);
			mockServiceExceptionHandlerAspect.afterThrowing(joinPoint, throwable);

			Assert.fail("expected exception");
		}catch(Throwable throwable) {
			Assert.assertTrue(throwable instanceof RuntimeException);
			assertNotNull(throwable.getCause());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() throws IllegalArgumentException{
		ServiceExceptionHandlerAspect serviceExceptionHandlerAspect =
				new ServiceExceptionHandlerAspect(null);
	}

	@Test
	public void testConstructorNotNull() throws IllegalArgumentException{
		ExceptionToExceptionTranslationHandler exceptionToExceptionTranslationHandler
				= new ExceptionToExceptionTranslationHandler();
		ServiceExceptionHandlerAspect serviceExceptionHandlerAspect =
				new ServiceExceptionHandlerAspect(exceptionToExceptionTranslationHandler);
		assertNotNull(serviceExceptionHandlerAspect);
	}
	
    public Method myMethod() throws NoSuchMethodException{
        return getClass().getDeclaredMethod("someMethod");
    }

    public void someMethod() {
        // do nothing
    }

	/**
	 * Gets the Method to use in tests.  Method isn't critical for these tests.
	 *
	 * @return the method to use in tests
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 */
	private Method getMethodToUseInTests() throws NoSuchMethodException, SecurityException{
		return ExceptionToExceptionTranslationHandler.class.getMethod("handleViaTranslation", Method.class, Object[].class, Throwable.class);
	}
}
