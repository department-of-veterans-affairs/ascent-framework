package gov.va.ascent.framework.exception;

import gov.va.ascent.framework.messages.MessageSeverity;

/**
 * AscentMessageException is used by services.
 * @author rthota
 *
 */
public class AscentMessageException extends RuntimeException {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8436650114143466441L;

	private MessageSeverity severity;
	private String key;
	private String message;

	public MessageSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(MessageSeverity severity) {
		this.severity = severity;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public AscentMessageException(MessageSeverity severity, String key, String message) {
		this.severity = severity;
		this.key = key;
		this.message = message;
	}

	public AscentMessageException(MessageSeverity severity, String message) {
		this.severity = severity;
		this.message = message;
	}
	
}
