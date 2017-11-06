package com.chase.dcjrCase.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ZYK on 2017/10/23.
 */

public class HistoryOpenHelper extends SQLiteOpenHelper {

    public HistoryOpenHelper(Context context) {
        super(context, HistoryConstants.DB_NAME, null, HistoryConstants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(HistoryConstants.CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
