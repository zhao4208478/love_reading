package com.example.love_reading;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.example.love_reading.sql.SQLiteHelper2;
import com.example.love_reading.sql.SQLiteHelper3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import com.example.love_reading.BookInfo;

public class Booksee extends Activity {
    private Intent intent;
    private TextView title,author,publisher,date,isbn,summary,price,pages,now;
    private ImageView cover;
    private ImageButton reading_plan,reading_show;
	private File path = new File("/sdcard/love_reading");// 创建目录
	private File f = new File("/sdcard/love_reading/booknow.db");// 创建文件
	private SQLiteDatabase db;
	private SQLiteHelper3 dbHelper;
	private Cursor cursor;
	public static final String DB_NAME = "booknow.db";
	public static int DB_VERSION = 1;
	public int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bookview);

        reading_plan=(ImageButton)findViewById(R.id.book_read);
        reading_show=(ImageButton)findViewById(R.id.book_show);
        title=(TextView)findViewById(R.id.bookview_title);
        author=(TextView)findViewById(R.id.bookview_author);
        publisher=(TextView)findViewById(R.id.bookview_publisher);
        date=(TextView)findViewById(R.id.bookview_publisherdate);
        isbn=(TextView)findViewById(R.id.bookview_isbn);
        summary=(TextView)findViewById(R.id.bookview_summary);
        cover=(ImageView)findViewById(R.id.bookview_cover);
        price=(TextView)findViewById(R.id.bookview_price);
        pages=(TextView)findViewById(R.id.bookview_pages);
        now=(TextView)findViewById(R.id.bookview_now);
        intent=getIntent();
        //BookInfo book=(BookInfo) getIntent().getSerializableExtra(BookInfo.class.getName());
       
        title.setText(intent.getStringExtra("name"));
        author.setText("作者："+intent.getStringExtra("author"));
        publisher.setText("出版公司："+intent.getStringExtra("publisher"));
        date.setText("出版时间："+intent.getStringExtra("date"));
        isbn.setText("isbn："+intent.getStringExtra("isbn"));
        summary.setText("    "+intent.getStringExtra("summary"));
        byte[] image = intent.getByteArrayExtra("image");
        cover.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        price.setText("价格："+intent.getStringExtra("price"));
        pages.setText("页数："+intent.getStringExtra("pages"));
        createData();
        onclick();
        getdata();
        now.setText("当前读到页数："+i);
    }

		
    public void onclick() {
		reading_plan.setOnTouchListener(new OnTouchListener() {

			@Override
			  public boolean onTouch(View v, MotionEvent event) {     
                if(event.getAction() == MotionEvent.ACTION_DOWN){     
                        //鏇存敼涓烘寜涓嬫椂鐨勮儗鏅浘鐗�     
                        //v.setBackgroundResource(R.drawable.pressed);     
                }else if(event.getAction() == MotionEvent.ACTION_UP){     
                        //鏀逛负鎶捣鏃剁殑鍥剧墖     
                        //v.setBackgroundResource(R.drawable.released);     
                	Intent i = new Intent();
    				i.setClass(Booksee.this, Bookplan.class);
    				i.putExtra("name", intent.getStringExtra("name"));
    				startActivity(i);
                }     
				return false;
			}
		});
		reading_show.setOnTouchListener(new OnTouchListener() {

			@Override
			  public boolean onTouch(View v, MotionEvent event) {     
                if(event.getAction() == MotionEvent.ACTION_DOWN){     
                        //鏇存敼涓烘寜涓嬫椂鐨勮儗鏅浘鐗�    
                        //v.setBackgroundResource(R.drawable.pressed);     
                }else if(event.getAction() == MotionEvent.ACTION_UP){     
                        //鏀逛负鎶捣鏃剁殑鍥剧墖     
                        //v.setBackgroundResource(R.drawable.released);     
                	Intent in = new Intent();
    				in.setClass(Booksee.this, Bookthink.class);
    				in.putExtra("name", intent.getStringExtra("name"));
    				in.putExtra("pages", intent.getStringExtra("pages"));
    				startActivity(in);
                }     
				return false;
			}
		});
	}
    
	private void createData() {
		if (!path.exists()) {// 目录存在返回false
			path.mkdirs();// 创建一个目录
		}
		if (!f.exists()) {// 文件存在返回false
			try {
				f.createNewFile();// 创建文件
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			dbHelper = new SQLiteHelper3(this, DB_NAME, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			StringBuilder sql = new StringBuilder(); 
	        sql.append("CREATE TABLE IF NOT EXISTS ").append(SQLiteHelper3.TB_NAME).append(" (").append("_id integer primary key autoincrement,")  
	           .append("name text,").append("now integer")
	           .append(")");  
			db.execSQL(sql.toString()); 
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}
		db.close();
	} 
    
	private void getdata() {
		i = -1 ;
		try {
			dbHelper = new SQLiteHelper3(this, DB_NAME, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			/* 查询表，得到cursor对象 */
			cursor = db.rawQuery("select * from booknow where name='"+intent.getStringExtra("name")+"'", null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			i = cursor.getInt(2);
			cursor.moveToNext();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}
		if (i == -1) {
			dbHelper.insertData(db , intent.getStringExtra("name") , 0);
			i = 0;
		}
	}
  //返回键处理
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
