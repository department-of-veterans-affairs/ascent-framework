package gov.va.ascent.framework.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
		}catch(NoSuchAlgorithmException e) {
			
		}
	}

}
