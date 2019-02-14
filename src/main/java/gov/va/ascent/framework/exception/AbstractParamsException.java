package gov.va.ascent.framework.exception;

/**
 * Custom extension of RuntimeException so that we can raise this for exceptions we have no intention
 * of handling and need to raise but for some reason (i.e. checkstyle checks) cannot raise
 * java's RuntimeException or allow the original exception to simply propagate.
 * <p>
 * This class can be extended to provide expression names and values used in exception messages
 * that have replaceable parameters, e.g. "Some {0} message with {1} parameters".
 * <p>
 * Current use-case is for transmission of exceptions and messages back to JSR303 validation constraint messages.
 *
 * @author vanapalliv
 */
public class AbstractParamsException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2598842813684506349L;

	private Integer paramCount;  // NOSONAR cannot be final

	private String[] paramNames;  // NOSONAR cannot be final

	private String[] paramValues; // NOSONAR cannot be final

	/**
	 * Instantiates a new service exception. Param Names and Values must be set separately.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AbstractParamsException(final String message, final Throwable cause) { 	
		super(message, cause);
	}

	/**
	 * An in-order array of replaceable parameter names used in the message.
	 * These are the literal strings between the curly braces.
	 *
	 * @return String[] the names, in same order as thier respective getParamValues()
	 */
	public String[] getParamNames() { 	// NOSONAR not duplicate
		return paramNames;
	}

	/**
	 * An in-order array of replaceable parameter names used in the message.
	 * These are the literal strings between the curly braces.
	 *
	 * @param paramNames the names, in same order as thier respective getParamValues()
	 */
	public void setParamNames(String[] paramNames) { 	// NOSONAR not duplicate
		this.paramNames = paramNames;
	}

	/**
	 * An in-order array of replaceable parameter values used in the message.
	 * These are the values that replace the parameters in the message.
	 *
	 * @return String[] the values, in same order as their respective getParamNames()
	 */
	public String[] getParamValues() {  	// NOSONAR not duplicate
		return paramValues;
	}

	/**
	 * An in-order array of replaceable parameter values used in the message.
	 * These are the values that replace the parameters in the message.
	 *
	 * @param paramValues the values, in same order as their respective getParamNames()
	 */
	public void setParamValues(String[] paramValues) {  	// NOSONAR not duplicate
		this.paramValues = paramValues;
	}

	/**
	 * Number of elements in the getParamNames() and getParamValues() arrays.
	 *
	 * @return Integer the number of elements in the name and value arrays
	 */
	public Integer getParamCount() {  	// NOSONAR not duplicate
		return paramCount;
	}

	/**
	 * Number of elements in the getParamNames() and getParamValues() arrays.
	 *
	 * @param paramCount the number of elements in the name and value arrays
	 */
	public void setParamCount(Integer paramCount) {  	// NOSONAR not duplicate
		this.paramCount = paramCount;
	}
}
