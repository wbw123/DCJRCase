package com.chase.dcjrCase.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chase.dcjrCase.bean.HistoryBean;
import com.chase.dcjrCase.bean.TechData.DataBean.TechDataBean;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ZYK on 2017/10/23.
 */

public class HistoryDao {

    private static HistoryOpenHelper mHistoryOpenHelper;
    private static SQLiteDatabase database;


    /*单例设计模式*/
    private static HistoryDao instance = null;
    private Cursor cursor;
    private long insert;

    public HistoryDao(Context context) {
        mHistoryOpenHelper = new HistoryOpenHelper(context);
    }
    public static HistoryDao getInstance(Context context) {
        if (instance == null) {
            synchronized (HistoryDao.class) {
                if (instance == null) {
                    instance = new HistoryDao(context);
                }
            }
        }
        return instance;

    }
    //数据库插入操作
    public boolean insert(HistoryBean historyBean) {
        database = mHistoryOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(HistoryConstants.ID,historyBean.id);
        values.put(HistoryConstants.TITLE,historyBean.title);
        values.put(HistoryConstants.DATE,historyBean.date);
        values.put(HistoryConstants.FROM,historyBean.from);
        values.put(HistoryConstants.IMGURL,historyBean.imgUrl);
        values.put(HistoryConstants.URL,historyBean.url);
        values.put(HistoryConstants.TYPE,historyBean.id);
        values.put(HistoryConstants.IMGURL1, historyBean.imgUrl1);
        values.put(HistoryConstants.IMGURL2, historyBean.imgUrl2);
        values.put(HistoryConstants.IMGURL3, historyBean.imgUrl3);
        insert = database.insert(HistoryConstants.TABLE_NAME, null, values);
        return insert != -1;
    }
    //通过ID判断是否已经添加到数据库
    public boolean query(String id) {
        System.out.println("query:已经添加到数据库");
        SQLiteDatabase database = mHistoryOpenHelper.getWritableDatabase();
        String[] columns = new String[] { HistoryConstants.ID };
        String selection = HistoryConstants.ID + "= ?";
        String[] selectionArgs = new String[] { id };
        Cursor cursor = database.query(HistoryConstants.TABLE_NAME, columns,
                selection, selectionArgs, null, null, null);
        boolean result = false;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                result = true;
            }
        }
        cursor.close();
        database.close();
        return result;
    }

    // 删除包含此id的数据
    public boolean deleteID(String id) {
        SQLiteDatabase database = mHistoryOpenHelper.getWritableDatabase();
        String whereClause = HistoryConstants.ID + "= ?";
        String[] whereArgs = new String[] { id };
        // 参数一表名，查询语句，查询参数
        int delete = database.delete(HistoryConstants.TABLE_NAME, whereClause,
                whereArgs);

        database.close();
        return delete != 0;
    }

    // 删除所有数据
	public void delete() {
		SQLiteDatabase database = mHistoryOpenHelper.getWritableDatabase();
		database.delete(HistoryConstants.TABLE_NAME, null, null);
		database.close();
	}

    //查询数据库所有数据
    public ArrayList<HistoryBean> querySqlHistory(){
        database = mHistoryOpenHelper.getReadableDatabase();
        ArrayList<HistoryBean> historyList = new ArrayList<>();
        //扫描数据库，将数据库信息放入集合中
        cursor = database.query(HistoryConstants.TABLE_NAME, null,null , null, null, null, null);

        while (cursor.moveToNext()) {
            HistoryBean mHistoryBean = new HistoryBean();
            mHistoryBean.id = cursor.getString(cursor.getColumnIndex(HistoryConstants.ID));
            mHistoryBean.title = cursor.getString(cursor.getColumnIndex(HistoryConstants.TITLE));
            mHistoryBean.date = cursor.getString(cursor.getColumnIndex(HistoryConstants.DATE));
            mHistoryBean.imgUrl = cursor.getString(cursor.getColumnIndex(HistoryConstants.IMGURL));
            mHistoryBean.imgUrl1 = cursor.getString(cursor.getColumnIndex(HistoryConstants.IMGURL1));
            mHistoryBean.imgUrl2 = cursor.getString(cursor.getColumnIndex(HistoryConstants.IMGURL2));
            mHistoryBean.imgUrl3 = cursor.getString(cursor.getColumnIndex(HistoryConstants.IMGURL3));
            mHistoryBean.url = cursor.getString(cursor.getColumnIndex(HistoryConstants.URL));
            mHistoryBean.type = cursor.getString(cursor.getColumnIndex(HistoryConstants.TYPE));
            mHistoryBean.from = cursor.getString(cursor.getColumnIndex(HistoryConstants.FROM));
            mHistoryBean.author = cursor.getString(cursor.getColumnIndex(HistoryConstants.AUTHOR));
            historyList.add(mHistoryBean);
        }
        Collections.reverse(historyList);
        cursor.close();
        database.close();
        return historyList;
    }
}
