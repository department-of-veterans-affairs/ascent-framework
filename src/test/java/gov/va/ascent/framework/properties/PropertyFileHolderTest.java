package gov.va.ascent.framework.properties;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertyFileHolderTest {

	private PropertyFileHolder propertyFileHolder;
	@Before
	public void setUp() throws Exception {
		propertyFileHolder = new PropertyFileHolder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetName() {
		propertyFileHolder.setName("UnitTest");
		assertNotNull(propertyFileHolder.getName());
		assertEquals("UnitTest",propertyFileHolder.getName());
	}

	@Test
	public void testGetPropertyInfo() {
		assertNotNull(propertyFileHolder.getPropertyInfo());
	}

	@Test
	public void testSetName() {
		propertyFileHolder.setName("UnitTest");
		assertNotNull(propertyFileHolder.getName());
	}

}
