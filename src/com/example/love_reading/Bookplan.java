package com.example.love_reading;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.love_reading.MainActivity.MyAdapter;
import com.example.love_reading.MainActivity.ViewHolder;
import com.example.love_reading.R.id;
import com.example.love_reading.sql.SQLiteHelper;
import com.example.love_reading.sql.SQLiteHelper2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Bookplan extends Activity {
	
	private List<Map<String, Object>> mData;
	private Cursor cursor;
	private int i;
	private String tag = Bookplan.this.getClass().getSimpleName();
	private TextView bookplan_title;
	private ListView bookplan_list;
	private SQLiteDatabase db;
	private SQLiteHelper2 dbHelper;
	public static final String DB_NAME = "bookplan.db";
	public static int DB_VERSION = 1;
	private File path = new File("/sdcard/love_reading");// 创建目录
	private File f = new File("/sdcard/love_reading/bookplan.db");// 创建文件
    private MyAdapter adapter;
	private Intent intent;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bookplan);
        bookplan_title=(TextView)findViewById(R.id.bookplan_title);
        intent=getIntent();
        bookplan_title.setText(intent.getStringExtra("name")+"的阅读记录");
        Log.i(tag, intent.getStringExtra("name"));
        createData();
        init();
        setlistener();
    }
	
    public void setlistener() {
	bookplan_list.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			getall(arg2);
		}
	});
    }
    
	private void getall(int arg2) {
		try {
			dbHelper = new SQLiteHelper2(this, DB_NAME, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			/* 查询表，得到cursor对象 */
			cursor = db.rawQuery("select * from bookplan where _id='"+(arg2+1)+"'", null);
			cursor.moveToFirst();
			if (!cursor.isAfterLast() && (cursor.getString(10) != null)) {	
		        Log.i(tag, "ssss:"+cursor.getString(10));
		    	Intent intent2 = new Intent(Bookplan.this, Booksummary.class);
				intent2.putExtra("summary", cursor.getString(10));
				startActivity(intent2);
			}
			else {
				 Toast.makeText(this, "本阅读记录无感悟", Toast.LENGTH_SHORT).show();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}
	}
	
    private void init() {
    	bookplan_list = (ListView) findViewById(R.id.bookplan_list);
    	mData = gettp();
    	adapter = new MyAdapter(this);
    	bookplan_list.setAdapter(adapter);
	}

    private List<Map<String, Object>> gettp() {
    	BitmapDrawable bd; 
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		try {
			dbHelper = new SQLiteHelper2(this, DB_NAME, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			/* 查询表，得到cursor对象 */
			cursor = db.rawQuery("select * from bookplan where name='"+intent.getStringExtra("name")+"'", null);
			cursor.moveToFirst();
			Log.i(tag, "show");
			i=0;
			while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
				map = new HashMap<String, Object>();
				map.put("name",cursor.getString(1));
				map.put("startpage",cursor.getInt(2));
				map.put("endpage",cursor.getInt(3));
				map.put("year",cursor.getInt(4));
				map.put("month",cursor.getInt(5));
				map.put("date",cursor.getInt(6));
				map.put("hour",cursor.getInt(7));
				map.put("minute",cursor.getInt(8));
				map.put("second",cursor.getInt(9));
				map.put("summary",cursor.getString(10));
				list.add(map);
				cursor.moveToNext();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
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
			dbHelper = new SQLiteHelper2(this, DB_NAME, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			StringBuilder sql = new StringBuilder(); 
	        sql.append("CREATE TABLE IF NOT EXISTS ").append(SQLiteHelper2.TB_NAME).append(" (").append("_id integer primary key autoincrement,")  
	           .append("name text,").append("startpage integer,").append("endpage integer,").append("year integer,").append("month integer,").append("date integer,")
	           .append("hour integer,").append("minute integer,").append("second integer,").append("summary text")
	           .append(")");  
			db.execSQL(sql.toString()); 
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}
		db.close();
	}
	
	public final class ViewHolder{
		public TextView name;
		public TextView startpage;
		public TextView endpage;
		public TextView time;
		public TextView summary;
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
				
				convertView = mInflater.inflate(R.layout.list_item, null);
				holder.name = (TextView)convertView.findViewById(R.id.name);
				holder.startpage = (TextView)convertView.findViewById(R.id.startpage);
				holder.endpage = (TextView)convertView.findViewById(R.id.endpage);
				holder.time = (TextView)convertView.findViewById(R.id.time);			
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.name.setText(" "+(String)mData.get(position).get("name")+" ");
			holder.startpage.setText(" "+mData.get(position).get("startpage")+" ");
			holder.endpage.setText(" "+mData.get(position).get("endpage")+" ");
			holder.time.setText(" "+mData.get(position).get("year")+"-"+mData.get(position).get("month")+"-"+mData.get(position).get("date")+" "+mData.get(position).get("hour")+":"+mData.get(position).get("minute")+":"+mData.get(position).get("second")+" ");
			return convertView;
		}
		
	}
	
}