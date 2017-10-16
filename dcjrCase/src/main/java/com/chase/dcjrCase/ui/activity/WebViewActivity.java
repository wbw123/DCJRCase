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

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressBar pbLoading;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        /*初始化actionbar*/
        initActionBar();

        mWebView = (WebView) findViewById(R.id.wv_webview);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);


        mUrl = getIntent().getStringExtra("url");

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


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_back, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
