package com.chase.dcjrCase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.ExhibitionAdapter;
import com.chase.dcjrCase.bean.ExhibitionData;
import com.chase.dcjrCase.bean.ExhibitionData.ExhiDataBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.uitl.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class ExhibitionActivity extends AppCompatActivity {

    private static final int EXHIDATA_REQUEST_SUCCESS = 801;
    private static final int EXHIDATA_REQUEST_FAILURE = 802;
    private ListView mListView;
    private SmartRefreshLayout mRefreshLayout;//整个布局
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXHIDATA_REQUEST_SUCCESS:
                    String result =  (String) msg.obj;
//                    mRlError.setVisibility(View.GONE);
//                    mListView.setVisibility(View.VISIBLE);
                    //解析json数据
                    processResult(result);
                    //写缓存 将成功读取的json字符串写入XML中保存
                    CacheUtils.setCache(Constants.EXHIJSON_URL, result, getApplicationContext());
                    break;
                case EXHIDATA_REQUEST_FAILURE:
                    String message = (String) msg.obj;
//                    Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "请检查是否连接网络!", Toast.LENGTH_SHORT).show();
//                    if (!TextUtils.isEmpty(mCache)) {
//                        // 有缓存 读取缓存数据 显示布局
//                        System.out.println("发现缓存....");
////                        processResult(mCache, count);
//                        mRlError.setVisibility(View.GONE);
//                        mListView.setVisibility(View.VISIBLE);
//                    }else {
//                        //没有缓存 添加加载失败布局
//                        mRlError.setVisibility(View.VISIBLE);
//                        mListView.setVisibility(View.GONE);
//                    }
                    break;
            }
        }
    };
    private ExhibitionData mExhibitionData;
    private ArrayList<ExhiDataBean> mExhiList;
    private ExhibitionAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition);

        mListView = (ListView) findViewById(R.id.lv_list);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExhiDataBean exhiDataBean = mExhiList.get(position);
                Intent intent = new Intent(getApplicationContext(), ExhibitionWebView.class);
                intent.putExtra("url", Constants.HOME_URL+exhiDataBean.url);//webView链接
                startActivity(intent);
            }
        });

        initData();
    }

    private void initData() {

        //触发自动刷新
        mRefreshLayout.autoRefresh();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                System.out.println("case fragment 下拉刷新 加载数据");
                refreshlayout.finishRefresh(1500);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        /*if (!TextUtils.isEmpty(mCache)) {
                            // 有缓存 解析json缓存
                            System.out.println("发现缓存....");
                            processResult(mCache);
                        }*/
                        // 即使发现有缓存,仍继续调用网络, 获取最新数据
                        getDataFromServer();//通过网络获取数据
                        refreshlayout.finishRefresh();//完成刷新
                        refreshlayout.setLoadmoreFinished(true);//可以出发加载更多事件
                    }
                }, 1500);
            }
        });
    }

    public void getDataFromServer() {
        new Thread() {
            @Override
            public void run() {
                HttpUtils utils = new HttpUtils();
                utils.send(HttpMethod.GET, Constants.EXHIJSON_URL, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println("请求成功");
                        String result = responseInfo.result;
                        System.out.println(result);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = EXHIDATA_REQUEST_SUCCESS;
                        mHandler.sendMessage(msg);

                    }

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        System.out.println("请求失败");
                        System.out.println("msg:"+msg);
                        e.printStackTrace();
                        Message message = new Message();
                        message.what = EXHIDATA_REQUEST_FAILURE;
                        message.obj = msg;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * 解析json
     * @param result
     */
    private void processResult(String result) {
        Gson gson = new Gson();
        mExhibitionData = gson.fromJson(result, ExhibitionData.class);
        System.out.println("mExhibitionData解析结果:" + mExhibitionData.toString());

        mExhiList = mExhibitionData.exhiData;
        if (mAdapter == null ) {
            mAdapter = new ExhibitionAdapter(this, mExhiList);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }
}
