package com.chase.dcjrCase.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class SearchDao {
	private static SearchOpenHelper searchOpenHelper;

	/*单例设计模式*/
	private static SearchDao instance = null;
	private SearchDao(Context context) {
		searchOpenHelper = new SearchOpenHelper(context);
	}
	public static SearchDao getInstance(Context context) {
		if (instance == null) {
			synchronized (SearchDao.class) {
				if (instance == null) {
					instance = new SearchDao(context);
				}
			}
		}
		return instance;
	}

	// 数据库插入操作
	public boolean insert(String searchHistory) {
		SQLiteDatabase database = searchOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SearchConstacts.SEARCH_HISTORY, searchHistory);
		long insert = database
				.insert(SearchConstacts.TABLE_NAME, null, values);
		database.close();
		return insert != -1;
	}

	// 删除操作
	public boolean delete(String searchHistory) {
		SQLiteDatabase database = searchOpenHelper.getWritableDatabase();
		String whereClause = SearchConstacts.SEARCH_HISTORY + "= ?";
		String[] whereArgs = new String[] { searchHistory };
		// 参数一表名，查询语句，查询参数
		int delete = database.delete(SearchConstacts.TABLE_NAME, whereClause,
				whereArgs);

		database.close();
		return delete != 0;
	}
	// 删除所有数据
	public void delete() {
		SQLiteDatabase database = searchOpenHelper.getWritableDatabase();
		String whereClause = SearchConstacts.SEARCH_HISTORY + "= ?";
		database.delete(SearchConstacts.TABLE_NAME, null, null);
		database.close();
	}

	// 查询是否包含此条searchHistory
	public boolean query(String searchHistory) {
		SQLiteDatabase database = searchOpenHelper.getWritableDatabase();
		String[] columns = new String[] { SearchConstacts.SEARCH_HISTORY };
		String selection = SearchConstacts.SEARCH_HISTORY + "= ?";
		String[] selectionArgs = new String[] { searchHistory };
		Cursor cursor = database.query(SearchConstacts.TABLE_NAME, columns,
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
	 * 查询所有history
	 * @return
     */
	public String[] queryAll() {
		SQLiteDatabase database = searchOpenHelper.getWritableDatabase();

		Cursor cursor = database.query(SearchConstacts.TABLE_NAME, null,null , null, null, null, null);

		ArrayList<String> history = new ArrayList<>();
		String[] strings;
		while(cursor.moveToNext()){
			String searchHistory = cursor.getString(cursor.getColumnIndex(SearchConstacts.SEARCH_HISTORY));
			history.add(searchHistory);
			System.out.println("searchHistory: " + searchHistory);
		}
		Collections.reverse(history);
		strings = new String[history.size()];
		for (int i = 0; i < history.size();i++) {
			strings[i] = history.get(i);
		}
		cursor.close();
		database.close();
		return strings;
	}
}