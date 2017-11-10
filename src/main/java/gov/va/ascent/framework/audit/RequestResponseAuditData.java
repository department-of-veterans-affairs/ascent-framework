package gov.va.ascent.framework.audit;

import java.io.Serializable;
import java.util.Map;

/**
 * @author npaulus
 * The purpose of this class is to collect the audit data for a request and response before serializing it to the logs.
 */
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
