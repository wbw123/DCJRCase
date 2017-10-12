package com.chase.dcjrCase.ui.fragment;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.TopNewsAdapter;
import com.chase.dcjrCase.bean.NewsData;
import com.chase.dcjrCase.bean.NewsData.DataBean.TopNewsBean;
import com.chase.dcjrCase.global.Constants;
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
    /*控件*/
    private ListView mListView;
    private RefreshLayout mRefreshLayout;//整个布局
    private Button mBtnReLoad;//加载失败时 重新加载按钮
    private RelativeLayout mRlError;//加载失败布局
    private CirclePageIndicator mIndicator;
    private TextView mTopNewsTitle;
    private ViewPager mViewPager;
    private Handler mHandler = null;
    // 头条新闻的网络数据
    private ArrayList<TopNewsBean> mTopnews;
    private TopNewsAdapter mNewsAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_news, null);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mListView = view.findViewById(R.id.lv_list_news);
        mRlError = view.findViewById(R.id.rl_error);
        mBtnReLoad = view.findViewById(R.id.btn_reload);

        mViewPager = view.findViewById(R.id.news_viewpager);
        mIndicator = view.findViewById(R.id.indicator);
        mTopNewsTitle = view.findViewById(R.id.news_vp_title);

        return view;
    }

    @Override
    public void initData() {
        //触发自动刷新
        mRefreshLayout.autoRefresh();
        System.out.println("home detail1 加载数据");
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                System.out.println("home detail1 下拉刷新 加载数据");
                refreshlayout.finishRefresh(1500);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        count = 10;
//
//                        if (!TextUtils.isEmpty(mCache)) {
//                            // 有缓存 解析json缓存
//                            System.out.println("发现缓存....");
//                            processResult(mCache, count);
//                        }
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
//                        count+=10;
//                        if (!TextUtils.isEmpty(mCache)) {
//                            // 有缓存 解析json缓存
//                            System.out.println("发现缓存....");
//                            processResult(mCache, count);
//                        }
                        getDataFromServer();//通过网络获取数据
                        refreshlayout.finishLoadmore();//完成加载更多
//                        System.out.println("mCaseList大小:"+mCaseList.size());
//                        System.out.println("count:"+count);
//                        if (count > mCaseList.size()) {
//                            Toast.makeText(mActivity, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
//                            refreshlayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
//                        }
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
                        processResult(result);
//                        Message msg = new Message();
//                        msg.obj = result;
//                        msg.what = CASEDATA_REQUEST_SUCCESS;
//                        mHandler.sendMessage(msg);

                    }

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        System.out.println("请求失败");
                        System.out.println("msg:" + msg);
                        e.printStackTrace();
//                        Message message = new Message();
//                        message.what = CASEDATA_REQUEST_FAILURE;
//                        message.obj = msg;
//                        mHandler.sendMessage(message);
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

        mTopnews = mNewsData.data.topNews;
        if (mNewsAdapter == null) {
            mNewsAdapter = new TopNewsAdapter(mTopnews,mActivity);
            mViewPager.setAdapter(mNewsAdapter);
        } else {
            mNewsAdapter.notifyDataSetChanged();
            System.out.println("刷新adapter notifyDataSetChanged");
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
        if (mHandler == null) {
            mHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    int currentItem = mViewPager.getCurrentItem();

                    if (currentItem < mTopnews.size() - 1) {
                        currentItem++;
                    } else {
                        currentItem = 0;
                    }

                    mViewPager.setCurrentItem(currentItem);

                    mHandler.sendEmptyMessageDelayed(0, 2000);
                }

                ;
            };

            //  延时2秒切换广告条
            mHandler.sendEmptyMessageDelayed(0, 2000);

            mViewPager.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            System.out.println("ACTION_DOWN");
                            // 删除所有消息
                            mHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_CANCEL:// 事件取消(当按下后,然后移动下拉刷新,导致抬起后无法响应ACTION_UP,
                            // 但此时会响应ACTION_CANCEL,也需要继续播放轮播条)
                        case MotionEvent.ACTION_UP:
                            // 延时2秒切换广告条
                            mHandler.sendEmptyMessageDelayed(0, 2000);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }



//    /**
//     * ListView的Adapter
//     */
//    class MyListAdapter extends BaseAdapter {
//
//        private final BitmapUtils bitmapUtils;
//
//        public MyListAdapter() {
//            bitmapUtils = new BitmapUtils(mActivity);
//            bitmapUtils.configDefaultLoadingImage(R.mipmap.ic_launcher);
//        }
//
//        @Override
//        public int getCount() {
//            return mNewsList.size();
//        }
//
//        @Override
//        public NewsData.DataBean.NewsBean getItem(int position) {
//            return mNewsList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup viewGroup) {
//            ViewHolder holder = null;
//            if (convertView == null) {
//                convertView = View.inflate(mActivity, R.layout.item_list_news, null);
//                holder = new ViewHolder(convertView);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            NewsData.DataBean.NewsBean newsBean = getItem(position);
//
//            holder.tv_date.setText(newsBean.pubdate);
//            holder.tv_title.setText(newsBean.title);
//            bitmapUtils.display(holder.iv_icon, newsBean.listimage);
////            // 标记已读和未读
////            String readIds = PrefUtils.getString("read_ids", "", mActivity);
////            if (readIds.contains(newsBean.id)) {
////                // 已读
////                holder.tv_title.setTextColor(Color.GRAY);
////            } else {
////                // 未读
////                holder.tv_title.setTextColor(Color.BLACK);
////            }
//            return convertView;
//        }
//
//        class ViewHolder {
//            public ImageView iv_icon;
//            public TextView tv_title;
//            public TextView tv_date;
//
//            public ViewHolder(View view) {
//                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
//                tv_title = (TextView) view.findViewById(R.id.tv_title);
//                tv_date = (TextView) view.findViewById(R.id.tv_date);
//            }
//
//        }
//    }

}
