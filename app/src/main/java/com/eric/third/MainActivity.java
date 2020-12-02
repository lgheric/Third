package com.eric.third;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/XXPermissions
 *    time   : 2018/06/15
 *    desc   : XXPermissions 权限请求框架使用案例
 */
public class MainActivity extends AppCompatActivity  {
    private EditText editurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editurl = findViewById(R.id.editurl);
        findViewById(R.id.btnsave).setOnClickListener(view -> new Thread()
        {
            @Override
            public void run() {
                String path = editurl.getText().toString();
                try {
                    DownLoadService.downLoad(path, MainActivity.this);
                    //内部存储不需要任何权限，但只能本app 访问

//                    String fileInnerName = "/inner/img"; //目录名
//                    //内置存储缓存目录
//                    File fileCache = new File(getApplicationContext().getCacheDir(), fileInnerName);
//                    //     /data/data/com.android.imageloaderstorage/cache/inner/img
//                    if (!fileCache.exists()) {
//                        boolean isInner = fileCache.mkdirs();
//                    }
//                    Log.i(" FileFragment ",  "  " + fileCache.getAbsolutePath());
//

                } catch (Exception e) {e.printStackTrace();}
            }
        }.start());
    }




}
