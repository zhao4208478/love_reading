package com.example.love_reading;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.love_reading.MainActivity.MyAdapter;
import com.example.love_reading.sql.SQLiteHelper2;
import com.example.love_reading.sql.SQLiteHelper3;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import android.app.AlertDialog.Builder; 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.TextView;
import android.widget.Toast;

public class Bookthink<viewAddEmployee> extends Activity implements Formatter{
	
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
    private final int LISTDIALOG = 1;  
	private Cursor cursor;
	private ImageButton show,decide;
	private TextView bookplan_title;
	private EditText bookplan_think;
	private SQLiteDatabase db;
	private SQLiteHelper2 dbHelper;
	public static final String DB_NAME = "bookplan.db";
	public static final String DB_NAME2 = "booknow.db";
	public static int DB_VERSION = 1;
	private File path = new File("/sdcard/love_reading");// 创建目录
	private File f = new File("/sdcard/love_reading/bookplan.db");// 创建文件
	private File f2 = new File("/sdcard/love_reading/booknow.db");// 创建文件
    private MyAdapter adapter;
	private Intent intent;
	private String str;
	private NumberPicker mNumberPicker,mNumberPicker2;  
	private int now;
	private SQLiteHelper3 dbHelper2;
	private String tag = Bookthink.this.getClass().getSimpleName();
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bookthink);
        bookplan_title=(TextView)findViewById(R.id.title);
        bookplan_think=(EditText)findViewById(R.id.book_think);
        show=(ImageButton)findViewById(R.id.show);
        decide=(ImageButton)findViewById(R.id.decide);        
        intent=getIntent();
        bookplan_title.setText("对于"+intent.getStringExtra("name")+"的感受");
        createData();
        getdata();
        init();
        look();
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
	
	private void getdata() {
		try {
			dbHelper2 = new SQLiteHelper3(this, DB_NAME2, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f2, null);
			/* 查询表，得到cursor对象 */
			cursor = db.rawQuery("select * from booknow where name='"+intent.getStringExtra("name")+"'", null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			now = cursor.getInt(2);
			cursor.moveToNext();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}
	}
	
	private void init() {  
        mNumberPicker = (NumberPicker) findViewById(R.id.book_think_startpage);  
        mNumberPicker.setFormatter(this);  
        mNumberPicker.setMaxValue(Integer.parseInt(intent.getStringExtra("pages")));  
        mNumberPicker.setMinValue(0);  
        mNumberPicker.setValue(now);  
        mNumberPicker2 = (NumberPicker) findViewById(R.id.book_think_endpage);  
        mNumberPicker2.setFormatter(this);   
        mNumberPicker2.setMaxValue(Integer.parseInt(intent.getStringExtra("pages")));  
        mNumberPicker2.setMinValue(0);  
        mNumberPicker2.setValue(now);  
    }  
	
	public String format(int value) {  
        String tmpStr = String.valueOf(value);  
        if (value < 10) {  
            tmpStr = "000" + tmpStr;  
        }  
        else if (value < 100) {  
            tmpStr = "00" + tmpStr;  
        }  
        else if (value < 1000) {  
            tmpStr = "0" + tmpStr;  
        }  
        return tmpStr;  
    }
	
	public void look(){
		decide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intthink();
				finish();
			}
		});
		show.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intthink();
				mController.setShareContent("#图书分享#对于"+intent.getStringExtra("name")+"这本书,读过"+mNumberPicker.getValue()+"至"+mNumberPicker2.getValue()+"之后,我有一些心得.具体如下："+bookplan_think.getText().toString());
				showDialog(LISTDIALOG);
				//mController.openShare(Bookthink.this, false);
				//finish();
				}
		});
	}
	
	protected Dialog onCreateDialog(int id) {  
        Dialog dialog = null;  
        switch(id) {  
            case LISTDIALOG:  
                Builder builder = new AlertDialog.Builder(this);  
                builder.setTitle("感悟分享平台选择");  
                DialogInterface.OnClickListener listener =   
                    new DialogInterface.OnClickListener() {  
                          
                        @Override  
                        public void onClick(DialogInterface dialogInterface,   
                                int which) {  
                        	switch(which) {  
                            case 0:
                            	mController.doOauthVerify(Bookthink.this, SHARE_MEDIA.QZONE, new UMAuthListener() {
                            	    @Override
                            	    public void onStart(SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权开始", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onError(SocializeException e, SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权错误", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权完成", Toast.LENGTH_SHORT).show();
                            	        //获取相关授权信息或者跳转到自定义的分享编辑页面
                            	        String uid = value.getString("uid");
                            	    }

                            	    @Override
                            	    public void onCancel(SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权取消", Toast.LENGTH_SHORT).show();
                            	    }
                            	} );
                            	mController.directShare(Bookthink.this, SHARE_MEDIA.QZONE,
                                    new SnsPostListener() {

                                @Override
                                public void onStart() {
                                    Toast.makeText(Bookthink.this, "分享开始",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
                                    if(eCode == StatusCode.ST_CODE_SUCCESSED){
                                        Toast.makeText(Bookthink.this, "分享成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(Bookthink.this, "分享失败",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            	   break;
                            case 1:
                            	mController.doOauthVerify(Bookthink.this, SHARE_MEDIA.TENCENT, new UMAuthListener() {
                            	    @Override
                            	    public void onStart(SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权开始", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onError(SocializeException e, SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权错误", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权完成", Toast.LENGTH_SHORT).show();
                            	        //获取相关授权信息或者跳转到自定义的分享编辑页面
                            	        String uid = value.getString("uid");
                            	    }

                            	    @Override
                            	    public void onCancel(SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权取消", Toast.LENGTH_SHORT).show();
                            	    }
                            	} );
                            	mController.directShare(Bookthink.this, SHARE_MEDIA.TENCENT,
                                    new SnsPostListener() {

                                @Override
                                public void onStart() {
                                    Toast.makeText(Bookthink.this, "分享开始",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
                                    if(eCode == StatusCode.ST_CODE_SUCCESSED){
                                        Toast.makeText(Bookthink.this, "分享成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(Bookthink.this, "分享失败",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            	break;
                            case 2:
                            	mController.doOauthVerify(Bookthink.this, SHARE_MEDIA.SINA, new UMAuthListener() {
                            	    @Override
                            	    public void onStart(SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权开始", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onError(SocializeException e, SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权错误", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权完成", Toast.LENGTH_SHORT).show();
                            	        //获取相关授权信息或者跳转到自定义的分享编辑页面
                            	        String uid = value.getString("uid");
                            	    }

                            	    @Override
                            	    public void onCancel(SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权取消", Toast.LENGTH_SHORT).show();
                            	    }
                            	} );
                            	mController.directShare(Bookthink.this, SHARE_MEDIA.SINA,
                                    new SnsPostListener() {

                                @Override
                                public void onStart() {
                                    Toast.makeText(Bookthink.this, "分享开始",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
                                    if(eCode == StatusCode.ST_CODE_SUCCESSED){
                                        Toast.makeText(Bookthink.this, "分享成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(Bookthink.this, "分享失败",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            	break;
                            case 3:
                            	mController.doOauthVerify(Bookthink.this, SHARE_MEDIA.DOUBAN, new UMAuthListener() {
                            	    @Override
                            	    public void onStart(SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权开始", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onError(SocializeException e, SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权错误", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权完成", Toast.LENGTH_SHORT).show();
                            	        //获取相关授权信息或者跳转到自定义的分享编辑页面
                            	        String uid = value.getString("uid");
                            	    }

                            	    @Override
                            	    public void onCancel(SHARE_MEDIA platform) {
                            	        Toast.makeText(Bookthink.this, "授权取消", Toast.LENGTH_SHORT).show();
                            	    }
                            	} );
                            	mController.directShare(Bookthink.this, SHARE_MEDIA.DOUBAN,
                                    new SnsPostListener() {

                                @Override
                                public void onStart() {
                                    Toast.makeText(Bookthink.this, "分享开始",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
                                    if(eCode == StatusCode.ST_CODE_SUCCESSED){
                                        Toast.makeText(Bookthink.this, "分享成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(Bookthink.this, "分享失败",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            	break;
                        	}
                        }  
                    };  
                builder.setItems(R.array.hobby, listener);  
                dialog = builder.create();  
                break;  
        }  
        return dialog;  
    }  
	
	public void intthink() {
			// TODO Auto-generated method stub
		    Time t=new Time();
		    t.setToNow();
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			Log.i(tag," "+t.month);
			dbHelper = new SQLiteHelper2(this, DB_NAME, null, DB_VERSION);
			dbHelper.insertData(db , intent.getStringExtra("name") , mNumberPicker.getValue() , mNumberPicker2.getValue() , t.year , t.month + 1 , t.monthDay , t.hour , t.minute , t.second , bookplan_think.getText().toString() );
			if (now < mNumberPicker2.getValue()) {
				change();
			}
	}

	
	
	private void change() {
		// TODO Auto-generated method stub
		db = SQLiteDatabase.openOrCreateDatabase(f2, null);
		dbHelper2 = new SQLiteHelper3(this, DB_NAME2, null, DB_VERSION);
		ContentValues values =new ContentValues();
		values.put("now", mNumberPicker2.getValue());
		db.update("booknow", values, "name='"+intent.getStringExtra("name")+"'", null);
	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    /**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}