package gov.va.ascent.framework.rest.provider;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import gov.va.ascent.framework.aspect.PerformanceLoggingAspect;

@Aspect
@Order(-9999)
public class RestProviderTimerAspect extends BaseRestProviderAspect {
	
	@Around("restController() && publicServiceResponseRestMethod()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		return PerformanceLoggingAspect.aroundAdvice(joinPoint);
    }
	
}
