package gov.va.ascent.framework.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HashGeneratorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMd5ForString() {
		try {
			String encryptStr = HashGenerator.getMd5ForString("TestInputString");
			assertNotNull(encryptStr);
			assertFalse(encryptStr.equals("TestInputString"));
		} catch (NoSuchAlgorithmException e) {

		}
	}

	@Test
	public void testConstructor() {
		try {
			Constructor<?> constructor = HashGenerator.class.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			constructor.newInstance();
			fail("Should have thrown IllegalAccessError");
		} catch (IllegalAccessError | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			assertTrue(InvocationTargetException.class.equals(e.getClass()));
			assertTrue(IllegalAccessError.class.equals(e.getCause().getClass()));
		}
	}

}
