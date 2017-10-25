package com.fta.httpframework;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.fta.httpframework.teleservice.ResourceInAssetsHandler;
import com.fta.httpframework.teleservice.SimpleHttpServer;
import com.fta.httpframework.teleservice.UploadImageHandler;
import com.fta.httpframework.teleservice.WebConfiguration;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SimpleHttpServer shs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebConfiguration wc = new WebConfiguration();
        wc.setPort(8088);
        wc.setMaxParallels(50);
        shs = new SimpleHttpServer(wc);
        shs.registerResourceHandler(new ResourceInAssetsHandler(this));
        shs.registerResourceHandler(new UploadImageHandler(){
            @Override
            protected void onImageLoaded(final String tmpPath) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView ivImage = (ImageView) findViewById(R.id.imageView);
                        Bitmap bitmap = BitmapFactory.decodeFile(tmpPath);
                        ivImage.setImageBitmap(bitmap);
                    }
                });

            }
        });
        shs.startAsync();
    }

    private void showImage(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView ivImage = (ImageView) findViewById(R.id.imageView);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ivImage.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this,"image recelved and shown!",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        try {
            shs.stopAsync();
        } catch (IOException e) {
            Log.e("spy",e.toString());
        }
        super.onDestroy();
    }
}
