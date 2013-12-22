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
 * ʵ�ֶԱ�Ĵ��������¡������������
 *
 */
public class SQLiteHelper3 extends SQLiteOpenHelper {
	private String tag = SQLiteHelper3.this.getClass().getSimpleName();
	public static final String TB_NAME = "booknow";

	public SQLiteHelper3(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	/**
	 * �����±�
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder(); 
        sql.append("CREATE TABLE IF NOT EXISTS ").append(TB_NAME).append(" (").append("_id integer primary key autoincrement,")  
	           .append("name text,").append("now integer")
	           .append(")");  
		db.execSQL(sql.toString()); 
	}
	
	/**
	 * �������ǰһ�δ������ݿ�汾��һ��ʱ����ɾ�����ٴ����±�
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
	 * �������
	 */
	public void insertData(SQLiteDatabase db,String name,int now){   
		ContentValues values = new ContentValues();
		values.put("name", name.trim());
		values.put("now", now);
		db.insert(SQLiteHelper3.TB_NAME, null, values);
	}
	
}
