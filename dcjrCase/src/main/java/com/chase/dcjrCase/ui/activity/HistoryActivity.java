package com.chase.dcjrCase.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.HistoryBean;

import java.util.ArrayList;


public class HistoryActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<HistoryBean> historyBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mListView = (ListView) findViewById(R.id.list_history);



    }
}
