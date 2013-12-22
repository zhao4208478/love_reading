package com.example.love_reading.sql;

import java.io.ByteArrayOutputStream;

import com.example.love_reading.BookInfo;
import com.example.love_reading.MainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
/**
 * 实现对表的创建、更新、变更列名操作
 *
 */
public class SQLiteHelper2 extends SQLiteOpenHelper {
	private String tag = SQLiteHelper2.this.getClass().getSimpleName();
	public static final String TB_NAME = "bookplan";

	public SQLiteHelper2(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	/**
	 * 创建新表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder(); 
        sql.append("CREATE TABLE IF NOT EXISTS ").append(TB_NAME).append(" (").append("_id integer primary key autoincrement,")  
	           .append("name text,").append("startpage integer,").append("endpage integer,").append("year integer,").append("month integer,").append("date integer,")
	           .append("hour integer,").append("minute integer,").append("second integer,").append("summary text")
	           .append(")");  
		db.execSQL(sql.toString()); 
	}
	
	/**
	 * 当检测与前一次创建数据库版本不一样时，先删除表再创建新表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}
	

	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
		try{
			db.execSQL("ALTER TABLE " +
					TB_NAME + " CHANGE " +
					oldColumn + " "+ newColumn +
					" " + typeColumn
			);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 添加数据
	 */
	public void insertData(SQLiteDatabase db,String name,int startpage,int endpage,int year,int month,int date,int hour,int minute,int second,String summary){   
		ContentValues values = new ContentValues();
		values.put("name", name.trim());
		values.put("startpage", startpage);
		values.put("endpage", endpage);
		values.put("year", year);
		values.put("month", month);;
		values.put("date", date);
		values.put("hour", hour);
		values.put("minute", minute);
		values.put("second", second);
		values.put("summary", summary.trim());
		db.insert(SQLiteHelper2.TB_NAME, null, values);
	}
	
}
