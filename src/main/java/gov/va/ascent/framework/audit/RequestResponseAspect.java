package gov.va.ascent.framework.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by vgadda on 8/17/17.
 */
@Aspect
@Component
public class RequestResponseAspect extends BaseAuditAspect{

    @Autowired
    ObjectMapper mapper;

    @Around("auditableExecution()")
    public void logRequestResponse(final ProceedingJoinPoint joinPoint ){
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        System.out.println("#################################"+method.getAnnotations().length);
        Auditable auditableAnnotation = method.getAnnotation(Auditable.class);
        if(auditableAnnotation.event().equals(AuditEvents.REQUEST_RESPONSE)){
            Object returnObject = null;
            try {
                returnObject = joinPoint.proceed();
                AuditLogger.info("", AuditEvents.REQUEST_RESPONSE.name(), mapper.writeValueAsString(returnObject));
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }

        }
    }
}
