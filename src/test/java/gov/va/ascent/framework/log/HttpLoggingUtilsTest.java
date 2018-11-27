package gov.va.ascent.framework.log;

import static gov.va.ascent.framework.log.HttpLoggingUtils.UNABLE_TO_LOG_HTTP_MESSAGE_TEXT;
import static gov.va.ascent.framework.log.HttpLoggingUtils.logMessage;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.WebServiceMessage;

import gov.va.ascent.framework.log.HttpLoggingUtils.ByteArrayTransportOutputStream;

@RunWith(SpringRunner.class)
public class HttpLoggingUtilsTest {

	private static final String TEST_SAMPLE_AUDIT_CLASS_NAME = "test audit class";

	private static final String TEST_SAMPLE_AUDIT_ACTIVITY = "test audit activity";

	private static final String TEST_SAMPLE_SOAP_MESSAGE = "test sample soap message";

	private static final String TEST_STARTING_TEXT = "Test starting text";

	/** The output capture. */
	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Mock
	private WebServiceMessage request;

	@Test
	public void logMessageTest() {

		try {
			doAnswer((Answer) invocation -> {
				ByteArrayTransportOutputStream arg0 =
						invocation.getArgumentAt(0, HttpLoggingUtils.ByteArrayTransportOutputStream.class);
				arg0.write(TEST_SAMPLE_SOAP_MESSAGE.getBytes("UTF-8"));
				return null;
			}).when(request).writeTo(any(HttpLoggingUtils.ByteArrayTransportOutputStream.class));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Should not throw execption");
		}

		logMessage(TEST_STARTING_TEXT, request, TEST_SAMPLE_AUDIT_ACTIVITY, TEST_SAMPLE_AUDIT_CLASS_NAME);

		try {
			verify(request).writeTo(any(HttpLoggingUtils.ByteArrayTransportOutputStream.class));
		} catch (IOException e) {
			fail("Should not throw execption");
		}

		final String outString = outputCapture.toString();

		assertTrue(outString.contains(TEST_STARTING_TEXT));
		assertTrue(outString.contains(TEST_SAMPLE_SOAP_MESSAGE));

	}

	@Test
	public void logMessageThrowingExceptionTest() {
		logMessage(TEST_STARTING_TEXT, request, TEST_SAMPLE_AUDIT_ACTIVITY, TEST_SAMPLE_AUDIT_CLASS_NAME);

		final String outString = outputCapture.toString();

		assertTrue(outString.contains(UNABLE_TO_LOG_HTTP_MESSAGE_TEXT));

	}
}
