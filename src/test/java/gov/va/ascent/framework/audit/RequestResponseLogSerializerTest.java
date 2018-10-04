package gov.va.ascent.framework.audit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.messages.MessageSeverity;

@RunWith(SpringRunner.class)
public class RequestResponseLogSerializerTest {

	@SuppressWarnings("rawtypes")
	@Mock
	private ch.qos.logback.core.Appender mockAppender;
	// Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
	@Captor
	private ArgumentCaptor<ch.qos.logback.classic.spi.LoggingEvent> captorLoggingEvent;

	@Spy
	ObjectMapper mapper = new ObjectMapper();

	@InjectMocks
	private RequestResponseLogSerializer requestResponseLogSerializer = new RequestResponseLogSerializer();

	AuditEventData auditEventData = new AuditEventData(AuditEvents.REQUEST_RESPONSE, "MethodName", "ClassName");

	AuditEventData auditServiceEventData = new AuditEventData(AuditEvents.SERVICE_AUDIT, "MethodName", "ClassName");

	RequestResponseAuditData requestResponseAuditData = new RequestResponseAuditData();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		AscentLoggerFactory.getLogger(AscentLogger.ROOT_LOGGER_NAME).getLoggerBoundImpl().addAppender(mockAppender);

		requestResponseAuditData.setRequest(Arrays.asList("Request"));
		requestResponseAuditData.setResponse("Response");
		requestResponseAuditData.setMethod("GET");
		requestResponseAuditData.setUri("/");
		requestResponseAuditData.setAttachmentTextList(new ArrayList<String>(Arrays.asList("attachment1", "attachment2")));
		Map<String, String> headers = new HashMap<>();

		headers.put("Header1", "Header1Value");
		requestResponseAuditData.setHeaders(headers);

		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		ReflectionTestUtils.setField(requestResponseLogSerializer, "dateFormat", "yyyy-MM-dd'T'HH:mm:ss");
	}

	@SuppressWarnings("unchecked")
	@After
	public void teardown() {
		AscentLoggerFactory.getLogger(AscentLogger.ROOT_LOGGER_NAME).getLoggerBoundImpl().detachAppender(mockAppender);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testJson() throws Exception {
		requestResponseLogSerializer.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData,
				MessageSeverity.INFO);
		verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
		final List<ch.qos.logback.classic.spi.LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
		JSONAssert.assertEquals(
				"{\"headers\":{\"Header1\":\"Header1Value\"},\"uri\":\"/\",\"method\":\"GET\",\"response\":\"Response\",\"request\":[\"Request\"],\"attachmentTextList\":[\"attachment1\",\"attachment2\"]}",
				loggingEvents.get(0).getMessage(), JSONCompareMode.STRICT);
		assertThat(loggingEvents.get(0).getLevel(), is(ch.qos.logback.classic.Level.INFO));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testJsonException() throws Exception {
		when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
		requestResponseLogSerializer.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData,
				MessageSeverity.INFO);
		verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
		final List<ch.qos.logback.classic.spi.LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
		assertTrue(loggingEvents.get(0).getMessage().startsWith("Error occurred on JSON processing, calling toString"));
		assertThat(loggingEvents.get(0).getLevel(), is(ch.qos.logback.classic.Level.ERROR));
		assertEquals(
				"RequestResponseAuditData{headers={Header1=Header1Value}, uri='/', method='GET', response=Response, request=[Request]}",
				loggingEvents.get(1).getMessage());
		assertThat(loggingEvents.get(1).getLevel(), is(ch.qos.logback.classic.Level.INFO));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testJsonExceptionError() throws Exception {
		when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
		requestResponseLogSerializer.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData,
				MessageSeverity.ERROR);
		verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
		final List<ch.qos.logback.classic.spi.LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
		assertTrue(loggingEvents.get(0).getMessage().startsWith("Error occurred on JSON processing, calling toString"));
		assertThat(loggingEvents.get(0).getLevel(), is(ch.qos.logback.classic.Level.ERROR));
		assertEquals(
				"RequestResponseAuditData{headers={Header1=Header1Value}, uri='/', method='GET', response=Response, request=[Request]}",
				loggingEvents.get(1).getMessage());
		assertThat(loggingEvents.get(1).getLevel(), is(ch.qos.logback.classic.Level.ERROR));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testJsonExceptionFatal() throws Exception {
		when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
		requestResponseLogSerializer.asyncLogRequestResponseAspectAuditData(auditEventData, requestResponseAuditData,
				MessageSeverity.FATAL);
		verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
		final List<ch.qos.logback.classic.spi.LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
		assertTrue(loggingEvents.get(0).getMessage().startsWith("Error occurred on JSON processing, calling toString"));
		assertThat(loggingEvents.get(0).getLevel(), is(ch.qos.logback.classic.Level.ERROR));
		assertEquals(
				"RequestResponseAuditData{headers={Header1=Header1Value}, uri='/', method='GET', response=Response, request=[Request]}",
				loggingEvents.get(1).getMessage());
		assertThat(loggingEvents.get(1).getLevel(), is(ch.qos.logback.classic.Level.ERROR));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testServiceMessage() throws Exception {
		requestResponseLogSerializer.asyncLogMessageAspectAuditData(auditServiceEventData, "This is test", MessageSeverity.INFO);

		verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
		final List<ch.qos.logback.classic.spi.LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
		Assert.assertEquals("This is test", loggingEvents.get(0).getMessage());
		assertThat(loggingEvents.get(0).getLevel(), is(ch.qos.logback.classic.Level.INFO));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testServiceMessageError() throws Exception {
		requestResponseLogSerializer.asyncLogMessageAspectAuditData(auditServiceEventData, "Error test", MessageSeverity.ERROR);

		verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
		final List<ch.qos.logback.classic.spi.LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();
		Assert.assertEquals("Error test", loggingEvents.get(0).getMessage());
		assertThat(loggingEvents.get(0).getLevel(), is(ch.qos.logback.classic.Level.ERROR));
	}
}
