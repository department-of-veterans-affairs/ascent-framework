package gov.va.ascent.framework.service;

import org.aspectj.lang.annotation.Pointcut;

/**
 * This is the base class for service aspects.
 * 
 * @author jshrader
 */
public class BaseServiceAspect {

	protected BaseServiceAspect(){
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
	 * This pointcut reflects a public standard service method.
	 * 
	 * These are methods which are
	 * (1) public
	 * (2) return a ServiceResponse
	 * 
	 * @See gov.va.ascent.framework.service.ServiceResponse
	 */
	@Pointcut("execution(public gov.va.ascent.framework.service.ServiceResponse+ *(..))")
	protected static final void publicStandardServiceMethod() {
		//Do Nothing
	}

}
