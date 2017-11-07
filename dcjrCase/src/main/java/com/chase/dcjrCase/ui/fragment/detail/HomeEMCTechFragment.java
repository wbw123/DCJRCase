package com.chase.dcjrCase.ui.fragment.detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.EMCTechAdapter;
import com.chase.dcjrCase.bean.EMCTechData;
import com.chase.dcjrCase.bean.EMCTechData.TechEMCdataBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.activity.WebViewActivity;
import com.chase.dcjrCase.ui.fragment.BaseFragment;
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
 * Created by chase on 2017/9/6.
 */

public class HomeEMCTechFragment extends BaseFragment {
    private static final int HOME_EMC_TECHDATA_REQUEST_SUCCESS = 103;
    private static final int HOME_EMC_TECHDATA_REQUEST_FAILURE = 104;
    private ListView mListView;
    private RefreshLayout mRefreshLayout;
    private static int count = 12;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HOME_EMC_TECHDATA_REQUEST_SUCCESS:
                    String result = (String) msg.obj;
//                    mRlError.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    //解析json数据
                    processResult(result);
                    //写缓存 将成功读取的json字符串写入XML中保存
                    CacheUtils.setCache(Constants.EMCTECH_JSON_URL, result, mActivity);
                    break;
                case HOME_EMC_TECHDATA_REQUEST_FAILURE:
                    String message = (String) msg.obj;
//                    Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                    Toast.makeText(mActivity, "请检查是否连接网络!", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(mCache)) {
                        // 有缓存 读取缓存数据 显示布局
                        System.out.println("发现缓存....");
                        processResult(mCache);
//                        mRlError.setVisibility(View.GONE);
//                        mListView.setVisibility(View.VISIBLE);
                    } else {
                        //没有缓存 添加加载失败布局
//                        mRlError.setVisibility(View.VISIBLE);
//                        mListView.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    private EMCTechData mEmcTechData;
    private ArrayList<TechEMCdataBean> mTechEMCList;
    private EMCTechAdapter mAdapter;
    private String mCache;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_home_tech_emc, null);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mListView = view.findViewById(R.id.lv_list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TechEMCdataBean techEMCdataBean = mTechEMCList.get(position);
                System.out.println("position:"+position);
                System.out.println("techEMCdataBean:"+techEMCdataBean);

                /*条目跳转*/
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", Constants.HOME_URL+techEMCdataBean.url);//webView链接
                /*收藏*/
                intent.putExtra("title",techEMCdataBean.title);
                intent.putExtra("author",techEMCdataBean.author);
                intent.putExtra("date",techEMCdataBean.date);
                intent.putExtra("imgUrl",techEMCdataBean.imgUrl);
                intent.putExtra("imgUrl1",techEMCdataBean.imgUrl1);
                intent.putExtra("imgUrl2",techEMCdataBean.imgUrl2);
                intent.putExtra("imgUrl3",techEMCdataBean.imgUrl3);
                intent.putExtra("from",techEMCdataBean.from);
                intent.putExtra("type",techEMCdataBean.type);
                intent.putExtra("id",techEMCdataBean.id);
                mActivity.startActivity(intent);

                /*点击条目标记已读状态*/
                int type = techEMCdataBean.type;
                System.out.println("type:"+type);
                if (type == 1) {
                    TextView tvTitle = view.findViewById(R.id.tv_emc_tech_title);
                    TextView tvDate = view.findViewById(R.id.tv_emc_tech_date);
//                    TextView tvAuthor = view.findViewById(R.id.tv_emc_tech_author);
                    TextView tvFrom = view.findViewById(R.id.tv_emc_tech_from);
                    tvTitle.setTextColor(Color.argb(255, 155, 155, 155));
                    tvFrom.setTextColor(Color.argb(255, 155, 155, 155));
//                    tvAuthor.setTextColor(Color.argb(255, 155, 155, 155));
                    tvDate.setTextColor(Color.argb(255, 155, 155, 155));
                    //将已读状态持久化到本地
                    //key:read_ids; value:id
                    String readIds = PrefUtils.getString("read_ids", "", mActivity);
                    if (!readIds.contains(techEMCdataBean.id)) {
                        readIds = readIds + techEMCdataBean.id + ",";
                        PrefUtils.putString("read_ids", readIds, mActivity);
                    }
                } else {
                    TextView tvTitle = view.findViewById(R.id.tv_emc_tech_title);
                    TextView tvDate = view.findViewById(R.id.tv_emc_tech_date);
                    TextView tvAuthor = view.findViewById(R.id.tv_emc_tech_author);
                    TextView tvFrom = view.findViewById(R.id.tv_emc_tech_from);
                    tvTitle.setTextColor(Color.argb(255, 155, 155, 155));
                    tvFrom.setTextColor(Color.argb(255, 155, 155, 155));
                    tvAuthor.setTextColor(Color.argb(255, 155, 155, 155));
                    tvDate.setTextColor(Color.argb(255, 155, 155, 155));
                    //将已读状态持久化到本地
                    //key:read_ids; value:id
                    String readIds = PrefUtils.getString("read_ids", "", mActivity);
                    if (!readIds.contains(techEMCdataBean.id)) {
                        readIds = readIds + techEMCdataBean.id + ",";
                        PrefUtils.putString("read_ids", readIds, mActivity);
                    }
                }
            }
        });

        return view;
    }
    @Override
    public void initData() {
        System.out.println("HomeEMCTechFragment initData()");

        //获取缓存
        mCache = CacheUtils.getCache(Constants.EMCTECH_JSON_URL, mActivity);
        if (!TextUtils.isEmpty(mCache)) {
            System.out.println("有缓存  不自动刷新");
            processResult(mCache);
        } else {
            //触发自动刷新
            mRefreshLayout.autoRefresh();
            System.out.println("无缓存  自动刷新");
        }
        System.out.println("HomeEMCTechFragment 加载数据");

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                System.out.println("HomeEMCTechFragment 下拉刷新 加载数据");
//                refreshlayout.finishRefresh(2000);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       count = 12;
                         if (!TextUtils.isEmpty(mCache)) {
                            // 有缓存 解析json缓存
                            System.out.println("发现缓存....");
                            processResult(mCache);
                        }
                        //通过网络获取数据
                        getDataFromServer();
                        refreshlayout.finishRefresh();//完成刷新
                        refreshlayout.setLoadmoreFinished(false);//可以出发加载更多事件
                    }
                }, 1500);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                System.out.println("HomeEMCTechFragment 上拉 加载数据");
//                refreshlayout.finishLoadmore(2000);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count += 12;
                        if (!TextUtils.isEmpty(mCache)) {
                            // 有缓存 解析json缓存
                            System.out.println("发现缓存....");
                            processResult(mCache);
                        }
                        // 即使发现有缓存,仍继续调用网络, 获取最新数据
                        //通过网络获取数据
                        getDataFromServer();
                        refreshlayout.finishLoadmore();//完成加载更多
                        if (count > mTechEMCList.size()) {
                            Toast.makeText(mActivity, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshlayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
                        }
                    }
                }, 1500);
            }
        });
    }

    /**
     * 通过网络获取数据
     */
    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, Constants.EMCTECH_JSON_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println("请求成功");
                String result = responseInfo.result;
                System.out.println(result);
                Message msg = new Message();
                msg.obj = result;
                msg.what = HOME_EMC_TECHDATA_REQUEST_SUCCESS;
                mHandler.sendMessage(msg);

            }

            @Override
            public void onFailure(HttpException e, String msg) {
                System.out.println("请求失败");
                System.out.println("msg:" + msg);
                e.printStackTrace();
                Message message = new Message();
                message.what = HOME_EMC_TECHDATA_REQUEST_FAILURE;
                message.obj = msg;
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 解析json数据
     */
    private void processResult(String result) {
        Gson gson = new Gson();
        mEmcTechData = gson.fromJson(result, EMCTechData.class);
        System.out.println("mEmcTechData解析结果:" + mEmcTechData.toString());

        /*listview*/
        mTechEMCList = mEmcTechData.techEMCdata;
        if (mAdapter == null) {
            System.out.println("mTechEMCList:"+mTechEMCList);
            mAdapter = new EMCTechAdapter(mActivity, mTechEMCList);
            mListView.setAdapter(mAdapter);
        } else {
            System.out.println("notifyDataSetChanged");
            mAdapter.notifyDataSetChanged();
        }
    }
    public static int getCount() {
        return count;
    }
}
