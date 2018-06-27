package gov.va.ascent.framework.transfer;

/**
 * This marker interface identifies data POJO classes that exist as service layer (e.g. business layer) objects.
 * A typical purpose for this interface would be to mark objects that are root in the class hierarchies,
 * and/or their contained members as specifically being objects that live in the services/business layer,
 * as opposed to other POJOs or request/response objects from the REST APIs or partner WSDLs.
 *
 * @author aburkholder
 */
public interface ServiceTransferObjectMarker {

}
