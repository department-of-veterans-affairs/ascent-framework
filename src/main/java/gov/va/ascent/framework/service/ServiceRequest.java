package gov.va.ascent.framework.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import gov.va.ascent.framework.transfer.AbstractTransferObject;
import gov.va.ascent.framework.transfer.ServiceTransferObjectMarker;

/**
 * A base Request object capable of representing the payload of a service request.
 *
 * @see gov.va.ascent.framework.transfer.AbstractTransferObject
 * @author jshrader
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceRequest")
public class ServiceRequest extends AbstractTransferObject implements ServiceTransferObjectMarker {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8521125059263688741L;

	/**
	 * Instantiates a new rest request.
	 */
	protected ServiceRequest() {
		super();
	}

}
