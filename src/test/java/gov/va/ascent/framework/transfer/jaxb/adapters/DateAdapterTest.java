package gov.va.ascent.framework.transfer.jaxb.adapters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateAdapterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
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

}
