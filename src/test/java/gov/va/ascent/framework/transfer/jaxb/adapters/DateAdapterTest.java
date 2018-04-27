package gov.va.ascent.framework.transfer.jaxb.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.va.ascent.framework.AbstractBaseLogTester;

public class DateAdapterTest extends AbstractBaseLogTester {

	@Before
	public void setUp() throws Exception {
	}

	@Override
	@After
	public void tearDown() {
	}

	@Test
	public void testParseDate() {
		Date dt = DateAdapter.parseDate("1970-01-01");
		assertNotNull(dt);
	}

	@Test
	public void testPrintDate() {
		String dateValStr = DateAdapter.printDate(Calendar.getInstance().getTime());
		assertNotNull(dateValStr);
		assertTrue(dateValStr.contains("-"));
	}

	@Test
	public void testParseDateTime() {
		Date dt = DateAdapter.parseDateTime("1970-01-01T00:00:00");
		assertNotNull(dt);
	}

	@Test
	public void testPrintDateTime() {
		String retVal = DateAdapter.printDateTime(Calendar.getInstance().getTime());
		assertNotNull(retVal);
		assertTrue(retVal.contains("-"));
		assertTrue(retVal.contains(":"));
	}

	@Test
	public void testParseDateTimeThrowsParseException() {
		super.getAppender().clear();

		Date dt = DateAdapter.parseDateTime("1970-0101T00:00:00");
		assertEquals("Error parsing date, returning null:1970-0101T00:00:00", super.getAppender().get(0).getMessage());
		assertEquals(null, dt);
	}
	
	@Test
	public void testGetDateFormat() {
		DateFormat dateFormat = DateAdapter.getDateFormat();
		assertNotNull(dateFormat);
	}

}
