package gov.va.ascent.framework.exception;

public class BaseAscentRuntimeExceptionTest {
	
	protected static final String EXCEPTION_MESSAGE = "This is the message passed into the exception constructor!";
	protected static final String TEST_CATEGORY = "Unit Test Category";

	public BaseAscentRuntimeExceptionTest() {
		super();
	}

	/**
	 * Strip all the identifier data added by the AscentRuntimeException class so we can validate the message itself.
	 * 
	 * @param message
	 * @return
	 */
	protected String getJustMessage(final String message) {
		final String pattern = "] ";
		if (message == null) {
			return null;
		}
		int index = message.lastIndexOf(pattern);
		if (index == message.length() - pattern.length()) {
			return null;
		} else {
			return message.substring(index + pattern.length());
		}
	}

}