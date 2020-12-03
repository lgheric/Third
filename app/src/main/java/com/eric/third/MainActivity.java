package com.eric.third;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.eric.third.util.Downloader;

import java.io.File;

public class MainActivity extends AppCompatActivity  {
    private EditText editurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editurl2 = findViewById(R.id.editurl2);
        Button btnsave2 = findViewById(R.id.btnsave2);

        btnsave2.setOnClickListener(view -> {
                new Thread(){
                    @Override
                    public void run(){
                        startActivity(new Intent(MainActivity.this,UpdateAct.class));
                    }
                }.start();
            finish();

        });
    }




}
