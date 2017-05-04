package com.example.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.datas.Info_datas;
import com.example.datas.List_datas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * webservice访问和调用
 */

public class NetAsyncTask extends AsyncTask<Object, Object, ArrayList> {
    private ArrayList list;

    @Override
    public ArrayList doInBackground(Object... params) {
        String json = "";
        try {
            // 初始化URL
            URL url = new URL(params[0].toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求方式
            conn.setRequestMethod("POST");

            // 设置超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            // 设置允许输入
            conn.setDoInput(true);
            // 设置允许输出
            conn.setDoOutput(true);

            // post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);

            // 获取輸出流
            OutputStream out = conn.getOutputStream();

            out.write(params[1].toString().getBytes());
            out.flush();
            out.close();
            conn.connect();

            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 返回字符串
                json = new String(message.toByteArray(), "utf8");
                list = JsonToArrayList(json, Integer.parseInt(params[2].toString()));
                // 释放资源
                is.close();
                message.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: UnsupportedEncodingException", e);
            return null;
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: ProtocolException", e);
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: MalformedURLException", e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: IOException", e);
            return null;
        }
        return list;
    }

    private Bitmap getURLBitmap(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
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

    /**
     * @param json
     * @param type 解析类型，0为列表，1为详细内容
     * @return
     */
    private ArrayList JsonToArrayList(String json, int type) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (type == 0) {
                ArrayList<List_datas> datas = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    List_datas data = new List_datas();
                    Bitmap bitmap = getURLBitmap(jsonObject.getString("pic"));
                    data.set_id(jsonObject.getString("_id"));
                    data.setTitle(jsonObject.getString("name"));
                    data.setGenres(jsonObject.getString("genres"));
                    data.setRating(jsonObject.getString("rating"));
                    data.setPic(bitmap);
                    //把数据添加进ArrayList
                    datas.add(data);
                }
                return datas;
            } else if (type == 1) {
                ArrayList<Info_datas> datas = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Info_datas data = new Info_datas();
                    String[] keyword = jsonObject.getString("keyword").split(",");
                    Bitmap bitmap = getURLBitmap(jsonObject.getString("pic"));
                    data.set_id(jsonObject.getString("_id"));
                    data.setTitle(jsonObject.getString("name"));
                    data.setGenres(jsonObject.getString("genres"));
                    data.setRating(jsonObject.getString("rating"));
                    data.setDirector(jsonObject.getString("director"));
                    data.setActors(jsonObject.getString("actors"));
                    data.setKeyword(keyword);
                    data.setPic(bitmap);
                    //把数据添加进ArrayList
                    datas.add(data);
                }
                return datas;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
