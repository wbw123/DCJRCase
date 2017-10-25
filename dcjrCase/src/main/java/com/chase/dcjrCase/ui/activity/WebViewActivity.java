package com.chase.dcjrCase.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.CollectionBean;
import com.chase.dcjrCase.dao.CollecDao;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressBar pbLoading;
    private String mUrl;
    private String mId;
    private String mTitle;
    private String mDate;
    private String mFrom;
    private String mAuthor;
    private String mImgUrl;
    private String mImgUrl1;
    private String mImgUrl2;
    private String mImgUrl3;
    private String mType;
    private CollecDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        /*初始化actionbar*/
        initActionBar();

        mWebView = (WebView) findViewById(R.id.wv_webview);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);


        /*用于收藏的数据*/
        mUrl = getIntent().getStringExtra("url");
        mId = getIntent().getStringExtra("id");
        mTitle = getIntent().getStringExtra("title");
        mDate = getIntent().getStringExtra("date");
        mFrom = getIntent().getStringExtra("from");
        mAuthor = getIntent().getStringExtra("author");
        mImgUrl = getIntent().getStringExtra("imgUrl");
        mImgUrl1 = getIntent().getStringExtra("imgUrl1");
        mImgUrl2 = getIntent().getStringExtra("imgUrl2");
        mImgUrl3 = getIntent().getStringExtra("imgUrl3");
        mType = String.valueOf(getIntent().getIntExtra("type",1));

        mDao = CollecDao.getInstance(this);

        System.out.println("mUrl:" + mUrl);
        // 加载网页
//        mWebView.loadUrl("http://192.168.141.81:8080/dcjr/case/case2.html");
        mWebView.loadUrl(mUrl);

        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
        settings.setUseWideViewPort(true);// 只是双击缩放
        settings.setJavaScriptEnabled(true);// 打开js功能

        mWebView.setWebViewClient(new WebViewClient() {
            // 网页开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("网页开始加载");
                pbLoading.setVisibility(View.VISIBLE);
            }


            // 网页跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("网页跳转:" + url);
                view.loadUrl(url);// 强制在当前页面加载网页, 不用跳浏览器
                return true;

            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");
                pbLoading.setVisibility(View.GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            // 加载进度回调
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("newProgress:" + newProgress);
                pbLoading.setMax(100);
                pbLoading.setProgress(newProgress);//设置水平进度条进度
            }

            // 网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("title:" + title);
            }
            //网页图标

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });
    }

    /*初始化actionbar*/
    private void initActionBar() {
    /*得到actionbar*/
        ActionBar actionBar = getSupportActionBar();
        /*去掉actionbar底部的阴影线*/
        actionBar.setElevation(0);
        /*设置标题*/
        actionBar.setTitle(R.string.title);
        /*设置ActionBar可见，并且切换菜单和内容视图*/
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        System.out.println("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.action_menu_collec, menu);
        MenuItem item = menu.findItem(R.id.action_collec);
        /*每次创建都要判断是否已经收藏*/
        if (mDao.query(mId)) {
            item.setIcon(R.drawable.actionbar_collec_on);
        } else {
            item.setIcon(R.drawable.actionbar_collec_off);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_collec:
                CollectionBean collectionBean = new CollectionBean();
                collectionBean.author=mAuthor;
                collectionBean.url=mUrl;
                collectionBean.from=mFrom;
                collectionBean.date=mDate;
                collectionBean.imgUrl=mImgUrl;
                collectionBean.imgUrl1=mImgUrl1;
                collectionBean.imgUrl2=mImgUrl2;
                collectionBean.imgUrl3=mImgUrl3;
                collectionBean.title=mTitle;
                collectionBean.type=mType;
                collectionBean.id=mId;

                /*已收藏的不再收藏,并从数据库中清除*/
                /*没收藏的添加收藏,并添加到数据库中*/
                if (mDao.query(collectionBean.id)) {
                    //删除数据库中该条数据
                    mDao.delete(collectionBean.id);
                    item.setIcon(R.drawable.actionbar_collec_off);//取消收藏
                } else {
                    mDao.insert(collectionBean);
                    item.setIcon(R.drawable.actionbar_collec_on);//添加收藏
                }

                /*测试*/
                mDao.queryAll();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
