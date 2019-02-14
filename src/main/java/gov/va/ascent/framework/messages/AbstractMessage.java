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

	private Integer parameterCount = 0; // NOSONAR cannot be final

	private String[] parameterNames; // NOSONsAR cannot be final

	private String[] parameterValues; // NOSONAR cannot be final

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
		this.parameterCount = paramCount;
		this.parameterNames = paramNames;
		this.parameterValues = paramValues;
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
	public String[] getParamNames() {  	// NOSONAR not duplicate
		return parameterNames;
	}

	/**
	 * An in-order array of replaceable parameter names used in the message.
	 * These are the literal strings between the curly braces.
	 * 
	 * @param paramNames the names, in same order as thier respective getParamValues()
	 */
	public void setParamNames(String[] paramNames) {  	// NOSONAR not duplicate
		this.parameterNames = paramNames;
	}

	/**
	 * An in-order array of replaceable parameter values used in the message.
	 * These are the values that replace the parameters in the message.
	 * 
	 * @return String[] the values, in same order as their respective getParamNames()
	 */
	public String[] getParamValues() {  	// NOSONAR not duplicate
		return parameterValues;
	}

	/**
	 * An in-order array of replaceable parameter values used in the message.
	 * These are the values that replace the parameters in the message.
	 * 
	 * @param paramValues the values, in same order as their respective getParamNames()
	 */
	public void setParamValues(String[] paramValues) { 	// NOSONAR not duplicate
		this.parameterValues = paramValues;
	}

	/**
	 * Number of elements in the getParamNames() and getParamValues() arrays.
	 * 
	 * @return Integer the number of elements in the arrays
	 */
	public Integer getParamCount() {  	// NOSONAR not duplicate
		return parameterCount;
	}

	/**
	 * Number of elements in the getParamNames() and getParamValues() arrays.
	 * 
	 * @param paramCount the number of elements in the arrays
	 */
	public void setParamCount(Integer paramCount) {  	// NOSONAR not duplicate
		this.parameterCount = paramCount;
	}
}
