package gov.va.ascent.framework.transfer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AbstractTransferObjectTest {

	private TransferObjectTest testObj;
	
	@Before
	public void setUp() throws Exception {
		testObj = new TransferObjectTest();;
	}	
	
	@Test
	public void testHashCode() {
		assertTrue(testObj.hashCode()>0);
	}

	@Test
	public void testGetToStringEqualsHashExcludeFields() {
		assertNotNull(testObj.getToStringEqualsHashExcludeFields());
	}

	@Test
	public void testValidateForNullMessages() {
		assertNotNull(testObj.validate(null));
		assertEquals(0,testObj.validate(null).size());
	}

	@Test
	public void testToString() {
		assertNotNull(testObj.toString());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(testObj.equals(new TransferObjectTest()));
	}
	
	public class TransferObjectTest extends AbstractTransferObject{
		
	}

}
