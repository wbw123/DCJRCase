package com.chase.dcjrCase.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.HistoryListAdapter;
import com.chase.dcjrCase.bean.HistoryBean;
import com.chase.dcjrCase.dao.HistoryDao;
import com.chase.dcjrCase.global.Constants;

import java.util.ArrayList;


public class HistoryActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<HistoryBean> mHistoryBeanList;
    private HistoryBean mHistoryBean;
    private HistoryDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mListView = (ListView) findViewById(R.id.list_history);
        mDao = HistoryDao.getInstance(this);
        mHistoryBeanList = mDao.querySqlHistory();
        HistoryListAdapter historyListAdapter = new HistoryListAdapter(mHistoryBeanList,this);
        mListView.setAdapter(historyListAdapter);
        System.out.println("______________________history____________"+mHistoryBeanList);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("点击了历史记录+position:"+position);
                HistoryBean historyBean = mHistoryBeanList.get(position);
                System.out.println("11111111111111111111111"+historyBean.url);
                Intent intent = new Intent(HistoryActivity.this, WebViewActivity.class);
                intent.putExtra("url",Constants.HOME_URL+historyBean.url);//webView链接
                /*收藏*/
                intent.putExtra("title", historyBean.title);
                intent.putExtra("author", historyBean.author);
                intent.putExtra("date", historyBean.date);
                intent.putExtra("imgUrl", historyBean.imgUrl);
                intent.putExtra("from", historyBean.from);
                intent.putExtra("type", historyBean.type);
                intent.putExtra("id", historyBean.id);
                startActivity(intent);
            }
        });
        System.out.println("11111111111111111111111"+mHistoryBean);
    }

}
