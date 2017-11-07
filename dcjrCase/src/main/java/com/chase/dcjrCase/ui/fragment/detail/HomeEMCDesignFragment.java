package com.chase.dcjrCase.ui.fragment.detail;

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
import com.chase.dcjrCase.adapter.EMCDesignAdapter;
import com.chase.dcjrCase.bean.EMCDesignData;
import com.chase.dcjrCase.bean.EMCDesignData.DesignEMCdataBean;
import com.chase.dcjrCase.global.Constants;
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

public class HomeEMCDesignFragment extends BaseFragment {
    private static final int HOME_EMC_DESIGNDATA_REQUEST_SUCCESS = 105;
    private static final int HOME_EMC_DESIGNDATA_REQUEST_FAILURE = 106;
    private ListView mListView;
    private RefreshLayout mRefreshLayout;
    private static int count = 12;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HOME_EMC_DESIGNDATA_REQUEST_SUCCESS:
                    String result = (String) msg.obj;
//                    mRlError.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    //解析json数据
                    processResult(result);
                    //写缓存 将成功读取的json字符串写入XML中保存
                    CacheUtils.setCache(Constants.EMCDESIGN_JSON_URL, result, mActivity);
                    break;
                case HOME_EMC_DESIGNDATA_REQUEST_FAILURE:
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
    private EMCDesignAdapter mAdapter;
    private String mCache;
    private EMCDesignData mEmcDesignData;
    private ArrayList<DesignEMCdataBean> mDesignEMCList;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_home_design_emc, null);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mListView = view.findViewById(R.id.lv_list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DesignEMCdataBean designEMCdataBean = mDesignEMCList.get(position);
                System.out.println("position:"+position);
                System.out.println("designEMCdataBean:"+designEMCdataBean);
                /*条目跳转*/

                /*点击条目标记已读状态*/
                int type = designEMCdataBean.type;
                System.out.println("type:"+type);
                if (type == 1) {
                    TextView tvTitle = view.findViewById(R.id.tv_emc_design_title);
                    TextView tvDate = view.findViewById(R.id.tv_emc_design_date);
//                    TextView tvAuthor = view.findViewById(R.id.tv_emc_design_author);
                    TextView tvFrom = view.findViewById(R.id.tv_emc_design_from);
                    tvTitle.setTextColor(Color.argb(255, 155, 155, 155));
                    tvFrom.setTextColor(Color.argb(255, 155, 155, 155));
//                    tvAuthor.setTextColor(Color.argb(255, 155, 155, 155));
                    tvDate.setTextColor(Color.argb(255, 155, 155, 155));
                    //将已读状态持久化到本地
                    //key:read_ids; value:id
                    String readIds = PrefUtils.getString("read_ids", "", mActivity);
                    if (!readIds.contains(designEMCdataBean.id)) {
                        readIds = readIds + designEMCdataBean.id + ",";
                        PrefUtils.putString("read_ids", readIds, mActivity);
                    }
                } else {
                    TextView tvTitle = view.findViewById(R.id.tv_emc_design_title);
                    TextView tvDate = view.findViewById(R.id.tv_emc_design_date);
                    TextView tvAuthor = view.findViewById(R.id.tv_emc_design_author);
                    TextView tvFrom = view.findViewById(R.id.tv_emc_design_from);
                    tvTitle.setTextColor(Color.argb(255, 155, 155, 155));
                    tvFrom.setTextColor(Color.argb(255, 155, 155, 155));
                    tvAuthor.setTextColor(Color.argb(255, 155, 155, 155));
                    tvDate.setTextColor(Color.argb(255, 155, 155, 155));
                    //将已读状态持久化到本地
                    //key:read_ids; value:id
                    String readIds = PrefUtils.getString("read_ids", "", mActivity);
                    if (!readIds.contains(designEMCdataBean.id)) {
                        readIds = readIds + designEMCdataBean.id + ",";
                        PrefUtils.putString("read_ids", readIds, mActivity);
                    }
                }
            }
        });


        return view;
    }
    @Override
    public void initData() {
        //获取缓存
        mCache = CacheUtils.getCache(Constants.EMCDESIGN_JSON_URL, mActivity);
        if (!TextUtils.isEmpty(mCache)) {
            System.out.println("有缓存  不自动刷新");
            processResult(mCache);
        } else {
            //触发自动刷新
            mRefreshLayout.autoRefresh();
            System.out.println("无缓存  自动刷新");
        }

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
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
                        if (count > mDesignEMCList.size()) {
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
        utils.send(HttpMethod.GET, Constants.EMCDESIGN_JSON_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println("请求成功");
                String result = responseInfo.result;
                System.out.println(result);
                Message msg = new Message();
                msg.obj = result;
                msg.what = HOME_EMC_DESIGNDATA_REQUEST_SUCCESS;
                mHandler.sendMessage(msg);

            }

            @Override
            public void onFailure(HttpException e, String msg) {
                System.out.println("请求失败");
                System.out.println("msg:" + msg);
                e.printStackTrace();
                Message message = new Message();
                message.what = HOME_EMC_DESIGNDATA_REQUEST_FAILURE;
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
        mEmcDesignData = gson.fromJson(result, EMCDesignData.class);
        System.out.println("mEmcDesignData解析结果:" + mEmcDesignData.toString());

        /*listview*/
        mDesignEMCList = mEmcDesignData.designEMCdata;
        if (mAdapter == null) {
            System.out.println("mDesignEMCList:"+mDesignEMCList);
            mAdapter = new EMCDesignAdapter(mActivity, mDesignEMCList);
            mListView.setAdapter(this.mAdapter);
        } else {
            System.out.println("notifyDataSetChanged");
            mAdapter.notifyDataSetChanged();
        }
    }
    public static int getCount() {
        return count;
    }
}
