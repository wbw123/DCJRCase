package com.chase.dcjrCase.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.TechAdapter;
import com.chase.dcjrCase.adapter.TopTechAdapter;
import com.chase.dcjrCase.bean.HistoryBean;
import com.chase.dcjrCase.bean.TechData;
import com.chase.dcjrCase.bean.TechData.DataBean.TechDataBean;
import com.chase.dcjrCase.bean.TechData.DataBean.TopTechBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.uitl.CacheUtils;
import com.chase.dcjrCase.uitl.PrefUtils;
import com.chase.dcjrCase.view.HorizontalScrollViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class TechActivity extends AppCompatActivity {

    private static final int TECHDATA_REQUEST_SUCCESS = 401;
    private static final int TECHDATA_REQUEST_FAILURE = 402;

    private ListView mListView;
    private RefreshLayout mRefreshLayout;//整个布局
    private FrameLayout mFrameLayout;//轮播图整体布局
    private HorizontalScrollViewPager mViewPager;//轮播图viewpager
    private TextView mTopTechTitle;//轮播图标题
    private CirclePageIndicator mIndicator;//小红点指示器
    private RelativeLayout mRlError;//加载失败布局
    private ImageView mImageView;
    private Button mBtnReLoad;//加载失败时 重新加载按钮

    private ArrayList<TopTechBean> mTopTech;//头条科技的网络数据
    private TopTechAdapter mTopTechAdapter;//头条科技适配器
    private ArrayList<TechDataBean> mListTech;//科技的网络数据
    private TechAdapter mTechAdapter;//条目的适配器
    private ArrayList<HistoryBean> mTechHistory = new ArrayList<>();
    private ArrayList<HistoryBean> mTopTechHistory = new ArrayList<>();

    private static int count = 10;//用来记录第一次加载的条目数,以及在加载更多后加载的条目数
    private String mCache;//条目缓存数据 json字符串

    private Handler mIndicatorHandler = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TECHDATA_REQUEST_SUCCESS:
                    String result = (String) msg.obj;
                    mRlError.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mFrameLayout.setVisibility(View.VISIBLE);
                    //解析json数据
                    processResult(result);
                    //写缓存 将成功读取的json字符串写入XML中保存
                    CacheUtils.setCache(Constants.TECHJSON_URL, result, getApplicationContext());
                    break;
                case TECHDATA_REQUEST_FAILURE:
                    String message = (String) msg.obj;
                    Toast.makeText(getApplicationContext(), "请检查是否连接网络!", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(mCache)) {
                        //发现缓存 读缓存 显示布局
                        System.out.println("tech 发现缓存");
                        mRlError.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mFrameLayout.setVisibility(View.VISIBLE);
                    } else {
                        //没有缓存 添加加载失败布局
                        mRlError.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        mFrameLayout.setVisibility(View.GONE);
                    }

                    break;
            }
        }
    };
    private TechDataBean techDataBean;
    private HistoryBean mTeachHistoryBean;
    private HistoryBean mTopTechHistoeyBean;
    private TopTechBean topTechBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech);
        initView();
        initData();

    }

    public void initView() {
        mRefreshLayout = (RefreshLayout) findViewById(R.id.tech_refresh_layout);
        mFrameLayout = (FrameLayout) findViewById(R.id.fl_TechViewPager);
        mViewPager = (HorizontalScrollViewPager) findViewById(R.id.tech_viewpager);
        mTopTechTitle = (TextView) findViewById(R.id.tech_vp_title);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mListView = (ListView) findViewById(R.id.lv_list_tech);
        mRlError = (RelativeLayout) findViewById(R.id.rl_error);
        mImageView = (ImageView) findViewById(R.id.page_iv);
        mBtnReLoad = (Button) findViewById(R.id.btn_reload);

        mBtnReLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("tech 重新加载了被点击了");
                initData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 /*条目跳转*/
                techDataBean = mListTech.get(position);
                System.out.println(mListTech.size());
                Intent intent = new Intent(TechActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constants.HOME_URL + techDataBean.url);//webView链接
                startActivity(intent);

                 /*点击条目标记已读状态*/
                System.out.println("++++++++++++++++++++++++++++" + techDataBean);
                //当前点击的item的标题颜色置灰
                TextView tvTitle = view.findViewById(R.id.tv_tech_title);
                TextView tvDate = view.findViewById(R.id.tv_tech_date);
                tvTitle.setTextColor(Color.argb(255, 155, 155, 155));
                tvDate.setTextColor(Color.argb(255, 155, 155, 155));
                //将已读状态持久化到本地
                //key:read_ids; value:id
                String readIds = PrefUtils.getString("read_ids", "", getApplicationContext());
                System.out.println("________________________" + readIds);
                if (!readIds.contains(techDataBean.id)) {
                    readIds = readIds + techDataBean.id + ",";
                    PrefUtils.putString("read_ids", readIds, getApplicationContext());
                }

//                mTechHistory= new ArrayList<>();
                mTeachHistoryBean = new HistoryBean();
                mTeachHistoryBean.id = techDataBean.id;
                mTeachHistoryBean.title = techDataBean.title;
                mTeachHistoryBean.date = techDataBean.date;
                mTeachHistoryBean.imgUrl = techDataBean.imgUrl;
                mTeachHistoryBean.url = Constants.HOME_URL + "/dcjr/tech/tech" + id + ".html";
                mTechHistory.add(mTeachHistoryBean);
            }
        });

    }

    public void initData() {
        //获取缓存
        mCache = CacheUtils.getCache(Constants.TECHJSON_URL, this);
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
                refreshlayout.finishLoadmore(1500);
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
                refreshlayout.finishLoadmore(1000);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count += 10;
                        if (!TextUtils.isEmpty(mCache)) {
                            // 有缓存 解析json缓存
                            System.out.println("发现缓存....");
                            processResult(mCache);
                        }
                        getDataFromServer();//通过网络获取数据
                        refreshlayout.finishLoadmore();//完成加载更多
                        System.out.println("mListNews的大小:" + mListTech.size());
                        System.out.println("count:" + count);
                        if (count > mListTech.size()) {
                            Toast.makeText(getApplicationContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshlayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
                        }
                    }
                }, 1000);
            }
        });
    }

    /*
    * 请求网络数据
    * */
    public void getDataFromServer() {
        new Thread() {
            @Override
            public void run() {
                HttpUtils utils = new HttpUtils();
                utils.send(HttpMethod.GET, Constants.TECHJSON_URL, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println("tech 请求成功");
                        String result = responseInfo.result;
                        System.out.println(result);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = TECHDATA_REQUEST_SUCCESS;
                        mHandler.sendMessage(msg);
                        processResult(result);
                    }

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        System.out.println("tech 请求失败");
                        System.out.println("msg:" + msg);
                        e.printStackTrace();
                        Message message = new Message();
                        message.what = TECHDATA_REQUEST_FAILURE;
                        message.obj = msg;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /*
    * 解析json数据
    * */
    public void processResult(String result) {
        Gson gson = new Gson();
        TechData mTechData = gson.fromJson(result, TechData.class);
        System.out.println("tech解析结果" + mTechData.toString());

        /*头条科技*/
        mTopTech = mTechData.data.topTech;
        if (mTopTechAdapter == null) {
            mTopTechAdapter = new TopTechAdapter(mTopTech, this);
            mViewPager.setAdapter(mTopTechAdapter);
        } else {
            mTopTechAdapter.notifyDataSetChanged();
        }

        /*前沿科技list条目*/
        mListTech = mTechData.data.techData;
        if (mTechAdapter == null) {
            mTechAdapter = new TechAdapter(mListTech, this);
            mListView.setAdapter(mTechAdapter);
        } else {
            mTechAdapter.notifyDataSetChanged();
        }
        //indicator绑定ViewPager
        indicatorBindViewPager();
        //TopTech两秒切换一次
        autoChangeAfter2s();
    }

    /**
     * indicator绑定ViewPager
     */
    private void indicatorBindViewPager() {
        mIndicator.setViewPager(mViewPager);//将轮播图和指示器绑定
        mIndicator.setSnap(true);// 快照模式 取消则为粘性模式
//        mIndicator.onPageSelected(0);// 将小圆点位置归零, 解决它会在页面销毁时仍记录上次位置的bug
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //标题
                TopTechBean topTechBean = mTopTech.get(position);
                mTopTechTitle.setText(topTechBean.title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //当前viewpager是哪一页就设置哪一页的标题
        int currentItem = mViewPager.getCurrentItem();
        mTopTechTitle.setText(mTopTech.get(currentItem).title);
    }

    /**
     * TopTech两秒切换一次
     */

    private void autoChangeAfter2s() {
        if (mIndicatorHandler == null) {
            mIndicatorHandler = new Handler() {
                public void handleMessage(Message msg) {
                    int currentItem = mViewPager.getCurrentItem();

                    if (currentItem < mTopTech.size() - 1) {
                        currentItem++;
                    } else {
                        currentItem = 0;
                    }

                    mViewPager.setCurrentItem(currentItem);

                    mIndicatorHandler.sendEmptyMessageDelayed(0, 2000);
                }
            };

            //  延时2秒切换广告条
            mIndicatorHandler.sendEmptyMessageDelayed(0, 2000);

            mViewPager.setOnTouchListener(new View.OnTouchListener() {
                private float mDownX;
                private float mDownY;
                private long mDownTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            System.out.println("ACTION_DOWN");
                            mDownX = event.getX();
                            mDownY = event.getY();
                            mDownTime = System.currentTimeMillis();
                            // 删除所有消息
                            mIndicatorHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_CANCEL:// 事件取消(当按下后,然后移动下拉刷新,导致抬起后无法响应ACTION_UP,
                            // 但此时会响应ACTION_CANCEL,也需要继续播放轮播条)
                        case MotionEvent.ACTION_UP:

                            float upX = event.getX();
                            float upY = event.getY();
                            long upTime = System.currentTimeMillis();
                            // 设置轮播图点击事件
                            if (mDownX == upX && mDownY == upY) {
                                if (upTime - mDownTime < 500) {
//                                onClickListener.onClick(mViewPager.getCurrentItem() % mTopnews.size());
                                    int position = mViewPager.getCurrentItem();
                                    topTechBean = mTopTech.get(position);
                                    System.out.println("当前viewpager：" + position);
                                    System.out.println("当前mTopTech：" + topTechBean);
                                    Intent TopTechIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                                    TopTechIntent.putExtra("url", Constants.HOME_URL + mTopTech.get(position).url);//webView链接
                                    startActivity(TopTechIntent);
//                                    mTopTechHistory = new ArrayList<>();
                                    mTopTechHistoeyBean = new HistoryBean();
                                    mTopTechHistoeyBean.id = topTechBean.id;
                                    mTopTechHistoeyBean.title = topTechBean.title;
                                    mTopTechHistoeyBean.date = topTechBean.date;
                                    mTopTechHistoeyBean.imgUrl = topTechBean.imgUrl;
                                    mTopTechHistoeyBean.url = Constants.HOME_URL + topTechBean.url;
                                    mTopTechHistory.add(mTopTechHistoeyBean);

                                }
                            }

                            // 延时2秒切换广告条
                            mIndicatorHandler.sendEmptyMessageDelayed(0, 2000);
                            break;
                        default:
                            break;
                    }

                    return false;
                }

            });
        }
    }

    public static int getCount() {
        return count;
    }

}
