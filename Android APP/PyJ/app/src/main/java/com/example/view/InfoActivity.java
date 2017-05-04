package com.example.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Blur;
import com.example.datas.Info_datas;
import com.example.pyj.R;
import com.example.support.KeywordsFlow;
import com.example.webservice.NetAsyncTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.Global.Host;
import static com.example.Global.VisionInfo;
import static com.example.Global._Host;
import static com.example.Global._VisionInfo;


/**
 * 详情页
 */

public class InfoActivity extends AppCompatActivity {
    private ImageView background;
    private ImageView pic;
    private TextView title;
    private RatingBar ratingBar;
    private TextView rating;
    private TextView genres;
    private TextView director;
    private TextView actors;
    private Toolbar toolbar;
    private KeywordsFlow keywordsFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_info);
        //接收数据
        Intent intent = getIntent();
        String TargetID = intent.getStringExtra("text");
        //产生布局
        toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        background = (ImageView) findViewById(R.id.info_background);
        pic = (ImageView) findViewById(R.id.info_pic);
        title = (TextView) findViewById(R.id.info_title);
        ratingBar = (RatingBar) findViewById(R.id.info_ratingbar);
        rating = (TextView) findViewById(R.id.info_rating);
        genres = (TextView) findViewById(R.id.info_genres);
        director = (TextView) findViewById(R.id.info_director);
        actors = (TextView) findViewById(R.id.info_actors);
        keywordsFlow = (KeywordsFlow) findViewById(R.id.keywords);
        keywordsFlow.setDuration(2000);

        ratingBar.setMax(10);
        //形成view
        initView(TargetID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView(String TargetID) {
        String Data = setData(TargetID);
        NetAsyncTask netAsyncTask = new NetAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList result) {
                if (result == null) {
                    //用吐司返回信号
                    Toast toast = Toast.makeText(getApplicationContext(), "so sorry,连接发生错误", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    ArrayList<Info_datas> datas = result;
                    Info_datas data = datas.get(0);
                    String title_str = data.getTitle();
                    //创建toolbar
                    toolbar.setTitle(title_str);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    //虚化背景
                    Blur blur = new Blur();
                    Bitmap blur_bitmap = blur.darkBitmap(data.getPic(), 0.7f);
                    blur_bitmap = blur.compressBitmap(blur_bitmap, 1.5f);
                    blur_bitmap = blur.blurBitmap(blur_bitmap, blur_bitmap, 20f, InfoActivity.this);
                    background.setImageBitmap(blur_bitmap);

                    //导入数据
                    title.setText(title_str);
                    ratingBar.setRating(Float.parseFloat(data.getRating()) / 2);
                    rating.setText(data.getRating());
                    genres.setText(data.getGenres());
                    director.setText(data.getDirector());
                    actors.setText(data.getActors());
                    pic.setImageBitmap(data.getPic());
                    feedKeywordsFlow(keywordsFlow, data.getKeyword());
                    keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
                }
            }
        };
        netAsyncTask.execute(VisionInfo, Data, 1);
    }

    private String setData(String TargetID) {
        //我们请求的数据
        String Data = "";
        try {
            Data = "TargetID=" + URLEncoder.encode(TargetID, "UTF-8") + "&Host=" + URLEncoder.encode(Host, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Data;
    }

    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
        for (int i = 0; i < KeywordsFlow.MAX; i++) {
            String tmp = arr[i];
            keywordsFlow.feedKeyword(tmp);
        }
    }
}
