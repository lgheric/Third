package com.eric.third;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_play1;
    private Button btn_play2;
    private Button btn_play3;
    private Button btn_play4;
    private Button btn_play5;
    private Button btn_release;
    private AssetManager aManager;
    private SoundPool mSoundPool = null;
    private HashMap<Integer, Integer> soundID = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        try {
//            mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 1);
//            int sid = mSoundPool.load(this,R.raw.duang,1);
//            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                @Override
//                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                    Toast.makeText(MainActivity.this,"加特技准备完毕~",Toast.LENGTH_SHORT).show();
//                    mSoundPool.play(sid,1,1,0,1,1);
//                }
//            });
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        aManager = getAssets();
        try {
            initSP();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bindViews();
    }

    private void bindViews() {
        btn_play1 = findViewById(R.id.btn_play1);
        btn_play2 = findViewById(R.id.btn_play2);
        btn_play3 = findViewById(R.id.btn_play3);
        btn_play4 = findViewById(R.id.btn_play4);
        btn_play5 = findViewById(R.id.btn_play5);
        btn_release = findViewById(R.id.btn_release);

        btn_play1.setOnClickListener(this);
        btn_play2.setOnClickListener(this);
        btn_play3.setOnClickListener(this);
        btn_play4.setOnClickListener(this);
        btn_play5.setOnClickListener(this);
        btn_release.setOnClickListener(this);

    }

    private void initSP() throws Exception{
        //设置最多可容纳5个音频流，音频的品质为5
        mSoundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        soundID.put(1, mSoundPool.load(this, R.raw.duang, 1));
        soundID.put(2 , mSoundPool.load(getAssets().openFd("biaobiao.mp3") , 1));  //需要捕获IO异常
        soundID.put(3, mSoundPool.load(this, R.raw.duang, 1));
        soundID.put(4, mSoundPool.load(this, R.raw.duang, 1));
        soundID.put(5, mSoundPool.load(this, R.raw.duang, 1));
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Toast.makeText(MainActivity.this,"加特技准备完毕~",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play1:
                mSoundPool.play(soundID.get(1), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play2:
                mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play3:
                mSoundPool.play(soundID.get(3), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play4:
                mSoundPool.play(soundID.get(4), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play5:
                mSoundPool.play(soundID.get(5), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_release:
                mSoundPool.release();   //回收SoundPool资源
                break;
        }
    }

}