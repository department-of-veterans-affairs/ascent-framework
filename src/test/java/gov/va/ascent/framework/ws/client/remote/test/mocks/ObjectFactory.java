
package gov.va.ascent.framework.ws.client.remote.test.mocks;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the gov.va.vetsapi.refdata.api.v1.transfer package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {


	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.va.vetsapi.refdata.api.v1.transfer
	 *
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link TestAbstractRemoteServiceCallMockRequest }
	 *
	 */
	public TestAbstractRemoteServiceCallMockRequest createTestAbstractRemoteServiceCallMockRequest() {
		return new TestAbstractRemoteServiceCallMockRequest();
	}

	/**
	 * Create an instance of {@link TestAbstractRemoteServiceCallMockResponse }
	 *
	 */
	public TestAbstractRemoteServiceCallMockResponse createTestAbstractRemoteServiceCallMockResponse() {
		return new TestAbstractRemoteServiceCallMockResponse();
	}

	/**
	 * Create an instance of {@link TestNonexistingMockRequest }
	 *
	 */
	/*
	 * This method is intentionally not implemented for testing purposes
    public TestNonexistingMockRequest createTestNonexistingMockRequest() {
        return new TestNonexistingMockRequest();
    }
	 */
}
