package gov.va.ascent.framework.log;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AscentLogMarkersTest {

	@Test
	public final void testAscentLogMarkers() {
		assertNotNull(AscentLogMarkers.FATAL.getMarker());
		assertNotNull(AscentLogMarkers.EXCEPTION.getMarker());
		assertNotNull(AscentLogMarkers.TEST.getMarker());

		assertTrue("FATAL".equals(AscentLogMarkers.FATAL.getMarker().getName()));
		assertTrue("EXCEPTION".equals(AscentLogMarkers.EXCEPTION.getMarker().getName()));
		assertTrue("TEST".equals(AscentLogMarkers.TEST.getMarker().getName()));
	}

}
