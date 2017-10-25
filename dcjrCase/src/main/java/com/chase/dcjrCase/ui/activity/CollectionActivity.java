package com.chase.dcjrCase.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.CollectionAdapter;
import com.chase.dcjrCase.bean.CollectionBean;
import com.chase.dcjrCase.dao.CollecDao;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {
    private ListView mListView;
    private CollecDao mDao;
    private CollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mListView = (ListView) findViewById(R.id.list_collction);
        mDao = CollecDao.getInstance(this);
        ArrayList<CollectionBean> collectionList = mDao.queryAll();

        mAdapter = new CollectionAdapter(this, collectionList);
        mListView.setAdapter(mAdapter);
    }
}
