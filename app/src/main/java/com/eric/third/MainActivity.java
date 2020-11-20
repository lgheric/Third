package com.eric.third;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView txt_title;
    private FragmentManager fManager = null;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            fManager = getSupportFragmentManager();
            bindViews();

        ArrayList<Data> datas = new ArrayList<>();
            for (int i = 1; i <= 20; i++) {
                Data data = new Data("新闻标题" + i, i + "~新闻内容~~~~~~~~");
                datas.add(data);
            }
            NewListFragment nlFragment = new NewListFragment(fManager, datas);
            FragmentTransaction ft = fManager.beginTransaction();
            ft.replace(R.id.fl_content, nlFragment);
            ft.commit();

    }

    private void bindViews() {
        txt_title = findViewById(R.id.txt_title);
    }

    //点击回退键的处理：判断Fragment栈中是否有Fragment
    //没，双击退出程序，否则像是Toast提示
    //有，popbackstack弹出栈
    @Override
    public void onBackPressed() {
        if (fManager.getBackStackEntryCount() == 0) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        } else {
            fManager.popBackStack();
            txt_title.setText("新闻列表");
        }
    }
}