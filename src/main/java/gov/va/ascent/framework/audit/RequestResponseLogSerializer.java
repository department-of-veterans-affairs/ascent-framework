package gov.va.ascent.framework.audit;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.messages.MessageSeverity;

/**
 * @author npaulus
 * @author akulkarni
 *         The purpose of this class is to asynchronuously serialize an object to JSON and then write it to the audit logs.
 */
@Component
public class RequestResponseLogSerializer {

	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(RequestResponseLogSerializer.class);

	ObjectMapper mapper = new ObjectMapper();

	@Value("${spring.jackson.date-format:yyyy-MM-dd'T'HH:mm:ss.SSSZ}")
	private String dateFormat;

	/**
	 * Asynchronuously converts an object to JSON and then writes it to the audit logger.
	 * <p>
	 * <b>"Around" Advised by:</b>
	 * org.springframework.cloud.sleuth.instrument.async.TraceAsyncAspect.traceBackgroundThread(org.aspectj.lang.ProceedingJoinPoint)
	 *
	 * @param auditEventData Data specific to the audit event
	 * @param auditData The request and response audit data
	 */
	@Async
	public void asyncLogRequestResponseAspectAuditData(final AuditEventData auditEventData, final AuditableData auditData,
			final Class auditDataClass, final MessageSeverity messageSeverity, final Throwable t) {

		String auditDetails;
		try {
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			mapper.setDateFormat(new SimpleDateFormat(dateFormat, Locale.getDefault()));

			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			mapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
			mapper.disable(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS);

			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
			mapper.disable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES);

			auditDetails = mapper.writeValueAsString(auditDataClass.cast(auditData));
		} catch (JsonProcessingException ex) {
			LOGGER.error("Error occurred on JSON processing, calling custom toString()", ex);
			auditDetails = auditDataClass.cast(auditData).toString();
		}

		if (messageSeverity.equals(MessageSeverity.ERROR) || messageSeverity.equals(MessageSeverity.FATAL)) {
			AuditLogger.error(auditEventData, auditDetails, t);
		} else {
			AuditLogger.info(auditEventData, auditDetails);
		}
	}

	/**
	 * Asynchronuously writes to the audit logger.
	 * <p>
	 * <b>"Around" Advised by:</b>
	 * org.springframework.cloud.sleuth.instrument.async.TraceAsyncAspect.traceBackgroundThread(org.aspectj.lang.ProceedingJoinPoint)
	 *
	 * @param auditEventData Data specific to the audit event
	 * @param messageSeverity the message severity
	 * @param activityDetail the activity detail
	 */
	@Async
	public void asyncLogMessageAspectAuditData(final AuditEventData auditEventData, final String activityDetail,
			final MessageSeverity messageSeverity, final Throwable t) {

		if (messageSeverity.equals(MessageSeverity.ERROR) || messageSeverity.equals(MessageSeverity.FATAL)) {
			AuditLogger.error(auditEventData, activityDetail, t);
		} else {
			AuditLogger.info(auditEventData, activityDetail);
		}
	}

}
