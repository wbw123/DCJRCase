package com.chase.dcjrCase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.CaseSearchAdapter;
import com.chase.dcjrCase.bean.CaseData;
import com.chase.dcjrCase.bean.CaseData.DataBean.CaseDataBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.uitl.CacheUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CaseSearchActivity extends AppCompatActivity {

    private ArrayList<CaseDataBean> mCaseList;
    private CaseSearchAdapter mCaseSearchAdapter;
    private ListView mLlCaseSearch;
    private Button mBtnCaseSearch;
    private EditText mEtCaseSearch;
    private ArrayList<CaseDataBean> mCaseSearchedData;
    private String caseJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_search);

        mLlCaseSearch = (ListView) findViewById(R.id.ll_case_search);
        mBtnCaseSearch = (Button) findViewById(R.id.btn_case_search);
        mEtCaseSearch = (EditText) findViewById(R.id.et_case_search);

        caseJson = CacheUtils.getCache(Constants.CASEJSON_URL, this);

        processResult(caseJson);

        mBtnCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCaseSearchedData = new ArrayList<>();
                String text = mEtCaseSearch.getText().toString();
                System.out.println("text:" + text);
                for (CaseDataBean caseDataBean : mCaseList) {
                    if (text.isEmpty()) {
                        Toast.makeText(CaseSearchActivity.this, "请输入搜索内容!", Toast.LENGTH_SHORT).show();
                    } else if (caseDataBean.title.contains(text)) {
                        mCaseSearchedData.add(caseDataBean);
                    } else {
                        Toast.makeText(CaseSearchActivity.this, "您搜索的内容不存在!", Toast.LENGTH_SHORT).show();
                    }
                }
                mCaseSearchAdapter = new CaseSearchAdapter(CaseSearchActivity.this, mCaseSearchedData);
                mLlCaseSearch.setAdapter(mCaseSearchAdapter);

            }
        });
        mLlCaseSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CaseDataBean caseDataBean = mCaseSearchedData.get(position);
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", Constants.HOME_URL + caseDataBean.url);//webView链接
                startActivity(intent);
            }
        });

    }

    /**
     * 解析json数据
     */
    private void processResult(String caseJson) {
        Gson gson = new Gson();
        CaseData caseData = gson.fromJson(caseJson, CaseData.class);
        System.out.println("mCaseData解析结果:" + caseData.toString());

        mCaseList = caseData.data.caseData;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaseSearchAdapter = null;
        mCaseSearchedData.clear();
        mCaseList.clear();
    }
}
