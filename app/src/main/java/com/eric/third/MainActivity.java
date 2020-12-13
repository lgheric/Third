package com.eric.third;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView img_show;
    private File currentImageFile = null;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
    }

    private void bindViews() {
        img_show =  findViewById(R.id.img_show);
        Button btn_start = findViewById(R.id.btn_start);

        btn_start.setOnClickListener(v -> {
            File dir = new File(Environment.getExternalStorageDirectory(),"pictures");
            if(!dir.exists()){
                Boolean rs = dir.mkdirs();
                System.out.println(dir+" 创建结果："+rs);

            }
            currentImageFile = new File(dir,System.currentTimeMillis() + ".jpg");

            System.out.println(currentImageFile);
            if(!currentImageFile.exists()){
                try {
                    Boolean rs = currentImageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("exists file:"+currentImageFile);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(MainActivity.this, "com.eric.third.fileProvider", currentImageFile);
            } else {
                uri = Uri.fromFile(currentImageFile);
            }
            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            it.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
//            Bundle bundle = data.getExtras();
//            Bitmap bitmap = (Bitmap) bundle.get("data");
//            img_show.setImageBitmap(bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(MainActivity.this, "com.eric.third.fileProvider", currentImageFile);
            } else {
                uri = Uri.fromFile(currentImageFile);
            }
            System.out.println("[uri]"+uri);
            img_show.setImageURI(uri);
        }


//        switch (requestCode){
//            case REQUEST_CODE_TAKE_PICTURE:
//                img_show.setImageURI(Uri.fromFile(currentImageFile));
//                break;
//        }
    }
}
