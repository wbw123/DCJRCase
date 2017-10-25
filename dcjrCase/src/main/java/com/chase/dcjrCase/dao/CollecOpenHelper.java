package com.chase.dcjrCase.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chase on 2017/10/25.
 */

public class CollecOpenHelper extends SQLiteOpenHelper {
    public CollecOpenHelper(Context context) {
        super(context, CollecConstacts.DB_NAME, null, CollecConstacts.VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//创建表
        db.execSQL(CollecConstacts.CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
