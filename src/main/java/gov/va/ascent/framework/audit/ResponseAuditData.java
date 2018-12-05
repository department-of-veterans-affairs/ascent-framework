package gov.va.ascent.framework.audit;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The purpose of this class is to collect the audit data for a response before serializing it to the logs.
 */
@JsonInclude(Include.NON_NULL)
public class ResponseAuditData implements Serializable, AuditableData {

	private static final long serialVersionUID = 3362363966640647082L;

	/* A map of the http headers on the response. */
	private Map<String, String> headers;

	/* The response. */
	private transient Object response;

	/**
	 * Gets the http headers.
	 *
	 * @return
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Sets the http headers.
	 *
	 * @param headers
	 */
	public void setHeaders(final Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * Gets the response.
	 *
	 * @return
	 */
	public Object getResponse() {
		return response;
	}

	/**
	 * Sets the response.
	 *
	 * @param response
	 */
	public void setResponse(final Object response) {
		this.response = response;
	}

	/**
	 * Manually formatted JSON-like string of key/value pairs.
	 */
	@Override
	public String toString() {
		return "ResponseAuditData{" + "headers=" + (headers == null ? "" : ReflectionToStringBuilder.toString(headers)) + ", uri='"
				+ ", response=" + (response == null ? "" : ReflectionToStringBuilder.toString(response)) + '}';
	}
}
