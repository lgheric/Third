package com.eric.third;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new Thread() {
            @Override
            public void run() {
                try {
                    acceptServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void acceptServer() throws IOException {
        //1.创建客户端Socket，指定服务器地址和端口
        System.out.println("准备连接");
        Socket socket = new Socket();
        System.out.println("连接上了");

        //2.获取输出流，向服务器端发送信息
        OutputStream os = socket.getOutputStream();//字节输出流
        PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
        //获取客户端的IP地址
        InetAddress address = InetAddress.getLocalHost();
        String ip = address.getHostAddress();
        pw.write("客户端：~" + ip + "~ 接入服务器！！");
        System.out.println("客户端：~" + ip + "~ 接入服务器！！");
        pw.flush();
        socket.shutdownOutput();//关闭输出流
        socket.close();
    }
}
