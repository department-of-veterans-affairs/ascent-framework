package gov.va.ascent.framework.audit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author npaulus
 * The purpose of this class is to collect the audit data for a request and response before serializing it to the logs.
 */
@JsonInclude(Include.NON_NULL)
public class RequestResponseAuditData implements Serializable{

    private static final long serialVersionUID = -4623810801622309487L;

    /* A map of the http headers on the request. */
    private Map<String, String> headers;

    /*The uri of the request. */
    private String uri;

    /* The http method. */
    private String method;

    /* The response. */
    private transient Object response;

    /* The request. */
    private transient Object request;
    
    private List<String> attachmentTextList;

	/**
     * Gets the response.
     * @return
     */
    public Object getResponse() {
        return response;
    }

    /**
     * Sets the response.
     * @param response
     */
    public void setResponse(Object response) {
        this.response = response;
    }

    /**
     * Gets the http headers.
     * @return
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the http headers.
     * @param headers
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Gets the request uri.
     * @return
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets the uri.
     * @param uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Gets the http method.
     * @return
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the http method.
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Gets the request.
     * @return
     */
    public Object getRequest() {
        return request;
    }

    /**
     * Sets the request.
     * @param request
     */
    public void setRequest(Object request) {
        this.request = request;
    }
    
    /**
     * gets the attachmentTextList.
     * @return
     */
	public List<String> getAttachmentTextList() {
		return attachmentTextList;
	}

	/**
	 * sets the attachmentTextList.
	 * @param attachmentTextList
	 */
	public void setAttachmentTextList(List<String> attachmentTextList) {
		this.attachmentTextList = attachmentTextList;
	}
	
    @Override
    public String toString() {
        return "RequestResponseAuditData{" +
                "headers=" + headers +
                ", uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                ", response=" + response +
                ", request=" + request +
                '}';
    }
}
