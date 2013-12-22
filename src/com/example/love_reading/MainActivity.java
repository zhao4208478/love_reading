package com.example.love_reading;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.love_reading.R.id;
import com.example.love_reading.seeyou.CaptureActivityPortrait;
import com.example.love_reading.sql.ContentBean;
import com.example.love_reading.sql.SQLiteHelper;
import com.example.love_reading.util.DataUtil;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;  
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */   
	private List<Map<String, Object>> mData;
	private ListView shelf_list;
    // 书架的列数  
    public ImageButton button_check;
	private int i;
	public static final String DB_NAME = "bookInfo.db";
	public static int DB_VERSION = 1;
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private long mExitTime;
	private Cursor cursor;
	private String tag = MainActivity.this.getClass().getSimpleName();
	private File path = new File("/sdcard/love_reading");// 创建目录
	private File f = new File("/sdcard/love_reading/bookInfo.db");// 创建文件
    private ProgressDialog mpd;
    private MyAdapter adapter;
	private Handler hd;
	boolean longClicked = false;
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
    	exit.getInstance().addActivity(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_main);  
        button_check=(ImageButton)findViewById(id.shelf_image_button);
        createData();
        init();
        setlistener();
        getisbn();
    }  
  
    
   /* private void init() {
		// TODO Auto-generated method stub
        shelf_list = (ListView) findViewById(R.id.shelf_list);  
        ShelfAdapter adapter = new ShelfAdapter();  
        shelf_list.setAdapter(adapter);  
	}*/


    private void init() {
    	shelf_list = (ListView) findViewById(R.id.shelf_list);
    	mData = gettp();
    	adapter = new MyAdapter(this);
    	shelf_list.setAdapter(adapter);
	}

	/**
	 * 提取数据库数据，填充list内数据
	 */
    private List<Map<String, Object>> gettp() {
    	BitmapDrawable bd; 
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		try {
			dbHelper = new SQLiteHelper(this, DB_NAME, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			/* 查询表，得到cursor对象 */
			cursor = db.rawQuery("select * from bookInfo", null);
			cursor.moveToFirst();
			Log.i(tag, "show");
			i=0;
			while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
				map = new HashMap<String, Object>();
				map.put("title",cursor.getString(1));
				map.put("img", BitmapFactory.decodeByteArray(cursor.getBlob(7), 0, cursor.getBlob(7).length));
				list.add(map);
				cursor.moveToNext();
			}
		} catch (IllegalArgumentException e) {
			// 当用SimpleCursorAdapter装载数据时，表ID列必须是_id，否则报错column '_id' does not
			// exist
			e.printStackTrace();
			// 当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表 注：表以前数据将丢失
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
			// dbHelper.updateColumn(db, SQLiteHelper.ID, "_"+SQLiteHelper.ID,
			// "integer");
		}
		return list;
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
			/* 初始化并创建数据库 */
			// if (dbHelper == null) {
			dbHelper = new SQLiteHelper(this, DB_NAME, null, DB_VERSION);
			// }
			/* 创建表 */
			// db = dbHelper.getWritableDatabase();
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			StringBuilder sql = new StringBuilder(); 
	        sql.append("CREATE TABLE IF NOT EXISTS ").append(SQLiteHelper.TB_NAME).append(" (").append("_id integer primary key autoincrement,")  
	           .append("name text,").append("author text,")  
	           .append("publisher text,").append("date text,")  
	           .append("isbn text,").append("summary text,")  
	           .append("image BLOB,").append("price text,").append("pages text")   
	           .append(")");  
			db.execSQL(sql.toString()); 
		} catch (IllegalArgumentException e) {
			// 当用SimpleCursorAdapter装载数据时，表ID列必须是_id，否则报错column '_id' does not
			// exist
			e.printStackTrace();
			// 当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表 注：表以前数据将丢失
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
			// dbHelper.updateColumn(db, SQLiteHelper.ID, "_"+SQLiteHelper.ID,
			// "integer");
		}
		db.close();
	}
	
	public final class ViewHolder{
		public ImageView img;
		public TextView title;
	}
	public class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.vlist, null);
				holder.img = (ImageView)convertView.findViewById(R.id.img);
				holder.title = (TextView)convertView.findViewById(R.id.title);
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			
			holder.img.setImageBitmap((Bitmap)mData.get(position).get("img"));
			holder.title.setText((String)mData.get(position).get("title"));
			return convertView;
		}
		
	}
  
	public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
    public void setlistener() {
		// TODO Auto-generated method stub
    	button_check.setOnTouchListener(new OnTouchListener() {
    		@Override    
            public boolean onTouch(View v, MotionEvent event) {     
                    if(event.getAction() == MotionEvent.ACTION_DOWN){     
                            //鏇存敼涓烘寜涓嬫椂鐨勮儗鏅浘鐗�    
                            //v.setBackgroundResource(R.drawable.pressed);     
                    }else if(event.getAction() == MotionEvent.ACTION_UP){     
                            //鏀逛负鎶捣鏃剁殑鍥剧墖     
                            //v.setBackgroundResource(R.drawable.released);     
                    	Intent i = new Intent();
        				i.setClass(MainActivity.this, CaptureActivityPortrait.class);
        				startActivity(i);
                    }     
                    return false;     
            }     
    });   
    	if(longClicked==false){
    		shelf_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				getall(arg2);
			}
		});
    	}
    	
    	shelf_list.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            	delete(i);
            //	change();
            //	deletebox(i);
                return false;
            }
        });
    }
    
    
    
    protected void change() {
		// TODO Auto-generated method stub
    	boolean longClicked = true;
	}


	private void deletebox(final int i){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
    	builder.setMessage("真的要删除"+mData.get(i).get("title")+"这本书吗？") 
    	       .setCancelable(false) 
    	       .setPositiveButton("是", new DialogInterface.OnClickListener() { 
    	           public void onClick(DialogInterface dialog, int id) { 
    	        	    delete(i);
    	            	longClicked = false;
    	           } 
    	       }) 
    	       .setNegativeButton("否", new DialogInterface.OnClickListener() { 
    	           public void onClick(DialogInterface dialog, int id) { 
    	                dialog.cancel(); 
    	            	longClicked = false;
    	           } 
    	       }).show(); 
    	AlertDialog alert = builder.create(); 
    }
    private void delete(int i) {
        hd=	new Handler();
    	dbHelper = new SQLiteHelper(this, DB_NAME, null, DB_VERSION);
		db = SQLiteDatabase.openOrCreateDatabase(f, null);
		db.delete("bookInfo","name='"+mData.get(i).get("title")+"'", null);
    	hd.post(add);
    }
    
    
    private void getall(int arg2) {
    	Intent intent = new Intent(MainActivity.this, Booksee.class);
		try {
			dbHelper = new SQLiteHelper(this, DB_NAME, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			/* 查询表，得到cursor对象 */
			cursor = db.rawQuery("select * from bookInfo where name='"+mData.get(arg2).get("title")+"'", null);
			cursor.moveToFirst();
			    Log.i(tag, "show");
			    
				Log.i(tag, "test1:"+cursor.getString(1));
				intent.putExtra("name", cursor.getString(1));
				
				Log.i(tag, "test1:"+cursor.getString(2));
				intent.putExtra("author", cursor.getString(2));
				
				Log.i(tag, "test1:"+cursor.getString(3));
				intent.putExtra("publisher", cursor.getString(3));
			
				Log.i(tag, "test1:"+cursor.getString(4));
				intent.putExtra("date", cursor.getString(4));
				
				Log.i(tag, "test1:"+cursor.getString(5));
				intent.putExtra("isbn", cursor.getString(5));
				
				Log.i(tag, "test1:"+cursor.getString(6));
				intent.putExtra("summary", cursor.getString(6));
				
				Log.i(tag, "test1:"+cursor.getBlob(7));
				intent.putExtra("image", cursor.getBlob(7));
				
				Log.i(tag, "test1:"+cursor.getString(8));
				intent.putExtra("price", cursor.getString(8));
				
				Log.i(tag, "test1:"+cursor.getString(9));
				intent.putExtra("pages", cursor.getString(9));
				  
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}
		startActivity(intent);
	}
    
	private void getisbn() {
		// TODO Auto-generated method stub
		Intent intent = this.getIntent();
		String isbn = intent.getStringExtra("bookId");
		Log.i(tag, "result2:" + isbn);
		if (isbn!=null) getbookinfo(isbn);
		isbn = null;
	}


	private void getbookinfo(String isbn) {
		// TODO Auto-generated method stub
		try {
			mpd=new ProgressDialog(this);
	        mpd.setMessage("信息下载中。。。");
	        mpd.show();
            hd=	new Handler();
			new DownloadThread(isbn).start();
		} catch (Exception e) {
			// TODO: handle exception
			new  AlertDialog.Builder(this)    
			                .setTitle("提醒" )
			                .setMessage("图书信息获取错误" )  
			                .setPositiveButton("确定" ,  null )  
			                .show();
			return ;
		}
		
	}

	Runnable add=new Runnable(){
    @Override
    public void run() {
    	mData = gettp();
    	shelf_list.setAdapter(adapter); } 
    };
	
	private class DownloadThread extends Thread
    {
        String url=null;
        public DownloadThread(String isbn)
        {
            url="https://api.douban.com/v2/book/isbn/"+isbn;
			Log.i(tag, "url:"+url);
        }
        public void run()
        {
            String result=DataUtil.Download(url);
            Log.i("OUTPUT", "download over");
            BookInfo book=new DataUtil().parseBookInfo(result);
            Log.i("OUTPUT",book.getPrice()+book.getPages());
            Log.i("OUTPUT", "parse over");
            Log.i("OUTPUT",book.getSummary()+book.getAuthor());
            Log.i("OUTPUT","send over");
            sqlsave(book);
        	hd.post(add);
        }
    }
    
	public void sqlsave(BookInfo book) {
		// TODO Auto-generated method stub
		db = SQLiteDatabase.openOrCreateDatabase(f, null);
		dbHelper = new SQLiteHelper(this, DB_NAME, null, DB_VERSION);
		dbHelper.insertData(db , book.getTitle() , book.getAuthor() , book.getPublisher() , book.getPublishDate() , book.getISBN() , book.getSummary() , book.getBitmap(),book.getPrice(),book.getPages());
		mpd.dismiss();
	}
	
    //返回键处理
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();

                } else {
                	exit.getInstance().exit();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
