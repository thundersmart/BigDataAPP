package com.example.webservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by song on 2017-4-26.
 */

public class ImageReader extends AsyncTask<Object, Object, Bitmap> {
    //加载图片
    @Override
    public Bitmap doInBackground(Object... params) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(params[0].toString());
            // 获得连接  
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);// 读取超时  
            conn.setConnectTimeout(5000);//设置超时
            conn.connect();// 建立连接
            // 获取成功判断,获取响应码
            if (conn.getResponseCode() == 200) {
                // 拿到服务器返回的流，客户端请求的数据，就保存在流当中
                InputStream is = conn.getInputStream();
                // 从流中读取数据，构造一个图片对象GoogleAPI
                bmp = BitmapFactory.decodeStream(is);
                // 把图片设置到UI主线程
                // ImageView中,获取网络资源是耗时操作需放在子线程中进行,通过创建消息发送消息给主线程刷新控件；
            } else {
                bmp = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
