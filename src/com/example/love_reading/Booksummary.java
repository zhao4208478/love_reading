package com.example.love_reading;

import java.io.File;
import java.util.HashMap;

import com.example.love_reading.sql.SQLiteHelper;
import com.example.love_reading.sql.SQLiteHelper2;
import com.example.love_reading.sql.SQLiteHelper3;
import com.example.love_reading.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.love_reading.BookInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;

public class Booksummary extends Activity {
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
    private final int LISTDIALOG = 1;  
	private File path = new File("/sdcard/love_reading");// 创建目录
	private File f = new File("/sdcard/love_reading/bookplan.db");// 创建文件
	private SQLiteDatabase db;
	private SQLiteHelper2 dbHelper;
	private Cursor cursor;
	public static final String DB_NAME = "bookplan.db";
	public static int DB_VERSION = 1;
    private Intent intent;
	private String tag = Booksummary.this.getClass().getSimpleName();
	private Button button;
    private TextView summary;
    private String name,summary1;
    private int startpage,endpage;   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.booksummary);

        summary=(TextView)findViewById(R.id.booksummary1);
        intent=getIntent();
        Log.i(tag, "sssss:"+intent.getStringExtra("summary"));
        summary.setText(""+intent.getStringExtra("summary"));
        button=(Button)findViewById(R.id.booksummary2);
        init();
        button.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mController.setShareContent("#图书分享#对于"+name+"这本书,读过"+startpage+"至"+endpage+"之后,我有一些心得.具体如下："+summary1);
				showDialog(LISTDIALOG);
				return false;
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
                            	mController.doOauthVerify(Booksummary.this, SHARE_MEDIA.QZONE, new UMAuthListener() {
                            	    @Override
                            	    public void onStart(SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权开始", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onError(SocializeException e, SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权错误", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权完成", Toast.LENGTH_SHORT).show();
                            	        //获取相关授权信息或者跳转到自定义的分享编辑页面
                            	        String uid = value.getString("uid");
                            	    }

                            	    @Override
                            	    public void onCancel(SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权取消", Toast.LENGTH_SHORT).show();
                            	    }
                            	} );
                            	mController.directShare(Booksummary.this, SHARE_MEDIA.QZONE,
                                    new SnsPostListener() {

                                @Override
                                public void onStart() {
                                    Toast.makeText(Booksummary.this, "分享开始",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
                                    if(eCode == StatusCode.ST_CODE_SUCCESSED){
                                        Toast.makeText(Booksummary.this, "分享成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(Booksummary.this, "分享失败",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            	   break;
                            case 1:
                            	mController.doOauthVerify(Booksummary.this, SHARE_MEDIA.TENCENT, new UMAuthListener() {
                            	    @Override
                            	    public void onStart(SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权开始", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onError(SocializeException e, SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权错误", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权完成", Toast.LENGTH_SHORT).show();
                            	        //获取相关授权信息或者跳转到自定义的分享编辑页面
                            	        String uid = value.getString("uid");
                            	    }

                            	    @Override
                            	    public void onCancel(SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权取消", Toast.LENGTH_SHORT).show();
                            	    }
                            	} );
                            	mController.directShare(Booksummary.this, SHARE_MEDIA.TENCENT,
                                    new SnsPostListener() {

                                @Override
                                public void onStart() {
                                    Toast.makeText(Booksummary.this, "分享开始",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
                                    if(eCode == StatusCode.ST_CODE_SUCCESSED){
                                        Toast.makeText(Booksummary.this, "分享成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(Booksummary.this, "分享失败",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            	break;
                            case 2:
                            	mController.doOauthVerify(Booksummary.this, SHARE_MEDIA.SINA, new UMAuthListener() {
                            	    @Override
                            	    public void onStart(SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权开始", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onError(SocializeException e, SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权错误", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权完成", Toast.LENGTH_SHORT).show();
                            	        //获取相关授权信息或者跳转到自定义的分享编辑页面
                            	        String uid = value.getString("uid");
                            	    }

                            	    @Override
                            	    public void onCancel(SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权取消", Toast.LENGTH_SHORT).show();
                            	    }
                            	} );
                            	mController.directShare(Booksummary.this, SHARE_MEDIA.SINA,
                                    new SnsPostListener() {

                                @Override
                                public void onStart() {
                                    Toast.makeText(Booksummary.this, "分享开始",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
                                    if(eCode == StatusCode.ST_CODE_SUCCESSED){
                                        Toast.makeText(Booksummary.this, "分享成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(Booksummary.this, "分享失败",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            	break;
                            case 3:
                            	mController.doOauthVerify(Booksummary.this, SHARE_MEDIA.DOUBAN, new UMAuthListener() {
                            	    @Override
                            	    public void onStart(SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权开始", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onError(SocializeException e, SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权错误", Toast.LENGTH_SHORT).show();
                            	    }

                            	    @Override
                            	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权完成", Toast.LENGTH_SHORT).show();
                            	        //获取相关授权信息或者跳转到自定义的分享编辑页面
                            	        String uid = value.getString("uid");
                            	    }

                            	    @Override
                            	    public void onCancel(SHARE_MEDIA platform) {
                            	        Toast.makeText(Booksummary.this, "授权取消", Toast.LENGTH_SHORT).show();
                            	    }
                            	} );
                            	mController.directShare(Booksummary.this, SHARE_MEDIA.DOUBAN,
                                    new SnsPostListener() {

                                @Override
                                public void onStart() {
                                    Toast.makeText(Booksummary.this, "分享开始",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
                                    if(eCode == StatusCode.ST_CODE_SUCCESSED){
                                        Toast.makeText(Booksummary.this, "分享成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(Booksummary.this, "分享失败",Toast.LENGTH_SHORT).show();
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
    
    
  private void init() {
		// TODO Auto-generated method stub
	  try {
			dbHelper = new SQLiteHelper2(this, DB_NAME, null, DB_VERSION);
			db = SQLiteDatabase.openOrCreateDatabase(f, null);
			/* 查询表，得到cursor对象 */
			cursor = db.rawQuery("select * from bookplan where summary='"+intent.getStringExtra("summary")+"'", null);
			cursor.moveToFirst();
			Log.i(tag, "show");
			while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
				name=cursor.getString(1);
				startpage=cursor.getInt(2);
				endpage=cursor.getInt(3);
				summary1=cursor.getString(10);
				cursor.moveToNext();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
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
