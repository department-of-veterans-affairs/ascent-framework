package gov.va.ascent.framework.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import gov.va.ascent.framework.aspect.PerformanceLoggingAspect;

@Aspect
@Order(-9999)
public class ServiceTimerAspect extends BaseServiceAspect {

	@Around("publicStandardServiceMethod() && !restController()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		// thrown exceptions are handled in the PerformanceLoggingAspect
		return PerformanceLoggingAspect.aroundAdvice(joinPoint);
	}

}
