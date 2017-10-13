package com.chase.dcjrCase.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.NewsAdapter;
import com.chase.dcjrCase.adapter.TopNewsAdapter;
import com.chase.dcjrCase.bean.NewsData;
import com.chase.dcjrCase.bean.NewsData.DataBean.NewsDataBean;
import com.chase.dcjrCase.bean.NewsData.DataBean.TopNewsBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.activity.WebViewActivity;
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

/**
 * Created by chase on 2017/9/5.
 */

public class NewsFragment extends BaseFragment {
    private static final int NEWSDATA_REQUEST_SUCCESS = 301;//请求成功码
    private static final int NEWSDATA_REQUEST_FAILURE = 302;//请求失败码
    /*控件*/
    private ListView mListView;
    private RefreshLayout mRefreshLayout;//整个布局
    private Button mBtnReLoad;//加载失败时 重新加载按钮
    private RelativeLayout mRlError;//加载失败布局
    private FrameLayout mFrameLayout;//轮播图整体布局
    private CirclePageIndicator mIndicator;//小红点指示器
    private TextView mTopNewsTitle;//轮播图标题
    private HorizontalScrollViewPager mViewPager;//轮播图viewpager

    /*mvc*/
    private ArrayList<TopNewsBean> mTopnews;// 头条新闻的网络数据
    private ArrayList<NewsDataBean> mListNews;//list新闻的网络数据
    private TopNewsAdapter mTopNewsAdapter;//头条新闻适配器
    private NewsAdapter mListNewsAdapter;//条目新闻适配器

    private static int count;//用来记录第一次加载的条目数,以及在加载更多后加载的条目数
    private String mCache;//条目缓存数据 json字符串
    /*handler*/
    private Handler mIndicatorHandler = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEWSDATA_REQUEST_SUCCESS:
                    String result = (String) msg.obj;
                    mRlError.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mFrameLayout.setVisibility(View.VISIBLE);
                    //解析json数据
                    processResult(result);
                    //写缓存 将成功读取的json字符串写入XML中保存
                    CacheUtils.setCache(Constants.NEWSJSON_URL, result, mActivity);

                    break;
                case NEWSDATA_REQUEST_FAILURE:
                    String message = (String) msg.obj;
//                    Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                    Toast.makeText(mActivity, "请检查是否连接网络!", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(mCache)) {
                        // 有缓存 读取缓存数据 显示布局
                        System.out.println("发现缓存....");
//                        processResult(mCache);
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

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_news, null);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mListView = view.findViewById(R.id.lv_list_news);
        mRlError = view.findViewById(R.id.rl_error);
        mBtnReLoad = view.findViewById(R.id.btn_reload);

        mFrameLayout = view.findViewById(R.id.fl_viewpager);
        mViewPager = view.findViewById(R.id.news_viewpager);
        mIndicator = view.findViewById(R.id.indicator);
        mTopNewsTitle = view.findViewById(R.id.news_vp_title);


        mBtnReLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("重新加载被点击了");
                initData();
            }
        });

        /*listview条目点击事件*/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*条目跳转*/
                id = 24 - id;
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", Constants.HOME_URL + "/dcjr/news/news" + id + ".html");//webView链接
                mActivity.startActivity(intent);

                /*点击条目标记已读状态*/
                NewsDataBean newsDataBean = mListNews.get(position);
                //当前点击的item的标题颜色置灰
                TextView tvTitle = view.findViewById(R.id.tv_news_title);
                TextView tvDate = view.findViewById(R.id.tv_news_date);
                tvTitle.setTextColor(Color.argb(255, 155, 155, 155));
                tvDate.setTextColor(Color.argb(255, 155, 155, 155));
                //将已读状态持久化到本地
                //key:read_ids; value:id
                String readIds = PrefUtils.getString("read_ids", "", mActivity);
                if (!readIds.contains(newsDataBean.id)) {
                    readIds = readIds + newsDataBean.id + ",";
                    PrefUtils.putString("read_ids", readIds, mActivity);
                }
            }
        });

        return view;
    }

    @Override
    public void initData() {

        //获取缓存
        mCache = CacheUtils.getCache(Constants.NEWSJSON_URL, mActivity);
        //触发自动刷新
        mRefreshLayout.autoRefresh();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                System.out.println("news fragment 下拉刷新 加载数据");
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
                        count += 10;
                        if (!TextUtils.isEmpty(mCache)) {
                            // 有缓存 解析json缓存
                            System.out.println("发现缓存....");
                            processResult(mCache);
                        }
                        getDataFromServer();//通过网络获取数据
                        refreshlayout.finishLoadmore();//完成加载更多
                        System.out.println("mListNews的大小:" + mListNews.size());
                        System.out.println("count:" + count);
                        if (count > mListNews.size()) {
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
                utils.send(HttpMethod.GET, Constants.NEWSJSON_URL, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        System.out.println("请求成功");
                        String result = responseInfo.result;
                        System.out.println(result);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = NEWSDATA_REQUEST_SUCCESS;
                        mHandler.sendMessage(msg);

                    }

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        System.out.println("请求失败");
                        System.out.println("msg:" + msg);
                        e.printStackTrace();
                        Message message = new Message();
                        message.what = NEWSDATA_REQUEST_FAILURE;
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
        NewsData mNewsData = gson.fromJson(result, NewsData.class);
        System.out.println("mNewsData解析结果:" + mNewsData.toString());

        /*头条新闻*/
        mTopnews = mNewsData.data.topNews;
        if (mTopNewsAdapter == null) {
            mTopNewsAdapter = new TopNewsAdapter(mTopnews, mActivity);
            mViewPager.setAdapter(mTopNewsAdapter);
        } else {
            mTopNewsAdapter.notifyDataSetChanged();
            System.out.println("刷新adapter notifyDataSetChanged");
        }

        /*list条目新闻*/
        mListNews = mNewsData.data.newsData;
        System.out.println("mListNews==" + mListNews);
        if (mListNewsAdapter == null) {
            mListNewsAdapter = new NewsAdapter(mActivity, mListNews);
            mListView.setAdapter(mListNewsAdapter);
        } else {
            mListNewsAdapter.notifyDataSetChanged();
        }


        //indicator绑定ViewPager
        indicatorBindViewPager();

        //TopNews两秒切换一次
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
                TopNewsBean topNewsBean = mTopnews.get(position);
                mTopNewsTitle.setText(topNewsBean.title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //当前viewpager是哪一页就设置哪一页的标题
        int currentItem = mViewPager.getCurrentItem();
        mTopNewsTitle.setText(mTopnews.get(currentItem).title);
    }

    /**
     * TopNews两秒切换一次
     */
    private void autoChangeAfter2s() {
        if (mIndicatorHandler == null) {
            mIndicatorHandler = new Handler() {
                public void handleMessage(Message msg) {
                    int currentItem = mViewPager.getCurrentItem();

                    if (currentItem < mTopnews.size() - 1) {
                        currentItem++;
                    } else {
                        currentItem = 0;
                    }

                    mViewPager.setCurrentItem(currentItem);

                    mIndicatorHandler.sendEmptyMessageDelayed(0, 2000);
                }

                ;
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
                                    int positon = mViewPager.getCurrentItem();
                                    System.out.println("当前viewpager："+positon);
                                    Intent TopNewIntent = new Intent(mActivity, WebViewActivity.class);
                                    TopNewIntent.putExtra("url", Constants.HOME_URL + mTopnews.get(positon).url);//webView链接
                                    mActivity.startActivity(TopNewIntent);
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
