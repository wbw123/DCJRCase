package com.chase.dcjrCase.dao;

public interface CollecConstacts {
	String DB_NAME = "collection.db";
	int VERSION = 1;
	String TABLE_NAME = "collection";
	String ID = "id";
	String TITLE = "title";
	String DATE = "date";
	String FROM = "source";
	String AUTHOR = "author";
	String TYPE = "type";
	String URL = "url";
	String IMGURL = "imgUrl";
	String IMGURL1 = "imgUrl1";
	String IMGURL2 = "imgUrl2";
	String IMGURL3 = "imgUrl3";

	String CREATE_SQL = "create table " + TABLE_NAME
			+ " (_id integer primary key autoincrement, "
			+ ID + " varchar(200),"
			+ TITLE + " varchar(200),"
			+ DATE + " varchar(200),"
			+ FROM + " varchar(200),"
			+ AUTHOR + " varchar(200),"
			+ TYPE + " varchar(200),"
			+ URL + " varchar(200),"
			+ IMGURL + " varchar(200),"
			+ IMGURL1 + " varchar(200),"
			+ IMGURL2 + " varchar(200),"
			+ IMGURL3 + " varchar(200));";
}