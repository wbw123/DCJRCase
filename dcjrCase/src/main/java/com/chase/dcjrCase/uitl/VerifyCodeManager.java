package com.chase.dcjrCase.uitl;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerifyCodeManager extends AppCompatActivity {
    public final static int REGISTER = 1;
    public final static int RESET_PWD = 2;
    public final static int BIND_PHONE = 3;
    private Context mContext;
    private EditText phoneEdit;
    private Button getVerificationCodeButton;
    private String phoneNum;

    public VerifyCodeManager(Context context, EditText editText, Button button) {
        this.mContext = context;
        this.phoneEdit = editText;
        this.getVerificationCodeButton = button;
    }
    public void getVerifyCode(int type){
        //获取验证码之前先判断手机号
        phoneNum = phoneEdit.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNum)){
            Toast.makeText(mContext,"请输入手机号码",Toast.LENGTH_SHORT).show();
        }else if (phoneNum.length()<11){
            Toast.makeText(mContext,"手机号码格式不正确",Toast.LENGTH_SHORT).show();
        }else if (!RegexUtils.checkMobile(phoneNum)){
            Toast.makeText(mContext,"手机号码格式不正确",Toast.LENGTH_SHORT).show();
        }
    }
}
