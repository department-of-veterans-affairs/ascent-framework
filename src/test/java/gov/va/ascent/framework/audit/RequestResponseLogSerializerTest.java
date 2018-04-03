package gov.va.ascent.framework.audit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import gov.va.ascent.framework.messages.MessageSeverity;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RequestResponseLogSerializerTest {


    @SuppressWarnings("rawtypes")
    @Mock
    private Appender mockAppender;
    // Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Spy
    ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private RequestResponseLogSerializer requestResponseLogSerializer = new RequestResponseLogSerializer();

    AuditEventData auditEventData = new AuditEventData(AuditEvents.REQUEST_RESPONSE, "MethodName", "ClassName");
    RequestResponseAuditData requestResponseAuditData = new RequestResponseAuditData();

    @Before
    public void setUp() throws Exception {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
        requestResponseAuditData.setRequest(Arrays.asList("Request"));
        requestResponseAuditData.setResponse("Response");
        requestResponseAuditData.setMethod("GET");
        requestResponseAuditData.setUri("/");
        requestResponseAuditData.setAttachmentTextList(new ArrayList<String>(
        	    Arrays.asList("attachment1", "attachment2")));
        Map<String,String> headers = new HashMap<>();

        headers.put("Header1", "Header1Value");
        requestResponseAuditData.setHeaders(headers);
        
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

    }

    @SuppressWarnings("unchecked")
    @After
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void testJson() throws Exception {
        requestResponseLogSerializer.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData, MessageSeverity.INFO);
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
        System.out.println("HERE:  " + loggingEvents.get(0).getMessage());
        JSONAssert.assertEquals("{\"headers\":{\"Header1\":\"Header1Value\"},\"uri\":\"/\",\"method\":\"GET\",\"response\":\"Response\",\"request\":[\"Request\"],\"attachmentTextList\":[\"attachment1\",\"attachment2\"]}",
                loggingEvents.get(0).getMessage(), JSONCompareMode.STRICT);
        assertThat(loggingEvents.get(0).getLevel(), is(Level.INFO));
    }

    @Test
    public void testJsonException() throws Exception {
        when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        requestResponseLogSerializer.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData, MessageSeverity.INFO);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
        assertEquals("Error occurred on JSON processing, calling toString", loggingEvents.get(0).getMessage());
        assertThat(loggingEvents.get(0).getLevel(), is(Level.ERROR));
        assertEquals("RequestResponseAuditData{headers={Header1=Header1Value}, uri='/', method='GET', response=Response, request=[Request]}", loggingEvents.get(1).getMessage());
        assertThat(loggingEvents.get(1).getLevel(), is(Level.INFO));

    }

    @Test
    public void testJsonExceptionError() throws Exception {
        when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        requestResponseLogSerializer.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData, MessageSeverity.ERROR);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
        assertEquals("Error occurred on JSON processing, calling toString", loggingEvents.get(0).getMessage());
        assertThat(loggingEvents.get(0).getLevel(), is(Level.ERROR));
        assertEquals("RequestResponseAuditData{headers={Header1=Header1Value}, uri='/', method='GET', response=Response, request=[Request]}", loggingEvents.get(1).getMessage());
        assertThat(loggingEvents.get(1).getLevel(), is(Level.ERROR));
    }

    @Test
    public void testJsonExceptionFatal() throws Exception {
        when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        requestResponseLogSerializer.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData, MessageSeverity.FATAL);
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
        assertEquals("Error occurred on JSON processing, calling toString", loggingEvents.get(0).getMessage());
        assertThat(loggingEvents.get(0).getLevel(), is(Level.ERROR));
        assertEquals("RequestResponseAuditData{headers={Header1=Header1Value}, uri='/', method='GET', response=Response, request=[Request]}", loggingEvents.get(1).getMessage());
        assertThat(loggingEvents.get(1).getLevel(), is(Level.ERROR));
    }
}
