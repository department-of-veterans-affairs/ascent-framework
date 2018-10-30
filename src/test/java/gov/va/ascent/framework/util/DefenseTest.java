package gov.va.ascent.framework.util;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefenseTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsInstanceOf() {
		try{
			Defense defenseObj = Defense.class.newInstance();
			Defense.isInstanceOf(Defense.class, defenseObj);
		}catch(Exception e) {
			
		}
	}

	@Test
	public void testStateBooleanString() {
		Defense.state(true, "Boolean Condition is not satisfied");
	}
	
	@Test
	public void testStateBooleanStringForException() {
		try{
			Defense.state(false, "Boolean Condition is not satisfied");
		}catch(IllegalStateException e) {
			assertTrue(e.getMessage().equals("Boolean Condition is not satisfied"));
		}
	}	

	@Test
	public void testStateBoolean() {
		Defense.state(true);
	}

	@Test
	public void testIsNullObject() {
		Defense.isNull(null);
	}

	@Test
	public void testIsNullObjectString() {
		Defense.isNull(null, "Object should be null");
	}

	@Test
	public void testNotNullObject() {
		Defense.notNull(this);
	}

	@Test
	public void testNotNullObjectString() {
		Defense.notNull(this, "Object cannot be null");
	}
	
	@Test
	public void testNotNullObjectStringForException() {
		try {
			Defense.notNull(null, "Object cannot be null");
		}catch(IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("Object cannot be null"));
		}
	}	

	@Test
	public void testHasTextString() {
		Defense.hasText("Test Message");
	}

	@Test
	public void testHasTextStringString() {
		Defense.hasText("Test", "Missing Text");
	}

	@Test
	public void testHasTextStringStringForException() {
		try {
			Defense.hasText("", "Text cannot be blank");
		}catch(IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("Text cannot be blank"));
		}
		
	}
	
	
	@Test
	public void testNotEmptyCollectionOfQ() {
		List<String> dummyList = new ArrayList<String>();
		dummyList.add("value1");
		Defense.notEmpty(dummyList);
	}

	@Test
	public void testNotEmptyCollectionOfQString() {
		List<String> dummyList = new ArrayList<String>();
		dummyList.add("value1");
		Defense.notEmpty(dummyList,"Dummy List cannot be empty");
	}
	
	@Test
	public void testNotEmptyCollectionOfQStringForException() {
		try {
			List<String> dummyList = new ArrayList<String>();
			Defense.notEmpty(dummyList,"Dummy List cannot be empty");
		}catch(IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("Dummy List cannot be empty"));
		}
	}	

	@Test
	public void testNotEmptyStringArrayString() {
		String[] strArr = {"value1,value2"}; 
		Defense.notEmpty(strArr, "Array cannot be empty");
	}
	
	@Test
	public void testNotEmptyStringArrayStringForException() {
		try{
			String[] strArr = null;
			Defense.notEmpty(strArr, "Array cannot be empty");
		}catch(IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("Array cannot be empty"));
		}
	}	

	@Test
	public void testIsTrueBoolean() {
		Defense.isTrue(true);
	}

	@Test
	public void testIsTrueBooleanString() {
		try{
			Defense.isTrue(false, "Boolean condition not met");
			
		}catch(IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("Boolean condition not met"));
		}
	}

}
