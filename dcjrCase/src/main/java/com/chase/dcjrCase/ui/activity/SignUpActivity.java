package com.chase.dcjrCase.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.view.CleanEditText;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "SignUpActivity";
    //界面控件
    private Button btn_getVerCode;
    private Button btn_createAccount;
    private CleanEditText et_phone;
    private CleanEditText et_password;
    private CleanEditText et_verCode;
    private CleanEditText et_nickName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        btn_getVerCode = (Button) findViewById(R.id.btn_send_ver_code);
        btn_getVerCode.setOnClickListener(this);
        btn_createAccount = (Button) findViewById(R.id.btn_create_account);
        btn_createAccount.setOnClickListener(this);
        et_phone = (CleanEditText) findViewById(R.id.et_phone);
        et_phone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_nickName = (CleanEditText) findViewById(R.id.et_nickname);
        et_nickName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_verCode = (CleanEditText) findViewById(R.id.et_verCode);
        et_verCode.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_password = (CleanEditText) findViewById(R.id.et_password);
        et_password.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_password.setImeOptions(EditorInfo.IME_ACTION_GO);
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //点击虚拟键盘的done
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO){

                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_ver_code:
                break;
            case R.id.btn_create_account:
                break;
            default:
                break;
        }
    }
}
