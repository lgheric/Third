package com.eric.third;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editname;
    private EditText editpasswd;
    private Button btnlogin;
    private String strname;
    private String strpasswd;
    private SharedHelper sh;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        bindViews();
    }

    private void bindViews() {
        editname = (EditText)findViewById(R.id.editname);
        editpasswd = (EditText)findViewById(R.id.editpasswd);
        btnlogin = (Button)findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strname = editname.getText().toString();
                strpasswd = editpasswd.getText().toString();
                sh.save(strname,strpasswd);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String,String> data = sh.read();
        editname.setText(data.get("username"));
        editpasswd.setText(data.get("passwd"));
    }

}