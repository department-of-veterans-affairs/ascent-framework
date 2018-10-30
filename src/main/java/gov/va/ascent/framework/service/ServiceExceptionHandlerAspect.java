package gov.va.ascent.framework.service;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.exception.ExceptionToExceptionTranslationHandler;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.util.Defense;

/**
 * The Class ServiceExceptionHandlerAspect is a exception handling aspect for the service tier. The current implementation
 * leverages ExceptionToExceptionTranslator to basically translate and log errors. So if exceptions are raised by a
 * web service client, a DAO, etc. and are of the type "HibernateException" or some 3rd party exception type, the exception
 * will pass through the translator which will ensure it gets logged and wrapped into a exception of our own type with
 * associated diagnostic details that we can leverage to help trace through exceptions later.
 *
 * @author jshrader
 */
@Aspect
public class ServiceExceptionHandlerAspect extends BaseServiceAspect {

	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(ServiceExceptionHandlerAspect.class);

	private ExceptionToExceptionTranslationHandler exceptionToExceptionTranslator;

	/**
	 * Instantiates a new service exception handler aspect using a default ExceptionToExceptionTranslator.
	 * Explicitly declare this bean and use the other constructor(s) to customize the translator.
	 */
	public ServiceExceptionHandlerAspect() {
		// this to prevent double wrapping of AscentRuntimeException
		final Set<Class<? extends Throwable>> exclusionSet = new HashSet<>();
		exclusionSet.add(AscentRuntimeException.class);
		exclusionSet.add(IllegalArgumentException.class);
		this.exceptionToExceptionTranslator = new ExceptionToExceptionTranslationHandler(
				null, exclusionSet, ServiceException.class);
	}

	/**
	 * Instantiates a new service exception handler aspect using the specified exception translator.
	 *
	 * @param exceptionToExceptionTranslator the exception to exception translator
	 */
	public ServiceExceptionHandlerAspect(ExceptionToExceptionTranslationHandler exceptionToExceptionTranslator) {
		super();
		Defense.notNull(exceptionToExceptionTranslator, "exceptionToExceptionTranslator cannot be null");
		this.exceptionToExceptionTranslator = exceptionToExceptionTranslator;
	}

	/**
	 * Translate exceptions at the Service Interface service response
	 *
	 * @param joinPoint
	 * @param throwable
	 * @throws Throwable
	 */
	@AfterThrowing(pointcut = "publicStandardServiceMethod()", throwing = "throwable")
	public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws Throwable {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ServiceExceptionHandlerAspect executing, handling a throwable:" + joinPoint.toLongString());
		}
		// thrown exceptions are handled in the translator
		exceptionToExceptionTranslator.handleViaTranslation(((MethodSignature) joinPoint.getStaticPart().getSignature()).getMethod(),
				joinPoint.getArgs(), throwable);
	}

}
