package com.chase.dcjrCase.ui.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chase.dcjrCase.R;



public class SwitchModeActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题,继承Activity时用
        getSupportActionBar().hide();//去除标题栏，继承AppCompatActivity时用
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_switch_mode);
        mImageView = (ImageView) findViewById(R.id.iv_switch_mode);
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (mode == Configuration.UI_MODE_NIGHT_YES){
            mImageView.setImageResource(R.drawable.ic_moon);
        }else {
            mImageView.setImageResource(R.drawable.ic_sun);
        }

        //设置两秒后执行当前activity的销毁操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                back(null);
            }
        },2000);
    }

    private void back(View view) {
        this.finish();
        try {
            overridePendingTransition(R.anim.activity_in,R.anim.activity_out);
        }catch (Exception e){
        }
    }
}
