package gov.va.ascent.framework.audit;

/**
 * Events types to be associated with {@link AuditEventData} used in {@link Auditable} classes and/or methods.
 * Created by vgadda on 8/17/17.
 */
public enum AuditEvents {
	/** REST request / response event */
	REQUEST_RESPONSE,
	/** Security interceptor or similar event */
	SECURITY,
	/** Partner SOAP request / response event */
	PARTNER_REQUEST_RESPONSE,
	/** Audit event from within the service business tier */
	SERVICE_AUDIT,
	/** An audit rquest was made, but no event type was specified */
	UNKNOWN
}
