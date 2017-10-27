package com.chase.dcjrCase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.CollectionAdapter;
import com.chase.dcjrCase.bean.CollectionBean;
import com.chase.dcjrCase.dao.CollecDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private CollecDao mDao;
    private CollectionAdapter mAdapter;
    private List<HashMap<String, Object>> mHashMapList;//存储CollectionBean和Boolean,即将bean和对应的checkbox绑定操作
    private ArrayList<CollectionBean> mCollectionList;//存储收藏的集合
    private ArrayList<CollectionBean> mCheckedList;//存储被选中删除的集合
    private static boolean isCheckBoxVisible = false;//是否显示checkbox
    private boolean isAllChecked = false;//是否全选
    private ListView mListView;
    private RelativeLayout mRlBottom;//底部未显示布局
    private Button mBtnDelete;
    private CheckBox mCbALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mListView = (ListView) findViewById(R.id.list_collction);
        mRlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        mBtnDelete = (Button) findViewById(R.id.btn_delete);
        mCbALL = (CheckBox) findViewById(R.id.cb_all);

        mBtnDelete.setOnClickListener(this);
        mCbALL.setOnClickListener(this);


    }

    /**
     * 在onResume中操作是为了防止点击收藏item
     * 进入webview后,取消收藏,退出webview
     * 回到收藏界面导致listview没有及时刷新的问题
     */
    @Override
    protected void onResume() {
        super.onResume();
        mCheckedList = new ArrayList<>();//存储被选中删除的集合

        mHashMapList = new ArrayList<HashMap<String, Object>>();
        mDao = CollecDao.getInstance(this);
        mCollectionList = mDao.queryAll();

        /*item绑定checkBox*/
        for (int i = 0; i < mCollectionList.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("list", mCollectionList.get(i));
            map.put("isChecked", false);
            mHashMapList.add(map);
        }

        mAdapter = new CollectionAdapter(this, mHashMapList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("on long item click");
                isCheckBoxVisible = true;//用来在adapter中刷新显示checkBox
                mRlBottom.setVisibility(View.VISIBLE);//显示底部布局
                invalidateOptionsMenu();//更新actonbar的menu,会走onCreateOptionsMenu和onPrepareOptionsMenu
                mHashMapList.get(position).put("isChecked", true);
                CollectionBean collectionBean = (CollectionBean) mHashMapList.get(position).get("list");
                mCheckedList.add(collectionBean);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("onItemClick position:" + position);
                if (isCheckBoxVisible) {
                    CheckBox checkBox = view.findViewById(R.id.cb_check);
                    checkBox.toggle();
                    mHashMapList.get(position).put("isChecked", checkBox.isChecked());
                    mAdapter.notifyDataSetChanged();

                    CollectionBean collectionBean = (CollectionBean) mHashMapList.get(position).get("list");
                    if (checkBox.isChecked() && !mCheckedList.contains(collectionBean)) {
                        mCheckedList.add(collectionBean);
                    } else if (!checkBox.isChecked() && mCheckedList.contains(collectionBean)) {
                        mCheckedList.remove(collectionBean);
                    }

                    System.out.println("mCheckedList:" + mCheckedList);
                    System.out.println("mDao.queryAll():" + mDao.queryAll());
                    System.out.println("mCheckedList.containsAll(mDao.queryAll()):" + mCheckedList.containsAll(mDao.queryAll()));
                    if (mCheckedList.isEmpty()) {
                        mCbALL.setChecked(false);
                        isAllChecked = false;
                    } else if (mCheckedList.containsAll(mDao.queryAll())) {//重写CollectionBean的equals方法
                        mCbALL.setChecked(true);
                        isAllChecked = true;
                    }


                } else {
                    /*条目跳转*/
                    CollectionBean collectionBean = mCollectionList.get(position);
                    Intent intent = new Intent(CollectionActivity.this, WebViewActivity.class);
                    intent.putExtra("url", collectionBean.url);//webView链接
                    /*收藏*/
                    intent.putExtra("title", collectionBean.title);
                    intent.putExtra("author", collectionBean.author);
                    intent.putExtra("date", collectionBean.date);
                    intent.putExtra("imgUrl", collectionBean.imgUrl);
                    intent.putExtra("from", collectionBean.from);
                    intent.putExtra("type", collectionBean.type);
                    intent.putExtra("id", collectionBean.id);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                for (CollectionBean collection : mCheckedList) {
                    mDao.delete(collection.id);
                }
                mCollectionList = mDao.queryAll();
                    /*清空之前的数据*/
                mHashMapList.clear();
//                    mHashMapList = null;
//                    mHashMapList = new ArrayList<HashMap<String, Object>>();
                   /*item绑定checkBox*/
                for (int i = 0; i < mCollectionList.size(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("list", mCollectionList.get(i));
                    map.put("isChecked", false);
                    mHashMapList.add(map);
                }
                    /*集合发声改变,重新绑定适配器*/
                mAdapter = null;
                mAdapter = new CollectionAdapter(this, mHashMapList);
                mListView.setAdapter(mAdapter);
//                    mAdapter.notifyDataSetChanged();

                break;
            case R.id.cb_all:
                mCollectionList = mDao.queryAll();
                mHashMapList.clear();
                if (!isAllChecked) {
                /*item绑定checkBox*/
                    for (int i = 0; i < mCollectionList.size(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("list", mCollectionList.get(i));
                        map.put("isChecked", true);
                        mHashMapList.add(map);
                    }

                    mAdapter.notifyDataSetChanged();
                    mCheckedList.clear();
                    mCheckedList.addAll(mCollectionList);
                    isAllChecked = true;
                    mCbALL.setChecked(true);
                } else {
                    /*item绑定checkBox*/
                    for (int i = 0; i < mCollectionList.size(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("list", mCollectionList.get(i));
                        map.put("isChecked", false);
                        mHashMapList.add(map);
                    }
                    mAdapter.notifyDataSetChanged();
                    mCheckedList.clear();
                    isAllChecked = false;
                    mCbALL.setChecked(false);
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        System.out.println("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.action_menu_edit, menu);
        MenuItem item = menu.findItem(R.id.action_edit);
        /*创建时的操作*/
        if (isCheckBoxVisible) {
            item.setTitle("取消");
        } else {
            item.setTitle("编辑");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                /*点击时的操作*/
                if (isCheckBoxVisible) {
                    mCollectionList = mDao.queryAll();
                    mHashMapList.clear();
                    /*所有checkbox置false*/
                    for (int i = 0; i < mCollectionList.size(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("list", mCollectionList.get(i));
                        map.put("isChecked", false);
                        mHashMapList.add(map);
                    }
                    mCheckedList.clear();
                    isAllChecked = false;
                    mCbALL.setChecked(false);

                    item.setTitle("编辑");
                    mRlBottom.setVisibility(View.GONE);//不显示底部布局
                    isCheckBoxVisible = false;
                } else {
                    item.setTitle("取消");
                    mRlBottom.setVisibility(View.VISIBLE);//显示底部布局
                    isCheckBoxVisible = true;
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCheckBoxVisible = false;//退出不再显示checkbox
    }

    /*判断是否显示checkbox的方法*/
    public static boolean isVisible() {
        return isCheckBoxVisible;
    }

}
