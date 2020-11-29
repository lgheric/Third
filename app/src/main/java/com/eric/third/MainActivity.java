package com.eric.third;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ERIC";
    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    TextView txt_show;
    WebView web_show;
    private String responseData="";
    final Handler myHandler = new Handler(Objects.requireNonNull(Looper.myLooper())){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0x123){
                //web_show.loadData(responseData,"text/html", "UTF-8");
                txt_show.setText(responseData);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
    }

    private void bindView(){
        txt_show = findViewById(R.id.txt_show);
        web_show = findViewById(R.id.web_show);
        Button btn_async_get = findViewById(R.id.btn_async_get);
        Button btn_sync_get = findViewById(R.id.btn_sync_get);
        Button btn_post_string = findViewById(R.id.btn_post_string);
        Button btn_post_stream = findViewById(R.id.btn_post_stream);
        Button btn_post_file = findViewById(R.id.btn_post_file);
        Button btn_post_form = findViewById(R.id.btn_post_form);
        Button btn_post_part = findViewById(R.id.btn_post_part);


        btn_async_get.setOnClickListener(this);
        btn_sync_get.setOnClickListener(this);
        btn_post_string.setOnClickListener(this);
        btn_post_stream.setOnClickListener(this);
        btn_post_file.setOnClickListener(this);
        btn_post_form.setOnClickListener(this);
        btn_post_part.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_async_get:
                asyncGet();
                break;
            case R.id.btn_sync_get:
                syncGet();
                break;
            case R.id.btn_post_string:
                postString();
                break;
            case R.id.btn_post_stream:
                postStream();
                break;
            case R.id.btn_post_file:
                postFile();
                break;
            case R.id.btn_post_form:
                postForm();
                break;
            case R.id.btn_post_part:
                postMultipartBody();
                break;
        }
    }

    //异步GET请求
    private void asyncGet(){
        String url = "http://192.168.43.80";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    //同步GET请求
    private void syncGet(){
        String url = "http://192.168.43.80";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    assert response.body() != null;
                    Log.d(TAG, "run: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //异步POST方式提交String
    private void postString(){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Jdqm.";
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                assert response.body() != null;
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    //异步POST方式提交流
    private void postStream(){
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("I am Jdqm.");
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                assert response.body() != null;
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    //异步POST提交文件
    private void postFile(){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        File file = new File("test.md");
        Log.d(TAG,"path:"+file.getAbsolutePath());
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, file))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                assert response.body() != null;
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    //异步POST方式提交表单
    private void postForm(){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.43.80:8080/index/uploadIcon")//https://en.wikipedia.org/w/index.php
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                assert response.body() != null;
                //Log.d(TAG, "onResponse: " + response.body().string());
                responseData = response.body().string();//注意：只能调用一次
                myHandler.sendEmptyMessage(0x123);
            }
        });
    }

    //POST方式提交分块请求
    private void postMultipartBody(){
        OkHttpClient client = new OkHttpClient();

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("http://192.168.43.80:8080/index/uploadIcon")//https://api.imgur.com/3/image
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                Log.d(TAG, "onResponse: " + response.body().string());
            }

        });
    }
}

