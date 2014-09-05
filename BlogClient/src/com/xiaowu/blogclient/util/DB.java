package com.xiaowu.blogclient.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xiaowu.blogclient.model.BlogItem;

/**
 * 数据库操作类
 * 
 * @author wwj_748
 * @date 2014/8/9
 */
public class DB extends SQLiteOpenHelper {
	private static final String TAG = "BLOG DB";
	// 数据库名称常量
	private static final String DATABASE_NAME = "blog.db";
	// 数据库版本常量
	private static final int DATABASE_VERSION = 1;
	// 表名称常量
	public static final String TABLES_NAME = "blogTable";

	// 构造方法
	public DB(Context context) {
		// 创建数据库
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.e(TAG, "create database");
	}

	// 创建时调用，若数据库存在则不调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e(TAG, "oncreate DB");
		db.execSQL("CREATE TABLE " + TABLES_NAME + " (" + "_ID"
				+ " INTEGER PRIMARY KEY," + "title" + " TEXT," + "content"
				+ " TEXT," + "date" + " TEXT," + "img" + " TEXT," + "link"
				+ " TEXT," + "blogType" + " INTEGER" + ");");
	}

	// 版本更新时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 删除表
		Log.e(TAG, "delete DB");
		db.execSQL("DROP TABLE IF EXISTS blogTable");
		onCreate(db);// 创建新表
	}

	/**
	 * 插入数据
	 * 
	 * @param list
	 */
	public void insert(List<BlogItem> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (BlogItem item : list) {
			ContentValues values = new ContentValues();
			values.put("title", item.getTitle());
			values.put("content", item.getContent());
			values.put("date", item.getDate());
			values.put("img", item.getImgLink());
			values.put("link", item.getLink());
			values.put("blogType", item.getType());

			db.insert("blogTable", null, values);
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param blogType
	 *            博客类型
	 */
	public void delete(int blogType) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from blogTable where blogType = '" + blogType + "'");
	}

	/**
	 * 查询数据
	 * 
	 * @param blogType
	 * @return
	 */
	public List<BlogItem> query(int blogType) {
		List<BlogItem> list = new ArrayList<BlogItem>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from blogTable where blogType = '" + blogType
				+ "'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				BlogItem item = new BlogItem();
				item.setTitle(cursor.getString(1));
				item.setContent(cursor.getString(2));
				item.setDate(cursor.getString(3));
				item.setImgLink(cursor.getString(4));
				item.setLink(cursor.getString(5));
				item.setType(blogType);

				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

}
