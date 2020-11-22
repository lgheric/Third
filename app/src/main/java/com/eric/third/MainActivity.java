package com.eric.third;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        
        Display dis = getWindowManager().getDefaultDisplay();
        if(dis.getWidth() > dis.getHeight())
        {
            FragmentMan f1 = new FragmentMan();
            getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout1, f1).commit();
        } else {
            FragmentWoman f2 = new FragmentWoman();
            getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout1, f2).commit();
        }
    }
}