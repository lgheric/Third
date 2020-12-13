package com.eric.third;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_show;
    private Button btn_cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
    }

    private void bindViews() {
        btn_cam = findViewById(R.id.btn_cam);
        img_show = findViewById(R.id.img_show);

        btn_cam.setOnClickListener(this);
    }

    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode:"+requestCode);
        System.out.println("resultCode:"+resultCode);
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            assert bundle != null;
            Bitmap bitmap = (Bitmap) bundle.get("data");
            img_show.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        //
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it,Activity.DEFAULT_KEYS_DIALER);

    }
}