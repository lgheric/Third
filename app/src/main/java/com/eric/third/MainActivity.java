package com.eric.third;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private MyWebView wView;
    private Button btn_icon;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_icon = findViewById(R.id.btn_icon);
        wView = findViewById(R.id.wView);

        //-----------------set cookie---------------------------
//        CookieSyncManager.createInstance(MainActivity.this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.setCookie(url, cookies);  //cookies是要设置的cookie字符串
//        CookieSyncManager.getInstance().sync();
        //------------------------------------------------------
        wView.loadUrl("http://blog.csdn.net/coder_pig");
        wView.setWebViewClient(new WebViewClient() {
            //在webview里打开新链接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //获取屏幕高度，另外因为网页可能进行缩放了，所以需要乘以缩放比例得出的才是实际的尺寸
                Log.e("HEHE", wView.getContentHeight() * wView.getScale() + "");
                CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                Log.e("HEHE", "Cookies = " + CookieStr);
                super.onPageFinished(view, url);

            }
        });


        //比如这里做一个简单的判断，当页面发生滚动，显示那个Button
        wView.setOnScrollChangedCallback((dx, dy) -> {
            if (dy > 0) {
                btn_icon.setVisibility(View.VISIBLE);
            } else {
                btn_icon.setVisibility(View.GONE);
            }
        });

        btn_icon.setOnClickListener(v -> {
            wView.setScrollY(0);
            btn_icon.setVisibility(View.GONE);
        });
        WebSettings settings = wView.getSettings();
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);   //自适应屏幕
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);//设定支持缩放
        settings.setDisplayZoomControls(false);//隐藏缩放控件
        wView.setInitialScale(25);//为25%，最小缩放等级
        //settings2.setTextZoom(int);
        settings.setTextSize(WebSettings.TextSize.LARGER);

        CookieManager cm = CookieManager.getInstance();


    }

    @Override
    public void onBackPressed() {
        if (wView.canGoBack()) {
            wView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }

        }
    }

    @Override
    public void onClick(View view) {

    }
}
