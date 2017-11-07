package gov.va.ascent.framework.audit;

import java.io.Serializable;

public class RequestResponseAuditData implements Serializable{

    private static final long serialVersionUID = -4623810801622309487L;
    private HttpRequestAuditData httpRequestAuditData;

    private Object request;

    private Object response;


    public HttpRequestAuditData getHttpRequestAuditData() {
        return httpRequestAuditData;
    }

    public void setHttpRequestAuditData(HttpRequestAuditData httpRequestAuditData) {
        this.httpRequestAuditData = httpRequestAuditData;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "RequestResponseAuditData{" +
                "httpRequestAuditData=" + httpRequestAuditData +
                ", request=" + request +
                ", response=" + response +
                '}';
    }
}
