package gov.va.ascent.framework.validation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ViolationMessagePartsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOriginalKey() {
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		assertNotNull(violationMessageParts.getOriginalKey());
		assertEquals("UnitTestKey",violationMessageParts.getOriginalKey());
	}

	@Test
	public void testSetOriginalKey() {
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		assertNotNull(violationMessageParts.getOriginalKey());
	}

	@Test
	public void testGetNewKey() {
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		assertNotNull(violationMessageParts.getOriginalKey());
		assertNotNull(violationMessageParts.getNewKey());
		assertEquals("UpdatedUnitTestKey",violationMessageParts.getNewKey());
	}

	@Test
	public void testSetNewKey() {
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		assertNotNull(violationMessageParts.getOriginalKey());
		assertNotNull(violationMessageParts.getNewKey());
	}

	@Test
	public void testGetText() {
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		violationMessageParts.setText("Unit Testing");
		assertNotNull(violationMessageParts.getOriginalKey());
		assertNotNull(violationMessageParts.getNewKey());
		assertNotNull(violationMessageParts.getText());
		assertEquals("Unit Testing",violationMessageParts.getText());
	}

	@Test
	public void testSetText() {
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		violationMessageParts.setText("Unit Testing");
		assertNotNull(violationMessageParts.getOriginalKey());
		assertNotNull(violationMessageParts.getNewKey());
		assertNotNull(violationMessageParts.getText());
	}

	@Test
	public void testToString() {
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		violationMessageParts.setText("Unit Testing");
		assertNotNull(violationMessageParts.toString());		
		assertNotNull(violationMessageParts.getOriginalKey());
		assertNotNull(violationMessageParts.getNewKey());
		assertNotNull(violationMessageParts.getText());
		
	}

}
