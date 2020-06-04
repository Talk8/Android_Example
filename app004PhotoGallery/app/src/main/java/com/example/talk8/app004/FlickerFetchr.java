package com.example.talk8.app004;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//连接Flicker网站的类，国内无法访问此网站，我替换为
// https://www.poco.cn/ 不过类名还是保持和书中一致
public class FlickerFetchr {
    public byte[] getUrlBytes (String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        //书中使用http的链接，我修改为https
        //HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            //if(connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage()+urlSpec);
            }

            int bytesRead =0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
