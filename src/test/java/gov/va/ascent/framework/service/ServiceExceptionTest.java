package gov.va.ascent.framework.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceExceptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEmptyConstructor() {
		ServiceException serviceException = new ServiceException();
		assertNotNull(serviceException.getMessage());
		assertNull(serviceException.getCause());
	}

	@Test
	public void testConstructorWithStringAndThrowable() {
		ServiceException serviceException = new ServiceException("Unit Testing", new Throwable());
		assertNotNull(serviceException.getMessage());
		assertNotNull(serviceException.getCause());
        Assert.assertEquals("Unique ID: ["
                + serviceException.getUniqueId()
                + "] Unit Testing"
        , serviceException.getMessage());		
	}

	@Test
	public void testContructorWithString() {
		ServiceException serviceException = new ServiceException("Unit Testing");
		assertNotNull(serviceException.getMessage());
		assertNull(serviceException.getCause());
	}

	@Test
	public void testConstructorWithThrowable() {
		ServiceException serviceException = new ServiceException(new Throwable());
		assertNotNull(serviceException.getMessage());
		assertNotNull(serviceException.getCause());
        Assert.assertEquals("Unique ID: ["
                + serviceException.getUniqueId()
                + "] java.lang.Throwable"
        , serviceException.getMessage());		
	}

}
