package com.eric.third;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editname;
    private EditText editdetail;
    private Button btnsave;
    private Button btnclean;
    private Button btnread;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        bindViews();
    }

    private void bindViews() {
        editname = findViewById(R.id.edittitle);
        editdetail = findViewById(R.id.editdetail);
        btnsave = findViewById(R.id.btnsave);
        btnclean = findViewById(R.id.btnclean);
        btnread = findViewById(R.id.btnread);

        btnsave.setOnClickListener(this);
        btnclean.setOnClickListener(this);
        btnread.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnclean:
                editdetail.setText("");
                editname.setText("");
                break;
            case R.id.btnsave:
                String filename = editname.getText().toString();
                String filedetail = editdetail.getText().toString();
                SDFileHelper sdHelper = new SDFileHelper(mContext);
                try
                {
                    SDFileHelper.verifyStoragePermissions(this);
                    sdHelper.savaFileToSD(filename, filedetail);
                    Toast.makeText(mContext, "数据写入成功", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "数据写入失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnread:
                String detail = "";
                SDFileHelper sdHelper2 = new SDFileHelper(mContext);
                try
                {
                    String filename2 = editname.getText().toString();
                    detail = sdHelper2.readFromSD(filename2);
                }
                catch(IOException e){e.printStackTrace();}
                Toast.makeText(mContext, detail, Toast.LENGTH_SHORT).show();
                break;
        }
    }


}