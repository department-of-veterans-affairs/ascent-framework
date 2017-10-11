package gov.va.ascent.framework.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class LogTestAppender extends AppenderBase<LoggingEvent> {
    static List<LoggingEvent> events = new ArrayList<>();

    @Override
    protected void append(LoggingEvent e) {
        events.add(e);
    }
}
