package com.fta.httpframework.teleservice;

import java.io.IOException;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public interface IResourceUriHandler {
    boolean accept(String uri);
    void handle(String uri,HttpContext httpContext) throws IOException;
}
