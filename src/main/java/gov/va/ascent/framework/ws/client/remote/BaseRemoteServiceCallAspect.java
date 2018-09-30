package gov.va.ascent.framework.ws.client.remote;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.Pointcut;
import gov.va.ascent.framework.audit.AuditEventData;
import gov.va.ascent.framework.audit.AuditEvents;

/**
 * This is the base class for aspects involved in remote calls to partner services.
 *
 * @author aburkholder
 */

public class BaseRemoteServiceCallAspect{


	public BaseRemoteServiceCallAspect() {
		// TODO Auto-generated constructor stub
		super();
	}


	/**
	 * This pointcut reflects a standard remote partner service call.
	 * A partner service call is identified by implementation of the RemoteServiceCall interface.
	 */
	@Pointcut("execution(* gov.va.ascent.framework.ws.client.remote.RemoteServiceCall.*(..))")
	protected static final void standardRemoteServiceCallMethod() {
		//Do Nothing
	}
	
	
	/**
	 * This pointcut reflects a standard remote partner service call.
	 * A partner service call is identified by implementation of the RemoteServiceCall interface.
	 */
	@Pointcut("execution(* gov.va.ascent.framework.ws.client.remote.AbstractRemoteServiceCallMock+.*(..))")
	protected static final void standardRemoteServiceCallMockMethod() {
		//Do Nothing
	}

	/**
	 * Gets the default auditable instance.
	 *
	 * @param method the method
	 * @return the auditable instance
	 */
	public static AuditEventData getDefaultAuditableInstance(final Method method) {
		if(method != null) {
			return new AuditEventData(AuditEvents.PARTNER_REQUEST_RESPONSE, method.getName(), method.getDeclaringClass().getName());
		} else {
			return new AuditEventData(AuditEvents.PARTNER_REQUEST_RESPONSE, "", "");
		}
	}
}
