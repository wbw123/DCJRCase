package com.chase.dcjrCase.ui.fragment.detail;

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.HomeDetailAdapter1;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * Created by chase on 2017/9/6.
 */

public class HomeDetailFragment1 extends BaseChildFragment {

    private ListView mListView;
    private ArrayList<String> mListItems;
    private RefreshLayout mRefreshLayout;
    private HomeDetailAdapter1 myAdapter;

    @Override
    protected View getSuccessView() {
//        TextView view = new TextView(mActivity);
//        view.setText("HomeDetailFragment1");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.fragment_home_detail1, null);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mListView = view.findViewById(R.id.lv_list);

        System.out.println("home detail1 加载布局");
        return view;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBar actionBar = mActivity.getActionBar();
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 触摸按下时的操作
                        actionBar.hide();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 触摸移动时的操作

                        break;
                    case MotionEvent.ACTION_UP:
                        // 触摸抬起时的操作
                        actionBar.show();
                        break;
                }
                return false;

            }
        });
    }*/

    @Override
    protected Object requestData() {
//        SystemClock.sleep(1000);/*模拟请求服务器的延时过程*/

        //触发自动刷新
        mRefreshLayout.autoRefresh();
        System.out.println("home detail1 加载数据");
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                System.out.println("home detail1 下拉刷新 加载数据");
//                refreshlayout.finishRefresh(2000);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mListItems != null){
                            mListItems.clear();//清空所有数据
                        }
                        initData();//加载第一页数据(因为数据已经清空了)
                        if (myAdapter == null) {
                            myAdapter = new HomeDetailAdapter1(mActivity, mListItems);
                            mListView.setAdapter(myAdapter);
                        } else {
                            myAdapter.notifyDataSetChanged();
                        }
//                        mAdapter.refresh(initData());
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
//                refreshlayout.finishLoadmore(2000);
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();//每次都会添加十条数据
                        myAdapter.notifyDataSetChanged();//刷新adapter
                        refreshlayout.finishLoadmore();//完成加载更多
                        if (myAdapter.getCount() > 60) {
                            Toast.makeText(mActivity, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshlayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
                        }
                    }
                }, 2000);
            }
        });


        return "";/*加载成功*/
    }

    private void initData() {
        if (mListItems == null) {
            mListItems = new ArrayList<>();
        }
        for (int i = 1; i <= 15; i++) {
            mListItems.add("数据");
        }
    }
}
