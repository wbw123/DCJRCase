package com.chase.dcjrCase.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.CaseAdapter;
import com.chase.dcjrCase.bean.CaseData;
import com.chase.dcjrCase.ui.activity.CaseDetailActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.chase.dcjrCase.bean.CaseData.DataBean.CaseDataBean;

import java.util.ArrayList;

/**
 * Created by chase on 2017/9/5.
 */

public class CaseFragment extends BaseFragment {
    private static final int CASEDATA_REQUEST_SUCCESS = 201;//请求成功码
    private static final int CASEDATA_REQUEST_FAILURE = 202;//请求失败码

    private ListView mListView;
    private ArrayList<String> mListItems;
    private RefreshLayout mRefreshLayout;
    private CaseAdapter mCaseAdapter;
    private RelativeLayout mRlError;
    private CaseData mCaseData;
    private ArrayList<CaseDataBean> mCaseList;
    private static int count;//用来记录第一次加载的条目数,以及在加载更多后加载的条目数

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CASEDATA_REQUEST_SUCCESS:
                    String result = (String) msg.obj;
                    mRlError.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    //解析json数据
                    processResult(result,count);
                    //写缓存
//                    CacheUtils.setCache(mUrl, result, mActivity);

                    //收起下拉刷新控件
//                    mListView.onRefreshComplete(true);
                    break;
                case CASEDATA_REQUEST_FAILURE:
                    String message = (String) msg.obj;
                    Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                    //添加加载失败布局
                    mRlError.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    Toast.makeText(mActivity, "请检查是否连接网络!", Toast.LENGTH_SHORT).show();
                    break;
//                case MORE_REQUEST_SUCCESS:
//                    String resultMore = (String) msg.obj;
//                    //解析json数据
//                    processResult(resultMore, true);
//                    //写缓存
//                    CacheUtils.setCache(mUrl, resultMore, mActivity);
//
//                    //收起下拉刷新控件
//                    mListView.onRefreshComplete(true);
//                    break;
//                case MORE_REQUEST_FAILURE:
//                    String messageMore = (String) msg.obj;
//                    Toast.makeText(mActivity, messageMore, Toast.LENGTH_LONG).show();
//                    //收起下拉刷新控件
//                    mListView.onRefreshComplete(false);
//                    break;
            }
        }
    };
    private Button mBtnReLoad;


    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.fragment_case, null);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mListView = view.findViewById(R.id.lv_list_case);
        mRlError = view.findViewById(R.id.rl_error);
        mBtnReLoad = view.findViewById(R.id.btn_reload);

        mBtnReLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("重新加载被点击了");
                initData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mActivity, CaseDetailActivity.class));
            }
        });

        return view;
    }
    @Override
    public void initData() {
        System.out.println("casefragment 加载了");


        //        SystemClock.sleep(1000);/*模拟请求服务器的延时过程*/

        //触发自动刷新
        mRefreshLayout.autoRefresh();
        System.out.println("home detail1 加载数据");
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                System.out.println("home detail1 下拉刷新 加载数据");
                refreshlayout.finishRefresh(2000);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 10;
                        getDataFromServer();//通过网络获取数据
                        refreshlayout.finishRefresh();//完成刷新
                        refreshlayout.setLoadmoreFinished(false);//可以出发加载更多事件
                    }
                }, 2000);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                System.out.println("home detail1 下拉 加载数据");
                refreshlayout.finishLoadmore(2000);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count+=10;
                        getDataFromServer();//通过网络获取数据
//                        caseAdapter.notifyDataSetChanged();//刷新adapter
                        refreshlayout.finishLoadmore();//完成加载更多
                        System.out.println("mCaseList大小:"+mCaseList.size());
                        System.out.println("count:"+count);
                        if (count > mCaseList.size()) {
                            Toast.makeText(mActivity, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshlayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
                        }
                    }
                }, 2000);
            }
        });

    }


    /**
     * 从网络上获取数据
     */
    public void getDataFromServer() {
        new Thread() {
            @Override
            public void run() {
                HttpUtils utils = new HttpUtils();
                utils.send(HttpMethod.GET, "http://192.168.141.81:8080/dcjr/caseItem.json", new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println("请求成功");
                        String result = responseInfo.result;
                        System.out.println(result);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = CASEDATA_REQUEST_SUCCESS;
                        mHandler.sendMessage(msg);

                    }

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        System.out.println("请求失败");
                        System.out.println("msg:"+msg);
                        e.printStackTrace();
                        Message message = new Message();
                        message.what = CASEDATA_REQUEST_FAILURE;
                        message.obj = msg;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();

    }
    /**
     * 解析json数据
     */
    private void processResult(String result,int count) {
        Gson gson = new Gson();
        mCaseData = gson.fromJson(result, CaseData.class);
        System.out.println("mCaseData解析结果:" + mCaseData.toString());

        mCaseList = mCaseData.data.caseData;
        if (mCaseAdapter == null ) {
            mCaseAdapter = new CaseAdapter(mActivity, mCaseList);
            mListView.setAdapter(mCaseAdapter);
        } else {
            mCaseAdapter.notifyDataSetChanged();
        }

    }

    public static int getCount(){
        return count;
    }


}
