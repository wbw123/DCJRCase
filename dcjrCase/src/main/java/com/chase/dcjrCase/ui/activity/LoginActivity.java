package com.chase.dcjrCase.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.view.CleanEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_skip:
                    skipLogin();
                break;
            case R.id.btn_login:
                break;
            case R.id.iv_qq :
                break;
            case R.id.iv_wechat:
                break;
            case R.id.iv_sina:
                break;
            case R.id.tv_create_account:
                break;
            case R.id.tv_forget_password:
                break;
            default:
                break;
        }
    }
    //跳过登录页
    private void skipLogin() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
