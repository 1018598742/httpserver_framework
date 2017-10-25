package com.fta.httpframework.teleservice;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class SimpleHttpServer {
    private final WebConfiguration webConfiguration;
    private final ExecutorService threadPool;
    private ServerSocket socket;
    private Set<IResourceUriHandler> resourceHandlers;


    public SimpleHttpServer(WebConfiguration webConfiguration) {
        this.webConfiguration = webConfiguration;
        threadPool = Executors.newCachedThreadPool();
        resourceHandlers = new HashSet<IResourceUriHandler>();
    }

    boolean isEnable = true;

    public void startAsync() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcSync();
            }
        }).start();
    }


    public void stopAsync() throws IOException {
        if (!isEnable) {
            return;
        }
        isEnable = false;
        socket.close();
        socket = null;
    }

    private void doProcSync() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(webConfiguration.getPort());
            socket = new ServerSocket();
            socket.bind(socketAddress);
            while (isEnable) {
                //当远端连接时会接着执行
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("spy", "a remote peer accepted..." + remotePeer.getRemoteSocketAddress().toString());
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (Exception e) {
            Log.e("spy", e.toString());
        }
    }

    public void registerResourceHandler(IResourceUriHandler handler){
        resourceHandlers.add(handler);
    }

    private void onAcceptRemotePeer(Socket remotePeer) {
        try {

            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(remotePeer);
//            remotePeer.getOutputStream().write("congratulations,connected successful".getBytes());
            InputStream nis = remotePeer.getInputStream();
            String headerLine = null;
            String resuourceUrl = StreamToolkit.readLine(nis).split(" ")[1];

            Log.d("spy","resourceUrl=="+resuourceUrl);
            while ((headerLine = StreamToolkit.readLine(nis)) != null) {
                if (headerLine.equals("\r\n")){
                    break;
                }
                String[] pair = headerLine.split(":");
                httpContext.addRequestHeader(pair[0],pair[1]);
                Log.d("spy","header line:"+ headerLine);
            }
            for(IResourceUriHandler handler : resourceHandlers){
                if(!handler.accept(resuourceUrl)){
                    continue;
                }
                Log.d("spy", "onAcceptRemotePeer: handler first");
                handler.handle(resuourceUrl,httpContext);
            }
        } catch (IOException e) {
            Log.e("spy", e.toString());
        }finally {
            try {
                Log.d("spy", "onAcceptRemotePeer: finally close");
                remotePeer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
