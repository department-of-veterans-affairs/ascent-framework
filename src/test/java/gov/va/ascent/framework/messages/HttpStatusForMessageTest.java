package gov.va.ascent.framework.messages;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HttpStatusForMessageTest {

	@Test
	public void testGetReasonPhrase() throws Exception {
		assertTrue(HttpStatusForMessage.BAD_REQUEST.getReasonPhrase().equals("Bad Request"));
	}

	@Test
	public void testValue() throws Exception {
		assertTrue(HttpStatusForMessage.BAD_REQUEST.value() == 400);
	}

	@Test
	public void testIs1xxInformational() throws Exception {
		assertTrue(HttpStatusForMessage.CONTINUE.is1xxInformational());
	}

	@Test
	public void testIs2xxSuccessful() throws Exception {
		assertTrue(HttpStatusForMessage.OK.is2xxSuccessful());
	}

	@Test
	public void testIs3xxRedirection() throws Exception {
		assertTrue(HttpStatusForMessage.MOVED_PERMANENTLY.is3xxRedirection());
	}

	@Test
	public void testIs4xxClientError() throws Exception {
		assertTrue(HttpStatusForMessage.BAD_REQUEST.is4xxClientError());
	}

	@Test
	public void testIs5xxServerError() throws Exception {
		assertTrue(HttpStatusForMessage.INTERNAL_SERVER_ERROR.is5xxServerError());
	}

	@Test
	public void testSeries() throws Exception {
		assertTrue(HttpStatusForMessage.BAD_REQUEST.series() == HttpStatusForMessage.Series.CLIENT_ERROR);
	}

	@Test
	public void testToString() throws Exception {
		assertTrue(HttpStatusForMessage.BAD_REQUEST.toString().equals("400"));
	}

	@Test
	public void testValueOf() throws Exception {
		assertTrue(HttpStatusForMessage.valueOf(400) == HttpStatusForMessage.BAD_REQUEST);
	}

	@Test
	public void testSeriesValueOf() throws Exception {
		assertTrue(HttpStatusForMessage.Series.valueOf(400) == HttpStatusForMessage.Series.CLIENT_ERROR);
	}

}
