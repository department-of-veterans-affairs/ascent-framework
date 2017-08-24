package gov.va.ascent.framework.audit;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by vgadda on 8/17/17.
 */
public class BaseAuditAspect {

    @Pointcut("@annotation(gov.va.ascent.framework.audit.Auditable)")
    protected final static void auditableAnnotation(){}

    @Pointcut("auditableAnnotation()") //&& execution(* *(..))
    protected final static void auditableExecution(){}
}
