package gov.va.ascent.framework.service;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.transfer.AbstractTransferObject;
import gov.va.ascent.framework.transfer.ServiceTransferObjectMarker;

/**
 * A base Response object capable of representing the payload of a service response.
 *
 * @see gov.va.ascent.framework.transfer.AbstractTransferObject
 */
public class ServiceResponse extends AbstractTransferObject implements ServiceTransferObjectMarker {

	private static final long serialVersionUID = -3937937807439785385L;

	/** The messages. */
	private List<Message> messages;

	/*
	 * cacheResponse
	 *
	 * Must be ignored in the serialization and de-serialization
	 */
	@JsonIgnore
	private boolean doNotCacheResponse = false;

	/**
	 * Instantiates a new rest response.
	 */
	public ServiceResponse() {
		super();
	}

	/**
	 * Adds the message.
	 *
	 * @param severity the severity
	 * @param key the key
	 * @param text the text
	 */
	public final void addMessage(final MessageSeverity severity, final String key, final String text) {
		if (messages == null) {
			messages = new LinkedList<>();
		}
		final Message message = new Message();
		message.setSeverity(severity);
		message.setKey(key);
		message.setText(text);
		messages.add(message);
	}
	
	/**
	 * Adds the message.
	 *
	 * @param severity the severity
	 * @param key the key
	 * @param text the text
	 */
	public final void addMessage(final MessageSeverity severity, final String key, final String text,
			Integer paramCount, String[] paramNames, String[] paramValues) {
		if (messages == null) {
			messages = new LinkedList<>();
		}
		final Message message = new Message();
		message.setSeverity(severity);
		message.setKey(key);
		message.setText(text);
		message.setParamCount(paramCount);
		message.setParamNames(paramNames);
		message.setParamValues(paramValues);
		messages.add(message);
	}

	/**
	 * Adds all messages.
	 *
	 * @param newMessages the newMessages
	 */
	public final void addMessages(final List<Message> newMessages) {
		if (messages == null) {
			messages = new LinkedList<>();
		}
		messages.addAll(newMessages);
	}

	/**
	 * Gets the messages.
	 *
	 * @return the messages
	 */
	public final List<Message> getMessages() {
		if (messages == null) {
			messages = new LinkedList<>();
		}
		return this.messages;
	}

	/**
	 * Sets the messages.
	 *
	 * @param messages the new messages
	 */
	public final void setMessages(final List<Message> messages) {
		this.messages = messages;
	}

	/**
	 * Checks for messages of type.
	 *
	 * @param severity the severity
	 * @return true, if successful
	 */
	private boolean hasMessagesOfType(final MessageSeverity severity) {
		for (final Message message : getMessages()) {
			if (severity.equals(message.getSeverity())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks for fatals.
	 *
	 * @return true, if successful
	 */
	public final boolean hasFatals() {
		return hasMessagesOfType(MessageSeverity.FATAL);
	}

	/**
	 * Checks for errors.
	 *
	 * @return true, if successful
	 */
	public final boolean hasErrors() {
		return hasMessagesOfType(MessageSeverity.ERROR);
	}

	/**
	 * Checks for warnings.
	 *
	 * @return true, if successful
	 */
	public final boolean hasWarnings() {
		return hasMessagesOfType(MessageSeverity.WARN);
	}

	/**
	 * Checks for infos.
	 *
	 * @return true, if successful
	 */
	public final boolean hasInfos() {
		return hasMessagesOfType(MessageSeverity.INFO);
	}

	/**
	 *
	 * @return
	 */
	public boolean isDoNotCacheResponse() {
		return doNotCacheResponse;
	}

	/**
	 *
	 * @param doNotcacheResponse
	 */
	public void setDoNotCacheResponse(final boolean doNotCacheResponse) {
		this.doNotCacheResponse = doNotCacheResponse;
	}

}
