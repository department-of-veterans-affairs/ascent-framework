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

	@Test
	public void testSafeFilename() {

		// copied from the method under test
		final int[] illegalChars = { 34, 60, 62, 124, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
				23, 24, 25, 26, 27, 28, 29, 30, 31, 58, 42, 63, 92, 47 };

		final StringBuilder illegalString = new StringBuilder();
		for (int i = 0; i < illegalChars.length; i++) {
			illegalString.append((char) illegalChars[i]);
		}

		final String filename = "Test " + illegalString.toString() + " file.name";

		final String safe = SanitizationUtil.safeFilename(filename);

		assertTrue("Test  file.name".equals(safe));
	}

}
