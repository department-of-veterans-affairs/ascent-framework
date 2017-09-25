package gov.va.ascent.framework.audit;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by vgadda on 8/17/17.
 */
public class BaseAuditAspect {
	
	protected BaseAuditAspect() {
		
	}
    @Pointcut("@annotation(gov.va.ascent.framework.audit.Auditable)")
	protected static final void auditableAnnotation() {
  	  // Do nothing.
	}

    @Pointcut("auditableAnnotation() && execution(* *(..))")
	protected static final void auditableExecution() {
    	  // Do nothing.
	}
}
