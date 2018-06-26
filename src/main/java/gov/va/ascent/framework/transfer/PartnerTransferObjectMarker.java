package gov.va.ascent.framework.transfer;

/**
 * This marker interface identifies data POJO classes that are generated from partner client WSDLs.
 * A typical purpose for this interface would be to mark Request & Response objects
 * and/or their contained members as specifically being objects derived from partner WSDLs,
 * as opposed to other POJOs or request/response objects from a different layer.
 *
 * @author aburkholder
 */
public interface PartnerTransferObjectMarker {

}
