package com.fta.httpframework.teleservice;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class WebConfiguration {
    private int port;
    /**
     * 最大监听数
     */
    private int maxParallels;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxParallels() {
        return maxParallels;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }
}
