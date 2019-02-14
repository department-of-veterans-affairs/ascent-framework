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
	// NOSONAR not duplicate
	public AbstractParamsException(final String message, final Throwable cause) {
		super(message, cause);
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
	 * @return Integer the number of elements in the name and value arrays
	 */
	// NOSONAR not duplicate
	public Integer getParamCount() {
		return paramCount;
	}

	/**
	 * Number of elements in the getParamNames() and getParamValues() arrays.
	 *
	 * @param paramCount the number of elements in the name and value arrays
	 */
	// NOSONAR not duplicate
	public void setParamCount(Integer paramCount) {
		this.paramCount = paramCount;
	}
}
