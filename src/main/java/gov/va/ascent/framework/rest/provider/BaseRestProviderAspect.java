package gov.va.ascent.framework.rest.provider;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.Pointcut;

import gov.va.ascent.framework.audit.AuditEventData;
import gov.va.ascent.framework.audit.AuditEvents;

/**
 * This is the base class for rest provider aspects.
 * 
 * @author jshrader
 */
public class BaseRestProviderAspect {

	/**
	 * Protected constructor.
	 */
	protected BaseRestProviderAspect(){
		super();
	}
	
	/**
	 * This aspect defines the pointcut of standard REST controller.  Those are controllers that...
	 * 
	 * (1) are annotated with org.springframework.web.bind.annotation.RestController
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	protected static final void restController() {
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
	protected static final void publicServiceResponseRestMethod() {
		 // Do nothing.
	}
	
	
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
	 * (1) are annotated with gov.va.ascent.framework.audit.Auditable (2) the
	 * execution of any method regardless of return or parameter types Ensure you
	 * follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("auditableAnnotation() && execution(* *(..))")
	protected static final void auditableExecution() {
		// Do nothing.
	}

    
    /**
     * Gets the default auditable instance.
     *
     * @param method the method
     * @return the auditable instance
     */
	public static AuditEventData getDefaultAuditableInstance(final Method method) {
		if(method != null) {
			return new AuditEventData(AuditEvents.REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName());
		} else {
			return new AuditEventData(AuditEvents.REQUEST_RESPONSE, "", "");
		}
	}
}
