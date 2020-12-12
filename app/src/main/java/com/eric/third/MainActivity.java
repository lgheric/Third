package com.eric.third;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

        private MediaPlayer mPlayer = null;
        private SurfaceView sfv_show;
        private SurfaceHolder surfaceHolder;
        private Button btn_start;
        private Button btn_pause;
        private Button btn_stop;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            bindViews();
        }

        private void bindViews() {
            sfv_show = findViewById(R.id.sfv_show);
            btn_start = findViewById(R.id.btn_start);
            btn_pause = findViewById(R.id.btn_pause);
            btn_stop = findViewById(R.id.btn_stop);

            btn_start.setOnClickListener(this);
            btn_pause.setOnClickListener(this);
            btn_stop.setOnClickListener(this);

            //初始化SurfaceHolder类，SurfaceView的控制器
            surfaceHolder = sfv_show.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setFixedSize(320, 220);   //显示的分辨率,不设置为视频默认

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
                    mPlayer.start();
                    btn_start.setEnabled(false);
                    btn_pause.setEnabled(true);
                    btn_stop.setEnabled(true);
                    break;
                case R.id.btn_pause:
                    mPlayer.pause();
                    btn_start.setEnabled(true);
                    btn_pause.setEnabled(false);
                    btn_stop.setEnabled(false);
                    break;
                case R.id.btn_stop:
                    mPlayer.stop();
                    btn_start.setEnabled(true);
                    btn_pause.setEnabled(false);
                    btn_stop.setEnabled(false);
                    break;
            }
        }

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            mPlayer = MediaPlayer.create(MainActivity.this, R.raw.lesson);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDisplay(surfaceHolder);    //设置显示视频显示在SurfaceView上
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}

        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
        }
    }