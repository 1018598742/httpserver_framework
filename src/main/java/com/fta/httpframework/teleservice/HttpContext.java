package com.fta.httpframework.teleservice;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class HttpContext {
    private final HashMap<String, String> requestHeaders;
    private Socket underlySocket;

    public HttpContext() {
        requestHeaders = new HashMap<>();
    }

    public void setUnderlySocket(Socket remotePeer) {
        this.underlySocket = remotePeer;
    }

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public void addRequestHeader(String headerName, String headerValue) {
        requestHeaders.put(headerName, headerValue);
    }

    public String getRequestHeaderValue(String headerName) {
        return requestHeaders.get(headerName);
    }
}
