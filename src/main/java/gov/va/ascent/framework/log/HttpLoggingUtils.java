package gov.va.ascent.framework.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.transport.TransportOutputStream;

import gov.va.ascent.framework.audit.AuditEventData;
import gov.va.ascent.framework.audit.AuditEvents;
import gov.va.ascent.framework.audit.RequestResponseLogSerializer;
import gov.va.ascent.framework.messages.MessageSeverity;

/**
 * Utilities class with some static methods to log a given web service http message
 */
public class HttpLoggingUtils {
	public static final String UNABLE_TO_LOG_HTTP_MESSAGE_TEXT = "Unable to log HTTP message.";

	public static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(HttpLoggingUtils.class);

	private static final String NEW_LINE = System.getProperty("line.separator");

	static RequestResponseLogSerializer asyncLogging = new RequestResponseLogSerializer();

	private HttpLoggingUtils() {
	}

	public static void logMessage(final String id, final WebServiceMessage webServiceMessage, final String auditActivity,
			final String auditClassName) {
		try {
			ByteArrayTransportOutputStream byteArrayTransportOutputStream = new ByteArrayTransportOutputStream();
			webServiceMessage.writeTo(byteArrayTransportOutputStream);

			String httpMessage = new String(byteArrayTransportOutputStream.toByteArray(), "ISO-8859-1");
			AuditEventData auditEventData = new AuditEventData(AuditEvents.PARTNER_REQUEST, auditActivity, auditClassName);
			asyncLogging.asyncLogMessageAspectAuditData(auditEventData, NEW_LINE + "----------------------------" + NEW_LINE + id
					+ NEW_LINE + "----------------------------" + NEW_LINE + httpMessage + NEW_LINE, MessageSeverity.INFO);
		} catch (Exception e) {
			LOGGER.error(UNABLE_TO_LOG_HTTP_MESSAGE_TEXT, e);
		}
	}

	public static class ByteArrayTransportOutputStream extends TransportOutputStream {

		private ByteArrayOutputStream byteArrayOutputStream;

		@Override
		public void addHeader(final String name, final String value) throws IOException {
			createOutputStream();
			String header = name + ": " + value + NEW_LINE;
			byteArrayOutputStream.write(header.getBytes());
		}

		@Override
		protected OutputStream createOutputStream() throws IOException {
			if (byteArrayOutputStream == null) {
				byteArrayOutputStream = new ByteArrayOutputStream();
			}
			return byteArrayOutputStream;
		}

		public byte[] toByteArray() {
			return byteArrayOutputStream.toByteArray();
		}
	}
}
