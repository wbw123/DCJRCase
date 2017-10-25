package com.chase.dcjrCase.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.CaseSearchAdapter;
import com.chase.dcjrCase.bean.CaseData;
import com.chase.dcjrCase.bean.CaseData.DataBean.CaseDataBean;
import com.chase.dcjrCase.dao.SearchDao;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.uitl.CacheUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CaseSearchActivity extends AppCompatActivity {

    private ArrayList<CaseDataBean> mCaseList;
    private CaseSearchAdapter mCaseSearchAdapter;
    private ListView mLlCaseSearch;
    private ArrayList<CaseDataBean> mCaseSearchedData;
    private String caseJson;
    private LinearLayout mLlListView;
    private SearchView mSearchView;
    private ArrayList<String> mStringList;//用来存储历史记录的结合
    private ListView mListView;//用来展示历史记录的view
    private String[] mStrings;//用来存储历史记录的数组
    private SearchDao mDao;
    private ArrayAdapter<String> adapter;//搜索历史记录adapter
    private TextView mTvDelete;//删除历史记录按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_search);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        mLlListView = (LinearLayout) findViewById(R.id.ll_listview);
        mLlCaseSearch = (ListView) findViewById(R.id.ll_case_search);
        mTvDelete = (TextView) findViewById(R.id.tv_delete_history);
        mListView = (ListView) findViewById(R.id.listview);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(true);
//        mSearchView.onActionViewExpanded();
//        mSearchView.setBackgroundColor(0x22ff00ff);
        caseJson = CacheUtils.getCache(Constants.CASEJSON_URL, this);//获取缓存的json
        processResult(caseJson);//解析json

        mDao = SearchDao.getInstance(this);//获取dao用于操作数据库
        /*初始化*/
        mStrings = mDao.queryAll();
        initData(mStrings);

        mStringList = new ArrayList<>();

        // 开启过滤功能  
        mListView.setTextFilterEnabled(true);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                System.out.println("query:" + query);
                bindSearchAdapter(query);

                if (mSearchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    mSearchView.clearFocus(); // 不获取焦点
                }
                mStringList.add(query);//添加到历史记录
                for (int i = 0; i < mStringList.size(); i++) {
                    if (!mDao.query(mStringList.get(i))) {
                        mDao.insert(mStringList.get(i));
                    }
                }
                mStrings = mDao.queryAll();
                initData(mStrings);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mLlListView.setVisibility(View.VISIBLE);
                System.out.println("newText:" + newText);
                if (!TextUtils.isEmpty(newText)) {
//                    mListView.setFilterText(newText);
                    adapter.getFilter().filter(newText);//去除悬浮窗
                } else {
                    mListView.clearTextFilter();
                    mLlListView.setVisibility(View.INVISIBLE);
                }

                return true;
            }
        });

        /*搜索到的内容条目点击事件*/
        mLlCaseSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CaseDataBean caseDataBean = mCaseSearchedData.get(position);
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", Constants.HOME_URL + caseDataBean.url);//webView链接
                startActivity(intent);
            }
        });
        /*搜索历史记录条目点击事件*/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemname = ((TextView)view).getText().toString();
//                bindSearchAdapter(itemname);
                System.out.println("itemname:"+itemname);
                mSearchView.setQuery(itemname,true);//将内容显示到searchview上并submit
            }
        });


        /*清空搜索历史记录*/
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDao.delete();//清空数据库
                mStringList.clear();//清空集合
                /*重新查询数据库,并绑定adapter*/
                mStrings = mDao.queryAll();
                initData(mStrings);
            }
        });
    }

    /**
     * 绑定数据到adapter
     * @param query
     */
    private void bindSearchAdapter(String query) {
        mCaseSearchedData = new ArrayList<>();
        for (CaseDataBean caseDataBean : mCaseList) {
            if (TextUtils.isEmpty(query)) {
                Toast.makeText(CaseSearchActivity.this, "请输入搜索内容!", Toast.LENGTH_SHORT).show();
            } else if (caseDataBean.title.contains(query)) {
                mCaseSearchedData.add(caseDataBean);
            }
        }
        if (mCaseSearchedData.isEmpty()) {
            Toast.makeText(CaseSearchActivity.this, "您搜索的内容不存在!", Toast.LENGTH_SHORT).show();
        }
        mCaseSearchAdapter = new CaseSearchAdapter(CaseSearchActivity.this, mCaseSearchedData);
        mLlCaseSearch.setAdapter(mCaseSearchAdapter);

        mLlListView.setVisibility(View.GONE);//隐藏历史记录
    }

    /**
     * 绑定历史记录
     * @param mStrings
     */
    private void initData(String[] mStrings) {

        adapter = new ArrayAdapter<>(this, R.layout.item_search_history, mStrings);
        mListView.setAdapter(adapter);
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
}
