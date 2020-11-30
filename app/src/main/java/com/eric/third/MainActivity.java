package com.eric.third;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eric.third.util.DownloadUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
    ProgressBar pro;
    private String responseData="";
    private final String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final String server_host = "http://192.168.43.80:8080/";
    private final String server_api = "http://192.168.43.80:8080/api.php";
    final Handler myHandler = new Handler(Objects.requireNonNull(Looper.myLooper())){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0x123){
                //web_show.loadData(responseData,"text/html", "UTF-8");
                txt_show.setText(responseData);
            }else {
                pro.setProgress(msg.arg1);
            }
        }
    };
    public List<String> filenames = new ArrayList<>();

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
        Button btn_okhttp3_download = findViewById(R.id.btn_okhttp3_download);
        Button btn_file_download = findViewById(R.id.btn_file_download);
        pro = (ProgressBar) findViewById(R.id.pro);

        btn_async_get.setOnClickListener(this);
        btn_sync_get.setOnClickListener(this);
        btn_post_string.setOnClickListener(this);
        btn_post_stream.setOnClickListener(this);
        btn_post_file.setOnClickListener(this);
        btn_post_form.setOnClickListener(this);
        btn_post_part.setOnClickListener(this);
        btn_okhttp3_download.setOnClickListener(this);
        btn_file_download.setOnClickListener(this);
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
            case R.id.btn_okhttp3_download:
                DownloadUtil du = DownloadUtil.get();
                du.download(server_api+"/static/map.jpg",sdCard,"newmap.jpg",new DownloadUtil.OnDownloadListener(){

                    @Override
                    public void onDownloadSuccess(File file) {
                        Log.e(TAG, "onDownloadSuccess");

                    }

                    @Override
                    public void onDownloading(int progress) {
                        Log.e(TAG, "onDownloading");

                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        Log.e(TAG, "onDownloadFailed");

                    }
                });
                break;
            case R.id.btn_file_download:
                Log.i(TAG,"click");
                OkHttpClient okHttpClient = new OkHttpClient();
                Log.i(TAG,"click2");

                URL url = null;
                try {
                    url = new URL(server_host+"static/map.jpg");
                    Log.i(TAG,"url="+url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Request request = new Request.Builder().url(url).build();
                Log.i(TAG,"click3");

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i("myTag", "下载失败");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String fileName = "test.jpg";
                            writeFile(response,fileName);
                            Log.i(TAG,"download succeed.");
                        }
                    }
                });
                break;

            case R.id.btn_post_file:

                postFile();

                //postMultiType();

//                filenames.add(sdCard+"/ic_launcher.png");
//                filenames.add(sdCard+"/test.md");
//                upLoadFile(server_api, filenames,new Callback() {
//                    @Override
//                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                        Log.e(TAG, "onFailure: " + e.getMessage());
//                    }
//                    @Override
//                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        Log.i(TAG, "onResponse: " + response.body().string());
//                    }
//                });

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
        String url = server_api;
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
        String url = server_api;
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
                .url(server_api)
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
                .url(server_api)
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


//        if (Build.VERSION.SDK_INT >= 23) {
//            int REQUEST_CODE_PERMISSION_STORAGE = 100;
//            String[] permissions = {
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            };
//
//            for (String str : permissions) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    this.requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE);
//                    return;
//                }
//            }
//        }
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_EXTERNAL_STORAGE = 1;
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

        }

        File file = new File(sdCard+"/ic_launcher.png");
        Log.i(TAG,"file="+sdCard);
        Request request = new Request.Builder()
                .url(server_api)//https://api.github.com/markdown/raw
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
                //Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        //requestCode就是requestPermissions()的第三个参数
        //permission就是requestPermissions()的第二个参数
        //grantResults是结果，0调试通过，-1表示拒绝
        StringBuilder s= new StringBuilder();
        for (String str : permission) {
            s.append(str).append(" ");
        }
        for(int i : grantResults){
            s.append(i).append(" ");
        }

        Log.e("ERIC","requestCode:"+requestCode+" "+"permission:"+s+" \n");
    }
    //异步POST方式提交表单
    private void postForm(){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url(server_api)//https://en.wikipedia.org/w/index.php
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

    //POST方式提交分块请求 TODO:没研究明白
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
                        RequestBody.create(MEDIA_TYPE_PNG, new File(sdCard+"/ic_launcher.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(server_api)//https://api.imgur.com/3/image
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

    //使用MultipartBody同时上传多种类型数据
    private void postMultiType(){
        OkHttpClient client = new OkHttpClient();
        File file = new File(sdCard+"/ic_launcher.png");
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", "zhangsan")
                .addFormDataPart("age", "20")
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        Request request = new Request.Builder()
                .url(server_api)
                .post(multipartBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    /**
     * 通过上传的文件的完整路径生成RequestBody
     * @param fileNames List<String> 完整的文件路径
     * @return MultipartBody
     */
    private static RequestBody getRequestBody(List<String> fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
            File file = new File(fileNames.get(i)); //生成文件
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart( //给Builder添加上传的文件
                    "image",  //请求的名字
                    file.getName(), //文件的文字，服务器端用来解析的
                    RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
            );
        }
        return builder.build(); //根据Builder创建请求
    }
    private static String getMimeType(String name){
        return "image/png";
    }

    /**
     * 获得Request实例
     * @param  url String
     * @param fileNames 完整的文件路径
     * @return Request
     */
    private static Request getRequest(String url, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(fileNames));
        return builder.build();
    }

    /**
     * 根据url，发送异步Post请求
     * @param url 提交到服务器的地址
     * @param fileNames 完整的上传的文件的路径名
     * @param callback OkHttp的回调接口
     */
    public static void upLoadFile(String url,List<String> fileNames,Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(getRequest(url,fileNames)) ;
        call.enqueue(callback);
    }

    //--------------------------------------------------

    private void writeFile(Response response,String fileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        is = response.body().byteStream();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path, fileName);
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            //获取下载的文件的大小
            long fileSize = response.body().contentLength();
            long sum = 0;
            int porSize = 0;
            while ((len = is.read(bytes)) != -1) {
                fos.write(bytes);
                sum += len;
                porSize = (int) ((sum * 1.0f / fileSize) * 100);
                Message message = myHandler.obtainMessage(1);
                message.arg1 = porSize;
                myHandler.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("myTag", "下载成功");
    }
}

