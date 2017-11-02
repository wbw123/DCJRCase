package com.chase.dcjrCase.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.CaseAdapter;
import com.chase.dcjrCase.bean.CaseData;
import com.chase.dcjrCase.bean.CaseData.DataBean.CaseDataBean;
import com.chase.dcjrCase.bean.HistoryBean;
import com.chase.dcjrCase.dao.HistoryDao;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.activity.WebViewActivity;
import com.chase.dcjrCase.uitl.CacheUtils;
import com.chase.dcjrCase.uitl.PrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * Created by chase on 2017/9/5.
 */

public class CaseFragment extends BaseFragment {
    private static final int CASEDATA_REQUEST_SUCCESS = 201;//请求成功码
    private static final int CASEDATA_REQUEST_FAILURE = 202;//请求失败码

    /*控件*/
    private ListView mListView;
    private RefreshLayout mRefreshLayout;//整个布局
    private Button mBtnReLoad;//加载失败时 重新加载按钮
    private RelativeLayout mRlError;//加载失败布局

    /*mvc*/
    private CaseAdapter mCaseAdapter;//listview对应的adapter
    private CaseData mCaseData;//需要显示的数据javabean
    private ArrayList<CaseDataBean> mCaseList;//数据集合

    private static int count;//用来记录第一次加载的条目数,以及在加载更多后加载的条目数
    private String mCache;//条目缓存数据 json字符串

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CASEDATA_REQUEST_SUCCESS:
                    String result =  (String) msg.obj;
                    mRlError.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    //解析json数据
                    processResult(result);
                    //写缓存 将成功读取的json字符串写入XML中保存
                    CacheUtils.setCache(Constants.CASEJSON_URL, result, mActivity);
                    break;
                case CASEDATA_REQUEST_FAILURE:
                    String message = (String) msg.obj;
//                    Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                    Toast.makeText(mActivity, "请检查是否连接网络!", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(mCache)) {
                        // 有缓存 读取缓存数据 显示布局
                        System.out.println("发现缓存....");
//                        processResult(mCache, count);
                        mRlError.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }else {
                        //没有缓存 添加加载失败布局
                        mRlError.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    private String result;
    private HistoryDao mDao;
    private HistoryBean historyBean;


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
//                startActivity(new Intent(mActivity, WebViewActivity.class));

                /*条目跳转*/
                CaseDataBean caseDataBean = mCaseList.get(position);
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", Constants.HOME_URL+caseDataBean.url);//webView链接
                /*收藏*/
                intent.putExtra("title",caseDataBean.title);
                intent.putExtra("author",caseDataBean.author);
                intent.putExtra("date",caseDataBean.date);
                intent.putExtra("imgUrl",caseDataBean.imgUrl);
                intent.putExtra("from",caseDataBean.from);
                intent.putExtra("type",caseDataBean.type);
                intent.putExtra("id",caseDataBean.id);
                mActivity.startActivity(intent);

                /*点击条目标记已读状态*/

                System.out.println("!!!!!!!!!!!!!!!!!!!!"+caseDataBean);
                //当前点击的item的标题颜色置灰
                TextView tvTitle = view.findViewById(R.id.tv_case_title);
                TextView tvDate = view.findViewById(R.id.tv_case_date);
                TextView tvFrom = view.findViewById(R.id.tv_case_from);
                tvTitle.setTextColor(Color.argb(255,155,155,155));
                tvDate.setTextColor(Color.argb(255,155,155,155));
                tvFrom.setTextColor(Color.argb(255,155,155,155));
                //将已读状态持久化到本地
                //key:read_ids; value:id
                String readIds = PrefUtils.getString("read_ids","",mActivity);
                if (!readIds.contains(caseDataBean.id)) {
                    readIds = readIds + caseDataBean.id + ",";
                    PrefUtils.putString("read_ids", readIds, mActivity);
                }
                //将已读条目插入数据库
                historyBean = new HistoryBean();
                historyBean.id = caseDataBean.id;
                historyBean.title = caseDataBean.title;
                historyBean.date = caseDataBean.date;
                historyBean.imgUrl = caseDataBean.imgUrl;
                historyBean.url = caseDataBean.url;
                historyBean.author = caseDataBean.author;
                historyBean.from = caseDataBean.from;
                historyBean.type = String.valueOf(caseDataBean.type);
                //判断是否添加到历史记录
                IsInsert();
            }
        });

        return view;
    }
    @Override
    public void initData() {
        mDao = new HistoryDao(getActivity());
        System.out.println("casefragment 加载了");
        //获取缓存
        mCache = CacheUtils.getCache(Constants.CASEJSON_URL, mActivity);

        //        SystemClock.sleep(1000);/*模拟请求服务器的延时过程*/

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

                        count = 10;

                        if (!TextUtils.isEmpty(mCache)) {
                            // 有缓存 解析json缓存
                            System.out.println("发现缓存....");
                            processResult(mCache);
                        }
                        // 即使发现有缓存,仍继续调用网络, 获取最新数据
                        getDataFromServer();//通过网络获取数据
                        refreshlayout.finishRefresh();//完成刷新
                        refreshlayout.setLoadmoreFinished(false);//可以出发加载更多事件
                    }
                }, 1500);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                System.out.println("home detail1 下拉 加载数据");
                refreshlayout.finishLoadmore(1000);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count+=10;
                        if (!TextUtils.isEmpty(mCache)) {
                            // 有缓存 解析json缓存
                            System.out.println("发现缓存....");
                            processResult(mCache);
                        }
                        getDataFromServer();//通过网络获取数据
                        refreshlayout.finishLoadmore();//完成加载更多
                        System.out.println("mCaseList大小:"+mCaseList.size());
                        System.out.println("count:"+count);
                        if (count > mCaseList.size()) {
                            Toast.makeText(mActivity, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshlayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
                        }
                    }
                }, 1000);
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
                utils.send(HttpMethod.GET, Constants.CASEJSON_URL, new RequestCallBack<String>() {
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
    private void processResult(String result) {
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

    /*判断是否添加到历史记录
   * 如果已经添加到历史记录则先删除后添加
   * 如果没有添加则添加到历史记录*/
    public void IsInsert(){
        if (mDao.query(historyBean.id)){
            mDao.deleteID(historyBean.id);
            mDao.insert(historyBean);
            System.out.println("delete______________________");
        }else {
            mDao.insert(historyBean);
            System.out.println("insert___________________");
        }
    }

    public static int getCount(){
        return count;
    }

}
