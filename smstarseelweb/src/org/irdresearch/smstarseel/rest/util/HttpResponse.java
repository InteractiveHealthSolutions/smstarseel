package org.irdresearch.smstarseel.rest.util;

public class HttpResponse {
    private final boolean isSuccess;
    private final String body;
	private int statusCode;

    public HttpResponse(boolean isSuccess, int statusCode, String body) {
        this.isSuccess = isSuccess;
        this.body = body;
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String body() {
        return body;
    }
    
    public int statusCode() {
        return statusCode;
    }
}
