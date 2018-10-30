package gov.va.ascent.framework.log;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.event.Level;

public class AscentBaseLoggerTest {

	@Test
	public final void testGetSetLevel() {
		AscentLogger logger = AscentLoggerFactory.getLogger(AscentBanner.class);
		Level level = logger.getLevel();
		assertNotNull(level);
		logger.setLevel(Level.INFO);
		assertTrue(Level.INFO.equals(logger.getLevel()));
	}

}
