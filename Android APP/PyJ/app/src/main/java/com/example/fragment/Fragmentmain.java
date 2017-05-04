package com.example.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.view.InfoActivity;
import com.example.widget.mainListAdapter;
import com.example.pyj.R;
import com.example.webservice.NetAsyncTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.Global.Host;
import static com.example.Global.VisionInfo;
import static com.example.Global._Host;
import static com.example.Global._VisionInfo;

/**
 * 首页
 */

public class Fragmentmain extends Fragment {
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private mainListAdapter mainlistAdapter;
    private int lastItemPosition;

    public void getFloatingActionButton(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //初始化赋值
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_listRefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_listRecycler);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        //初始化recyclerView,swipeRefreshLayout
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#000000"), Color.parseColor("#222222"), Color.parseColor("#444444"));

        //加载初始数据
        InitData(1001, 10);

        //回到顶部事件
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        //上拉加载事件
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recycler, int newState) {
                super.onScrollStateChanged(recycler, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItemPosition + 1 == mainlistAdapter.getItemCount()) {
                    mainlistAdapter.notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        //上拉加载（异步获取数据）
                        @Override
                        public void run() {
                            String Data = setData(1001 + mainlistAdapter.getItemCount(), 10);
                            NetAsyncTask netAsyncTask = new NetAsyncTask() {
                                @Override
                                protected void onPostExecute(ArrayList result) {
                                    if (result == null) {
                                        //用吐司返回信号
                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "so sorry,连接发生错误", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } else {
                                        mainlistAdapter.AddItem(result);
                                    }
                                }
                            };
                            netAsyncTask.execute(VisionInfo, Data, 0);//解析方式选择0
                        }
                    }, 3000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recycler, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        //下拉刷新事件
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    //下拉刷新（异步加载数据）
                    @Override
                    public void run() {
                        String Data = setData(1001, 10);
                        NetAsyncTask netAsyncTask = new NetAsyncTask() {
                            @Override
                            protected void onPostExecute(ArrayList result) {
                                if (result == null) {
                                    //用吐司返回信号
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "so sorry,连接发生错误", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                } else {
                                    mainlistAdapter.RefreshItem(result);
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        };
                        netAsyncTask.execute(VisionInfo, Data, 0);//解析方式选择0
                    }
                }, 5000);
            }
        });

        return view;
    }

    /**
     * 网络异步获取数据
     *
     * @param StartIndex
     * @param NumIndex
     */
    private void InitData(int StartIndex, int NumIndex) {
        String Data = setData(StartIndex, NumIndex);
        NetAsyncTask netAsyncTask = new NetAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList result) {
                if (result == null) {
                    //用吐司返回信号
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "so sorry,连接发生错误", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                mainlistAdapter = new mainListAdapter(getActivity(), result);
                mainlistAdapter.setOnItemClickListener(new mainListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), InfoActivity.class);
                        intent.putExtra("text", "" + position);
                        /**
                         *Unable to find explicit activity class {com.example.pyj/com.example.view.InfoActivity}; have you declared this activity in your AndroidManifest.xml?
                         */
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(mainlistAdapter);
                mainlistAdapter.notifyDataSetChanged();
            }
        };
        netAsyncTask.execute(VisionInfo, Data, 0);//解析方式选择0
    }

    private String setData(int StartIndex, int NumIndex) {
        //我们请求的数据
        String Data = "";
        try {
            Data = "StartIndex=" + URLEncoder.encode("" + StartIndex, "UTF-8") + "&NumIndex=" + URLEncoder.encode("" + NumIndex, "UTF-8") + "&Host=" + URLEncoder.encode(Host, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Data;
    }
}