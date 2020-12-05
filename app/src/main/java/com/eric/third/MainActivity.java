package com.eric.third;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eric.third.util.PermissionUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private WebView wView;
    private final PermissionUtils.PermissionGrant mPermissionGrant = requestCode -> {
        switch (requestCode) {
            case PermissionUtils.CODE_RECORD_AUDIO:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_GET_ACCOUNTS:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_READ_PHONE_STATE:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_CALL_PHONE:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_CAMERA:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                Toast.makeText(MainActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsFragment fragment = new PermissionsFragment();

        //设置WebView的相关设置,依次是:
        //支持js,不保存表单,不保存密码,不支持缩放
        //同时绑定Java对象
        wView = findViewById(R.id.wView);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setSaveFormData(false);
        wView.getSettings().setSavePassword(false);
        wView.getSettings().setSupportZoom(false);
        wView.getSettings().setDefaultTextEncodingName("UTF-8");
        wView.addJavascriptInterface(new SharpJS(), "sharp");
        wView.loadUrl("file:///android_asset/demo3.html");
        //WebView file download.
//        wView.setDownloadListener(new DownloadListener(){
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition,
//                                        String mimetype, long contentLength) {
//                Log.e("ERIC","开始下载");
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                startActivity(intent);
//            }
//        });

        wView.setDownloadListener(new DownloadListener(){
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Log.e("HEHE","onDownloadStart被调用：下载链接：" + url);
                new Thread(new DownLoadThread(url)).start();
            }
        });
    }

    //自定义一个Js的业务类,传递给JS的对象就是这个,调用时直接javascript:sharp.contactlist()
    //注意点有两个：
    //1,Android4.2后，只有添加 @JavascriptInterface 声明的Java方法才可以被JavaScript调用
    //2,所有的WebView方法都应该在同一个线程程中调用，而这里的contactlist方法却在 JavaBridge线程中被调用了！
    // 所以我们要要把contactlist里的东东写到同一个线程中，比如一种解决 方法，就是下面这种：
    //java.lang.Throwable: A WebView method was called on thread 'JavaBridge'.
    // All WebView methods must be called on the same thread.
    // (Expected Looper Looper (main, tid 2) {f2e4762} called on Looper (JavaBridge, tid 22388)
    // {4c4c19c}, FYI main Looper is Looper (main, tid 2) {f2e4762})
    public class SharpJS {

        @JavascriptInterface
        public void contactlist() {
//            runOnUiThread(newRunnable(){
//                @Override
//                publicvoid run(){
//                    // Code for WebView goes here
//                }
//            });
            //方法1：使用post()方法使WebView方法保持在同一线程中调用
            wView.post(() -> {

                try {
                    System.out.println("contactlist()方法执行了！");
                    String json = buildJson(getContacts());
                    // This code is BAD and will block the UI thread(except in wWiew.post())
                    //wView.loadUrl("javascript:show('" + json + "')");
                    //千万不要这样做 Android 4.4中 evaluateJavascript() 就是专门来异步执行JavaScript代码的。
                    String script = "javascript:show('" + json + "')";
                    //wView.evaluateJavascript(script, value -> {
                    wView.evaluateJavascript(script, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            //将button显示的文字改成JS返回的字符串
                            //buttonLeft.setText(s);
                        }
                    });

                } catch (Exception e) {
                    System.out.println("设置数据失败" + e);
                }

            });

        }

        @JavascriptInterface
        public void call(String phone) {
            System.out.println("call()方法执行了！");
            Intent it = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(it);
        }
    }

    //将获取到的联系人集合写入到JsonObject对象中,再添加到JsonArray数组中
    public String buildJson(List<Contact> contacts)throws Exception
    {
        JSONArray array = new JSONArray();
        for(Contact contact:contacts)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", contact.getId());
            jsonObject.put("name", contact.getName());
            jsonObject.put("phone", contact.getPhone());
            array.put(jsonObject);
        }
        return array.toString();
    }

    //定义一个获取联系人的方法,返回的是List<Contact>的数据
    public List<Contact> getContacts()
    {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_GET_ACCOUNTS, mPermissionGrant);

        List<Contact> Contacts = new ArrayList<>();
        //①查询raw_contacts表获得联系人的id
        ContentResolver resolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //查询联系人数据
        Cursor cursor = resolver.query(uri, null, null, null, null);
        while(cursor.moveToNext())
        {
            Contact contact = new Contact();
            //获取联系人姓名,手机号码
            contact.setId(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
            contact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            Contacts.add(contact);
        }
        cursor.close();
        return Contacts;
    }

}
