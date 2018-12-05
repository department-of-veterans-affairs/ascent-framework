package gov.va.ascent.framework.audit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The purpose of this class is to collect the audit data for a request before serializing it to the logs.
 */
@JsonInclude(Include.NON_NULL)
public class RequestAuditData implements Serializable, AuditableData {

	private static final long serialVersionUID = -6346123934909781965L;

	/* A map of the http headers on the request. */
	private Map<String, String> headers;

	/* The uri of the request. */
	private String uri;

	/* The http method. */
	private String method;

	/* The request. */
	private transient List<Object> request;

	private List<String> attachmentTextList;

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
	 * Gets the request uri.
	 *
	 * @return
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Sets the uri.
	 *
	 * @param uri
	 */
	public void setUri(final String uri) {
		this.uri = uri;
	}

	/**
	 * Gets the http method.
	 *
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the http method.
	 *
	 * @param method
	 */
	public void setMethod(final String method) {
		this.method = method;
	}

	/**
	 * Gets the request.
	 *
	 * @return
	 */
	public List<Object> getRequest() {
		return request;
	}

	/**
	 * Sets the request.
	 *
	 * @param request
	 */
	public void setRequest(final List<Object> request) {
		this.request = request;
	}

	/**
	 * gets the attachmentTextList.
	 *
	 * @return
	 */
	public List<String> getAttachmentTextList() {
		return attachmentTextList;
	}

	/**
	 * sets the attachmentTextList.
	 *
	 * @param attachmentTextList
	 */
	public void setAttachmentTextList(final List<String> attachmentTextList) {
		this.attachmentTextList = attachmentTextList;
	}

	/**
	 * Manually formatted JSON-like string of key/value pairs.
	 */
	@Override
	public String toString() {
		return "RequestAuditData{" + "headers=" + (headers == null ? "" : ReflectionToStringBuilder.toString(headers)) + ", uri='"
				+ uri + "\'" + ", method='" + method + "\'" + ", request="
				+ (request == null ? "" : ReflectionToStringBuilder.toString(request)) + '}';
	}
}
