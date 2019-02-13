package gov.va.ascent.framework.exception;

/**
 * Custom extension of RuntimeException so that we can raise this for exceptions we have no intention
 * of handling and need to raise but for some reason (i.e. checkstyle checks) cannot raise
 * java's RuntimeException or allow the original exception to simply propagate.
 *
 * @author vanapalliv
 */
public class AbstractParamsException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2598842813684506349L;
	
	private Integer paramCount;

	private String[] paramNames;
	
	private String[] paramValues;


	/**
	 * Instantiates a new service exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AbstractParamsException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String[] getParamValues() {
		return paramValues;
	}

	public void setParamValues(String[] paramValues) {
		this.paramValues = paramValues;
	}	

	public Integer getParamCount() {
		return paramCount;
	}

	public void setParamCount(Integer paramCount) {
		this.paramCount = paramCount;
	}


}
