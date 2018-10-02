package gov.va.ascent.framework.messages;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.event.Level;

public class MessageSeverityTest {

	@Test
	public final void testValue() {
		assertTrue("FATAL".equals(MessageSeverity.FATAL.value()));
	}

	@Test
	public final void testFromValue() {
		assertTrue(MessageSeverity.TRACE.equals(MessageSeverity.fromValue("TRACE")));
	}

	@Test
	public final void testGetLevel() {
		assertTrue(Level.ERROR.equals(MessageSeverity.FATAL.getLevel()));
		assertTrue(Level.TRACE.equals(MessageSeverity.TRACE.getLevel()));
	}

}
