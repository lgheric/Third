package com.eric.third.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetData {

    // 定义一个获取网络图片数据的方法:
    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置连接超时为5秒
        conn.setConnectTimeout(5000);
        // 设置请求类型为Get类型
        conn.setRequestMethod("GET");
        // 判断请求Url是否成功
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("请求url失败");
        }
        InputStream inStream = conn.getInputStream();
        byte[] bt = StreamTool.read(inStream);
        inStream.close();
        return bt;
    }

    // 获取网页的html源代码
    public static String getHtml(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = StreamTool.read(in);
            return new String(data, StandardCharsets.UTF_8);
        }
        return null;
    }

    public static class FileHelper {

        private Context mContext;

        public FileHelper() {
        }

        public FileHelper(Context mContext) {
            super();
            this.mContext = mContext;
        }

        /*
         * 这里定义的是一个文件保存的方法，写入到文件中，所以是输出流
         * */
        public void save(String filename, String filecontent) throws Exception {
            //这里我们使用私有模式,创建出来的文件只能被本应用访问,还会覆盖原文件哦
            FileOutputStream output = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            output.write(filecontent.getBytes());  //将String字符串以字节流的形式写入到输出流中
            output.close();         //关闭输出流
        }


        /*
         * 这里定义的是文件读取的方法
         * */
        public String read(String filename) throws IOException {
            //打开文件输入流
            FileInputStream input = mContext.openFileInput(filename);
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            //读取文件内容:
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            //关闭输入流
            input.close();
            return sb.toString();
        }

    }
}
