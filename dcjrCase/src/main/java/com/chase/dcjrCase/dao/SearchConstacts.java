package com.chase.dcjrCase.dao;

public interface SearchConstacts {
	String DB_NAME = "search.db";
	int VERSION = 1;
	String TABLE_NAME = "search";
	String SEARCH_HISTORY = "searchHistory";
	String CREATE_SQL = "create table " + TABLE_NAME
			+ " (_id integer primary key autoincrement, " + SEARCH_HISTORY
			+ " varchar(200));";
}