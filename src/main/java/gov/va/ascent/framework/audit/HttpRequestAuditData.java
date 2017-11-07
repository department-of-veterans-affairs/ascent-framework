package gov.va.ascent.framework.audit;

import java.io.Serializable;
import java.util.Map;

public class HttpRequestAuditData implements Serializable{

    private static final long serialVersionUID = -4394190104168904752L;
    private final Map<String, String> headers;

    private final String uri;

    private final String method;

    public HttpRequestAuditData(final Map<String, String> headers, final String uri,
                                final String method){
        this.headers = headers;
        this.uri = uri;
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "HttpRequestAuditData{" +
                "headers=" + headers +
                ", uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
