package com.chase.dcjrCase.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chase.dcjrCase.bean.CollectionBean;

import java.util.ArrayList;
import java.util.Collections;

public class CollecDao {
    private static CollecOpenHelper collecOpenHelper;

    /*单例设计模式*/
    private static CollecDao instance = null;

    private CollecDao(Context context) {
        collecOpenHelper = new CollecOpenHelper(context);
    }

    public static CollecDao getInstance(Context context) {
        if (instance == null) {
            synchronized (CollecDao.class) {
                if (instance == null) {
                    instance = new CollecDao(context);
                }
            }
        }
        return instance;
    }

    // 数据库插入操作
    public boolean insert(CollectionBean collectionBean) {
        SQLiteDatabase database = collecOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CollecConstacts.ID, collectionBean.id);
        values.put(CollecConstacts.TITLE, collectionBean.title);
        values.put(CollecConstacts.DATE, collectionBean.date);
        values.put(CollecConstacts.FROM, collectionBean.from);
        values.put(CollecConstacts.AUTHOR, collectionBean.author);
        values.put(CollecConstacts.URL, collectionBean.url);
        values.put(CollecConstacts.TYPE, collectionBean.type);
        values.put(CollecConstacts.IMGURL, collectionBean.imgUrl);
        values.put(CollecConstacts.IMGURL1, collectionBean.imgUrl1);
        values.put(CollecConstacts.IMGURL2, collectionBean.imgUrl2);
        values.put(CollecConstacts.IMGURL3, collectionBean.imgUrl3);
        long insert = database
                .insert(CollecConstacts.TABLE_NAME, null, values);
        database.close();
        return insert != -1;
    }

    // 删除包含此id的数据
    public boolean delete(String id) {
        SQLiteDatabase database = collecOpenHelper.getWritableDatabase();
        String whereClause = CollecConstacts.ID + "= ?";
        String[] whereArgs = new String[]{id};
        // 参数一表名，查询语句，查询参数
        int delete = database.delete(CollecConstacts.TABLE_NAME, whereClause,
                whereArgs);

        database.close();
        return delete != 0;
    }
    // 删除所有数据
    /*public void delete() {
		SQLiteDatabase database = searchOpenHelper.getWritableDatabase();
		String whereClause = SearchConstacts.SEARCH_HISTORY + "= ?";
		database.delete(SearchConstacts.TABLE_NAME, null, null);
		database.close();
	}*/

    /**
     * 通过id判断是否已收藏
     *
     * @param id
     * @return
     */
    public boolean query(String id) {
        SQLiteDatabase database = collecOpenHelper.getWritableDatabase();
        String[] columns = new String[]{CollecConstacts.ID};
        String selection = CollecConstacts.ID + "= ?";
        String[] selectionArgs = new String[]{id};
        Cursor cursor = database.query(CollecConstacts.TABLE_NAME, columns,
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

    /**
     * 查询所有collection
     *
     * @return
     */
    public ArrayList<CollectionBean> queryAll() {
        SQLiteDatabase database = collecOpenHelper.getWritableDatabase();

        Cursor cursor = database.query(CollecConstacts.TABLE_NAME, null, null, null, null, null, null);

        ArrayList<CollectionBean> collecList = new ArrayList<>();
        while (cursor.moveToNext()) {

            CollectionBean collectionBean = new CollectionBean();
            collectionBean.id = cursor.getString(cursor.getColumnIndex(CollecConstacts.ID));
            collectionBean.title = cursor.getString(cursor.getColumnIndex(CollecConstacts.TITLE));
            collectionBean.from = cursor.getString(cursor.getColumnIndex(CollecConstacts.FROM));
            collectionBean.date = cursor.getString(cursor.getColumnIndex(CollecConstacts.DATE));
            collectionBean.author = cursor.getString(cursor.getColumnIndex(CollecConstacts.AUTHOR));
            collectionBean.url = cursor.getString(cursor.getColumnIndex(CollecConstacts.URL));
            collectionBean.type = cursor.getString(cursor.getColumnIndex(CollecConstacts.TYPE));
            collectionBean.imgUrl = cursor.getString(cursor.getColumnIndex(CollecConstacts.IMGURL));
            collectionBean.imgUrl1 = cursor.getString(cursor.getColumnIndex(CollecConstacts.IMGURL1));
            collectionBean.imgUrl2 = cursor.getString(cursor.getColumnIndex(CollecConstacts.IMGURL2));
            collectionBean.imgUrl3 = cursor.getString(cursor.getColumnIndex(CollecConstacts.IMGURL3));

            collecList.add(collectionBean);
            System.out.println("collecList: " + collecList);
        }
        Collections.reverse(collecList);
        cursor.close();
        database.close();
        return collecList;
    }
}