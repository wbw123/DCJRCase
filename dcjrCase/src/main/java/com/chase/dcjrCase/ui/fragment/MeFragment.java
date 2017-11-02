package com.chase.dcjrCase.ui.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatDelegate;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.ui.activity.CollectionActivity;
import com.chase.dcjrCase.ui.activity.HistoryActivity;
import com.chase.dcjrCase.ui.activity.LoginActivity;
import com.chase.dcjrCase.ui.activity.MainActivity;
import com.chase.dcjrCase.ui.activity.SwitchModeActivity;

/**
 * Created by chase on 2017/9/5.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_history;
    private LinearLayout ll_collection;
    private LinearLayout ll_night;
    private LinearLayout ll_clean;
    private LinearLayout ll_feedback;
    private LinearLayout ll_setting;
    private ImageView iv_login;
    private ImageView iv_nightImg;
    private TextView tv_nightText;
    private boolean isNightMode = false;


    @Override
    public View initView() {
     View view = View.inflate(mActivity, R.layout.fragment_me,null);
        ll_night= view.findViewById(R.id.ll_night_mode);
        iv_nightImg = view.findViewById(R.id.iv_night_img);
        tv_nightText = view.findViewById(R.id.tv_night_text);
        ll_history = view.findViewById(R.id.ll_history);
        ll_collection = view.findViewById(R.id.ll_collection);
        ll_clean = view.findViewById(R.id.ll_clean);
        ll_feedback = view.findViewById(R.id.ll_feedback);
        ll_setting = view.findViewById(R.id.ll_setting);
        iv_login = view.findViewById(R.id.iv_me_login);

        ll_night.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        ll_collection.setOnClickListener(this);
        ll_clean.setOnClickListener(this);
        ll_feedback.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        iv_login.setOnClickListener(this);
        return view;
    }
    @Override
    public void initData() {
        System.out.println("mefragment加载了");
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_history:
                getHistory();
                break;
            case R.id.ll_collection:
                getCollection();
                break;
            case R.id.ll_night_mode:
                Intent intent = new Intent(mActivity, SwitchModeActivity.class);
                mActivity.startActivity(intent);
                switchDayNightMode();
                getActivity().overridePendingTransition(R.anim.activity_in,R.anim
                .activity_out);//淡出淡入动画效果
                break;
            case R.id.ll_clean:
                cleanCache();
                break;
            case R.id.ll_feedback:
                break;
            case R.id.ll_setting:
                break;
            case R.id.iv_me_login:
                System.out.println("login");
                Intent loginIntent = new Intent(mActivity, LoginActivity.class);
                mActivity.startActivity(loginIntent);
                break;
        }
    }

    private void cleanCache() {
        String pkName = getContext().getPackageName();
        Intent intent = new Intent(
                "android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + pkName));
        startActivity(intent);
    }

    public void getCollection() {
        Intent intent = new Intent(mActivity, CollectionActivity.class);
        mActivity.startActivity(intent);
    }
    private void getHistory() {
        Intent intent = new Intent(mActivity, HistoryActivity.class);
        mActivity.startActivity(intent);
    }

    private void switchDayNightMode() {
        int mode = getActivity().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        if (mode == Configuration.UI_MODE_NIGHT_NO) {
//            iv_nightImg.setImageResource(R.drawable.ic_day_nor);
//            tv_nightText.setText(R.string.day_name);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }else if (mode == Configuration.UI_MODE_NIGHT_YES){
//            iv_nightImg.setImageResource(R.drawable.ic_night_pre);
//            tv_nightText.setText(R.string.night_name);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
//
        switch (mode){
            case Configuration.UI_MODE_NIGHT_NO:

                tv_nightText.setTextColor(Color.argb(136,0,0,0));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                getActivity().recreate();
                break;
            case Configuration.UI_MODE_NIGHT_YES:

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                getActivity().recreate();
                break;

        }
    }


}
