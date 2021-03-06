package com.chase.dcjrCase.ui.activity;

/**
 * Created by JimCharles on 2016/12/22.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.view.WowSplashView;
import com.chase.dcjrCase.view.WowView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private WowSplashView mWowSplashView;
    private WowView mWowView;
    private boolean isPause= false;//用于判断闪屏动画执行过程中,退出应用时,不再跳转activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashs);
        initsView();


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isPause){
                    finish();
                }else {
                    //动画结束后跳转到主页面
                    final Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(it);
                }
            }
        };
        timer.schedule(task, 1000 * 4); //4秒后
    }

    private void initsView() {
        mWowSplashView = (WowSplashView) findViewById(R.id.wowSplash);
        mWowView = (WowView) findViewById(R.id.wowView);

        mWowSplashView.startAnimate();


        mWowSplashView.setOnEndListener(new WowSplashView.OnEndListener() {
            @Override
            public void onEnd(WowSplashView wowSplashView) {
                mWowSplashView.setVisibility(View.GONE);
//              mWowView.setVisibility(View.VISIBLE);
//              mWowView.startAnimate(wowSplashView.getDrawingCache());
            }
        });
    }

    @Override
    protected void onPause() {
        isPause = true;
        System.out.println("SplashActivity onPause");
        finish();
        super.onPause();
    }
}
