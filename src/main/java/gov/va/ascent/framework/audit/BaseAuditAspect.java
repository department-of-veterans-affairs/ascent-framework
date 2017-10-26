package gov.va.ascent.framework.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by vgadda on 8/17/17.
 */
public class BaseAuditAspect {

    /**
	 * This aspect defines the pointcut of methods that...
	 * 
	 * (1) are annotated with gov.va.ascent.framework.audit.Auditable
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("@annotation(gov.va.ascent.framework.audit.Auditable)")
    protected static final void auditableAnnotation() {
  	  // Do nothing.
	}
    
	 /**
	 * This aspect defines the pointcut of methods that...
	 * 
	 * (1) are annotated with gov.va.ascent.framework.audit.Auditable
	 * (2) the execution of any method regardless of return or parameter types
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("auditableAnnotation() && execution(* *(..))")
	protected static final void auditableExecution() {
    	  // Do nothing.
	}
    
    /**
	 * This aspect defines the pointcut of standard REST controller.  Those are controllers that...
	 * 
	 * (1) are annotated with org.springframework.web.bind.annotation.RestController
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	protected static final void auditRestController() {
		 // Do nothing.
	}
	
	/**
	 * This aspect defines the pointcut of standard REST endpoints.  Those are endpoints that...
	 * 
	 * (1) are rest controllers (see that pointcut)
	 * (2) the method is public
	 * (3) the method returns org.springframework.http.ResponseEntity<gov.va.ascent.framework.service.ServiceResponse+>
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("execution(public org.springframework.http.ResponseEntity<gov.va.ascent.framework.service.ServiceResponse+> *(..))")
	protected static final void auditPublicServiceResponseRestMethod() {
		 // Do nothing.
	}
	
    /**
     * Gets the method and arguments as string.
     *
     * @param joinPoint the join point
     * @return the method and arguments as string
     */
    protected String getMethodAndArgumentsAsString(ProceedingJoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs()).map(arg -> arg.toString())
                .collect(Collectors.joining(", ", getMethodName(joinPoint) + "(", ")"));
    }

    /**
     * Gets the method name.
     *
     * @param joinPoint the join point
     * @return the method name
     */
    protected String getMethodName(ProceedingJoinPoint joinPoint) {
        return MethodSignature.class.cast(joinPoint.getSignature()).getMethod().getName();
    }

    /**
     * Gets the result as string.
     *
     * @param result the result
     * @return the result as string
     */
    protected String getResultAsString(Object result) {
        return new StringBuilder(" returned ").append(result).toString();
    }

    /**
     * Gets the exception as string.
     *
     * @param ex the ex
     * @param duration the duration
     * @return the exception as string
     */
    protected String getExceptionAsString(Throwable ex, long duration) {
        return new StringBuilder(" threw ").append(ex.getClass().getSimpleName()).append(" after ").append(duration)
                .append(" msecs with message ").append(ex.getMessage()).toString();
    }
    
    /**
     * Gets the default auditable instance.
     *
     * @param method the method
     * @return the auditable instance
     */
	public static AuditData getDefaultAuditableInstance(final Method method) {
		if(method != null) {
			return new AuditData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName());
		} else {
			return new AuditData(AuditEvents.REQUEST_RESPONSE, "", "");
		}
	}
}