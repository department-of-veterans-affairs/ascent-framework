
package gov.va.ascent.framework.ws.client.remote.test.mocks;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import gov.va.ascent.framework.transfer.AbstractTransferObject;


/**
 * <p>NOTE: THERE IS INTENTIONALLY NO ObjectFactory METHOD FOR THIS CLASS. Necessary for testing.
 *
 *
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="somePrivateMember" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"somePrivateMember"
})
@XmlRootElement(name = "TestNonexistingMockRequest")
public class TestNonexistingMockRequest
extends AbstractTransferObject
{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2396390075291310912L;
	private String somePrivateMember;

	/**
	 * Gets the value of the somePrivateMember property.
	 *
	 * @return
	 *     possible object is
	 *     {@link String }
	 *
	 */
	public String getSomePrivateMember() {
		return somePrivateMember;
	}

	/**
	 * Sets the value of the setSomePrivateMember property.
	 *
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *
	 */
	public void setSomePrivateMember(String somePrivateMember) {
		this.somePrivateMember = somePrivateMember;
	}

}
