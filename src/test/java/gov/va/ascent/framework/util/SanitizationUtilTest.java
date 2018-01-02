package gov.va.ascent.framework.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Sanitization utility Test class
 */
@RunWith(SpringRunner.class)
public class SanitizationUtilTest {

	@Test
	public void testSanitize() {
		String str = "test string";
		assertTrue(str.equals(SanitizationUtil.stripXSS(str)));
		
		str = "test \nstring";
		assertTrue("test \nstring".equals(SanitizationUtil.stripXSS(str)));
		
		str = null;
		assertNull(SanitizationUtil.stripXSS(str));
		
		str = "javascript:alert('Hello')";
		assertTrue("alert('Hello')".equals(SanitizationUtil.stripXSS(str)));
		
		str = "<script>alert('Hello')<script>";
		assertTrue("alert('Hello')".equals(SanitizationUtil.stripXSS(str)));
		
		str = "</script>";
		assertTrue("".equals(SanitizationUtil.stripXSS(str)));
	}
	
}
