package gov.va.ascent.framework.messages;

import gov.va.ascent.framework.transfer.AbstractTransferObject;

/**
 * Message is a generic abstraction of a "message" or "notification" which is layer agnostic and can be used to communicate status or
 * other sorts of information during method calls between components/layers. This is serializable and can be used in SOAP or REST
 * calls. This class has param names and values as lists.
 * <p>
 * This class can be extended to provide expression names and values used in messages
 * that have replaceable parameters, e.g. "Some {0} message with {1} parameters".
 * <p>
 * Current use-case is for message transmission back to JSR303 validation constraint messages.
 *
 * @author vanapalliv
 */
public abstract class AbstractMessage extends AbstractTransferObject {

	/** The Constant serialVersisonUID. */
	private static final long serialVersionUID = -1711431368372127556L;

	private Integer paramCount = 0; // NOSONAR cannot be final

	private String[] paramNames; // NOSONAR cannot be final

	private String[] paramValues; // NOSONAR cannot be final

	/**
	 * Construct a message providing only replaceable parameters.
	 * 
	 * @param paramCount
	 * @param paramNames
	 * @param paramValues
	 */
	// NOSONAR not duplicate
	public AbstractMessage(Integer paramCount, String[] paramNames, String[] paramValues) {
		super();
		this.paramCount = paramCount;
		this.paramNames = paramNames;
		this.paramValues = paramValues;
	}

	/**
	 * Construct default (empty) message object.
	 */
	// NOSONAR not duplicate
	public AbstractMessage() {
		super();
	}

	/**
	 * An in-order array of replaceable parameter names used in the message.
	 * These are the literal strings between the curly braces.
	 * 
	 * @return String[] the names, in same order as thier respective getParamValues()
	 */
	// NOSONAR not duplicate
	public String[] getParamNames() {
		return paramNames;
	}

	/**
	 * An in-order array of replaceable parameter names used in the message.
	 * These are the literal strings between the curly braces.
	 * 
	 * @param paramNames the names, in same order as thier respective getParamValues()
	 */
	// NOSONAR not duplicate
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	/**
	 * An in-order array of replaceable parameter values used in the message.
	 * These are the values that replace the parameters in the message.
	 * 
	 * @return String[] the values, in same order as their respective getParamNames()
	 */
	// NOSONAR not duplicate
	public String[] getParamValues() {
		return paramValues;
	}

	/**
	 * An in-order array of replaceable parameter values used in the message.
	 * These are the values that replace the parameters in the message.
	 * 
	 * @param paramValues the values, in same order as their respective getParamNames()
	 */
	// NOSONAR not duplicate
	public void setParamValues(String[] paramValues) {
		this.paramValues = paramValues;
	}

	/**
	 * Number of elements in the getParamNames() and getParamValues() arrays.
	 * 
	 * @return Integer the number of elements in the arrays
	 */
	// NOSONAR not duplicate
	public Integer getParamCount() {
		return paramCount;
	}

	/**
	 * Number of elements in the getParamNames() and getParamValues() arrays.
	 * 
	 * @param paramCount the number of elements in the arrays
	 */
	// NOSONAR not duplicates
	public void setParamCount(Integer paramCount) {
		this.paramCount = paramCount;
	}
}
