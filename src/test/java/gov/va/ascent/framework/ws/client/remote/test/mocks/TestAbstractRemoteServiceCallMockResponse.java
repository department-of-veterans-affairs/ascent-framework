
package gov.va.ascent.framework.ws.client.remote.test.mocks;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.va.ascent.framework.transfer.AbstractTransferObject;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="someData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "someData"
})
@XmlRootElement(name = "TestAbstractRemoteServiceCallMockResponse")
public class TestAbstractRemoteServiceCallMockResponse
    extends AbstractTransferObject
{

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8565439898520091824L;
	protected String someData;

    /**
     * Gets the value of the someData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSomeData() {
        return someData;
    }

    /**
     * Sets the value of the someData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSomeData(String value) {
        this.someData = value;
    }

}
