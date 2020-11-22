package com.eric.third;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //创建fragment对象
    private FragmentMan fragmentMan;
    private FragmentWoman fragmentWoman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定ID
        bangID();

    }

    private void bangID() {
        //创建button对象
        Button manBtn = findViewById(R.id.man_tv);
        Button womanBtn = findViewById(R.id.woman_tv);
        manBtn.setOnClickListener(this);
        womanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.man_tv:
                //判断fragmentMan是否为空，无则创建fragment对象
                if(fragmentMan==null){
                    fragmentMan = new FragmentMan();
                }
                //创建FragmentManager对象
                FragmentManager manager = getSupportFragmentManager();
                //创建FragmentTransaction事务对象
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //使用replace（将要替换位置的i的，替换的页面）方法实现页面的替换
                fragmentTransaction.replace(R.id.shop_content,fragmentMan);
                //提交事务
                fragmentTransaction.commit();
                break;
            case R.id.woman_tv:
                if(fragmentWoman==null){
                    fragmentWoman = new FragmentWoman();
                }
                FragmentManager  manager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 =manager1.beginTransaction();
                fragmentTransaction1.replace(R.id.shop_content,fragmentWoman);
                fragmentTransaction1.commit();
                break;
            default:
                break;

        }

    }
}