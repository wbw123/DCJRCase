package com.chase.dcjrCase.ui.activity;

/**
 * Created by JimCharles on 2016/12/22.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.uitl.PrefUtils;
import com.chase.dcjrCase.view.WowSplashView;
import com.chase.dcjrCase.view.WowView;


import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private WowSplashView mWowSplashView;
    private WowView mWowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashs);
        initsView();


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //判断是否需要跳转到新手引导页
                //默认是false--没有跳转到新手引导
                boolean isGuideShow= PrefUtils.getBoolean("is_guide_show",false,getApplicationContext());
                if (isGuideShow){
                    //动画结束后跳转到主页面
                    final Intent it = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(it);
                    //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else {
                    //跳转到新手引导
                    final Intent it = new Intent(getApplicationContext(),GuideActivity.class);
                    startActivity(it);
                    //startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                }
                //finish();
             //  startActivity(it); //执行
            }
        };
        timer.schedule(task, 1000*3); //6秒后
    }

    private void initsView(){
        mWowSplashView = (WowSplashView) findViewById(R.id.wowSplash);
        mWowView = (WowView) findViewById(R.id.wowView);

        mWowSplashView.startAnimate();

        mWowSplashView.setOnEndListener(new WowSplashView.OnEndListener() {
            @Override
            public void onEnd(WowSplashView wowSplashView) {
                mWowSplashView.setVisibility(View.GONE);
//                mWowView.setVisibility(View.VISIBLE);
//                mWowView.startAnimate(wowSplashView.getDrawingCache());

               /* //判断是否需要跳转到新手引导页
                //默认是false--没有跳转到新手引导
                boolean isGuideShow= PrefUtils.getBoolean("is_guide_show",false,getApplicationContext());
                if (isGuideShow){
                    //动画结束后跳转到主页面

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else {
                    //跳转到新手引导
                    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                }
                finish();*/
            }
        });
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
