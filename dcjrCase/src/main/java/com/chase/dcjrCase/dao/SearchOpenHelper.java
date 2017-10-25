package com.chase.dcjrCase.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chase on 2017/10/25.
 */

public class SearchOpenHelper extends SQLiteOpenHelper {
    public SearchOpenHelper(Context context) {
        super(context, SearchConstacts.DB_NAME, null, SearchConstacts.VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//创建表
        db.execSQL(SearchConstacts.CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
