package gov.va.ascent.framework.messages;

import gov.va.ascent.framework.transfer.AbstractTransferObject;

/**
 * Message is a generic abstraction of a "message" or "notification" which is layer agnostic and can be used to communicate status or
 * other sorts of information during method calls between components/layers. This is serializable and can be used in SOAP or REST
 * calls. This class has param names and values as lists.
 *
 * @author vanapalliv
 */
public abstract class AbstractMessage extends AbstractTransferObject {

	/** The Constant serialVersisonUID. */
	private static final long serialVersionUID = -1711431368372127556L;
	
	private Integer paramCount = 0;

	private String[] paramNames;
	
	private String[] paramValues;
	
	/**
	 * @param paramCount
	 * @param paramNames
	 * @param paramValues
	 */
	public AbstractMessage(Integer paramCount, String[] paramNames, String[] paramValues) {
		super();
		this.paramCount = paramCount;
		this.paramNames = paramNames;
		this.paramValues = paramValues;
	}
	
	public AbstractMessage() {
		super();
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
