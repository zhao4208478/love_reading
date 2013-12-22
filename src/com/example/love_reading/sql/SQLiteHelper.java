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
public class SQLiteHelper extends SQLiteOpenHelper {
	private String tag = SQLiteHelper.this.getClass().getSimpleName();
	public static final String TB_NAME = "bookInfo";

	public SQLiteHelper(Context context, String name, CursorFactory factory,
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
           .append("name text,").append("author text,")  
           .append("publisher text,").append("date text,")  
           .append("isbn text,").append("summary text,")  
           .append("image BLOB,").append("price text,").append("pages text")   
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
	
	/**
	 * 变更列名
	 * @param db
	 * @param oldColumn
	 * @param newColumn
	 * @param typeColumn
	 */
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
	public void insertData(SQLiteDatabase db,String name,String author,String publisher,String date,String isbn,String summary,Bitmap image,String Price,String Pages){  
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
		image.compress(Bitmap.CompressFormat.PNG, 100, os);
		ContentValues values = new ContentValues();
		Log.i(tag, "test2:"+name.trim());
		values.put("name", name.trim());
		Log.i(tag, "test2:"+author.trim());
		values.put("author", author.trim());
		Log.i(tag, "test2:"+publisher.trim());
		values.put("publisher", publisher.trim());
		Log.i(tag, "test2:"+date.trim());
		values.put("date", date.trim());
		Log.i(tag, "test2:"+isbn.trim());
		values.put("isbn", isbn.trim());
		Log.i(tag, "test2:"+summary.trim());
		values.put("summary", summary.trim());
		Log.i(tag, "test2:"+os.toByteArray());
		values.put("image", os.toByteArray());
		Log.i(tag, "test2:"+Price.trim());
		values.put("price", Price.trim());
		Log.i(tag, "test2:"+Pages.trim());
		values.put("pages", Pages.trim());
		db.insert(SQLiteHelper.TB_NAME, null, values);
	}
	
}
