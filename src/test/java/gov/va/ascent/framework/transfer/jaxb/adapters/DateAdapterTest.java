package gov.va.ascent.framework.transfer.jaxb.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.aspect.AspectLoggingTestAppender;
import gov.va.ascent.framework.aspect.PerformanceLoggingAspect;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateAdapterTest {

	private Logger dateAdapterLog = (Logger) org.slf4j.LoggerFactory.getLogger(DateAdapter.class);

	@Before
	public void setUp() throws Exception {
		DateAdapterLoggingTestAppender.events.clear();
	}

	@After
	public void tearDown() throws Exception {
		DateAdapterLoggingTestAppender.events.clear();
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

		Date dt = DateAdapter.parseDateTime("1970-0101T00:00:00");
		assertEquals("Error parsing date, returning null:1970-0101T00:00:00",
				DateAdapterLoggingTestAppender.events.get(0).getMessage());
		assertEquals(null, dt);
	}

}
