package com.fta.httpframework.teleservice;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class UploadImageHandler implements IResourceUriHandler {
    private String acceptPrefix = "/upload_image/";
    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
//        OutputStream nos = httpContext.getUnderlySocket().getOutputStream();
//        PrintWriter writer = new PrintWriter(nos);
//        writer.println("HTTP/1.1 200 OK");
//        writer.println();
//        writer.println("from upload image handler");
//        writer.flush();

//        String tmpPath = "/mnt/sdcard/test_upload.jpg";
        Log.d("spy", "handle: handler first");
        String tmpPath = Environment.getExternalStorageDirectory().getPath()+"/test_upload.jpg";
        File file = new File(tmpPath);
        String requestHeaderValue = httpContext.getRequestHeaderValue("Content-Length");
        long totalLength = Long.parseLong(requestHeaderValue.trim());
        Log.d("spy", "handle: totalLength="+totalLength);
        FileOutputStream fos = new FileOutputStream(file);
        InputStream nis = httpContext.getUnderlySocket().getInputStream();
        byte[] buffer = new byte[10240];

        int nReaded = 0;
        long nLeftLength = totalLength;
        Log.d("spy", "handle: 1");
        int n = 0;
        while (nLeftLength > 0 && (nReaded = nis.read(buffer)) > 0) {//对的
//        while ((nReaded = nis.read(buffer))>0 && nLeftLength >0){//错的
            fos.write(buffer,0,nReaded);
            nLeftLength -= nReaded;
            n++;
            Log.d("spy", "handle: 2"+n);
        }
        Log.d("spy", "handle: 3");
        fos.close();
        OutputStream nos = httpContext.getUnderlySocket().getOutputStream();
        PrintStream printer = new PrintStream(nos);
        printer.println("HTTP/1.1 200 OK");
        printer.println();
        printer.println("from upload image handler");
        printer.flush();

        onImageLoaded(tmpPath);
    }

    protected void onImageLoaded(String tmpPath) {
        Log.d("spy", "handle: end");
    }
}
