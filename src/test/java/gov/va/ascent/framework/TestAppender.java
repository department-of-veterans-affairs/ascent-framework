package gov.va.ascent.framework;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class TestAppender extends AppenderBase<LoggingEvent> {
	/**
	 * The events list must be static, otherwise it will not work.
	 * You must clear the list at the beginning of each test, or old events may still be on the list.
	 */
	static List<LoggingEvent> events = new ArrayList<>();

	/**
	 * Appends the specified {@link LoggingEvent} to the end of this list (optional operation).
	 *
	 * @param eventObject the LoggingEvent
	 */
	@Override
	public void append(LoggingEvent eventObject) {
		events.add(eventObject);
	}

	/** Remove all {@link LoggingEvent}s from the appended events. */
	public void clear() {
		events.clear();
	}

	/**
	 * Get a specific {@link LoggingEvent} by index.
	 *
	 * @param index the zero-based index
	 * @return LoggingEvent
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public LoggingEvent get(int index) {
		return events.get(index);
	}

	/**
	 * Returns the number of elements in this list. If this list contains more than Integer.MAX_VALUE elements, returns
	 * Integer.MAX_VALUE.
	 */
	public int size() {
		return events.size();
	}

	/**
	 * Returns {@code true} if this list contains no {@link LoggingEvent} elements.
	 *
	 * @return boolean
	 */
	public boolean isEmpty() {
		return events.isEmpty();
	}
}
