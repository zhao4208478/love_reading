package com.example.love_reading;

import com.example.love_reading.R.id;
import com.example.love_reading.seeyou.CaptureActivityPortrait;
import com.example.love_reading.util.DataUtil;

import android.R.drawable;
import android.R.layout;
import android.R.string;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;  
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ImageButton button1;
	public ImageButton button2;
	public ImageButton button3;
	public ImageButton button4;
	public ImageButton button5;
	public ImageButton button6;
	private long mExitTime;
	private Handler hd;
    private ProgressDialog mpd;
	private String tag = MainActivity.this.getClass()
			.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (ImageButton)findViewById(id.imageButton1);
        button2 = (ImageButton)findViewById(id.imageButton2);
        button3 = (ImageButton)findViewById(id.imageButton3);
        button4 = (ImageButton)findViewById(id.imageButton4);
        button5 = (ImageButton)findViewById(id.imageButton5);
        button6 = (ImageButton)findViewById(id.imageButton6);
        setlistener();
        getisbn();
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
    public void setlistener() {
		// TODO Auto-generated method stub
    	button1.setOnTouchListener(new OnTouchListener(){     
            @Override    
            public boolean onTouch(View v, MotionEvent event) {     
                    if(event.getAction() == MotionEvent.ACTION_DOWN){     
                            //更改为按下时的背景图片     
                            //v.setBackgroundResource(R.drawable.pressed);     
                    }else if(event.getAction() == MotionEvent.ACTION_UP){     
                            //改为抬起时的图片     
                            //v.setBackgroundResource(R.drawable.released);     
                    	Intent i = new Intent();
        				i.setClass(MainActivity.this, CaptureActivityPortrait.class);
        				startActivity(i);
                    }     
                    return false;     
            }     
    });   
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
			new DownloadThread(isbn).start();   
			setbookinfo();
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
	
	private void setbookinfo(){
    	hd=	new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                BookInfo book= (BookInfo)msg.obj;
                mpd.dismiss();
                Intent intent=new Intent(MainActivity.this,BookView.class);
                //Bundle bd=new Bundle();
                //bundle.putSerializable(key,object);
                //bd.putSerializable(BookInfo.class.getName(),book);
                //intent.putExtras(bd);
                intent.putExtra(BookInfo.class.getName(),book);
                startActivity(intent);
            }
        };
    }
	
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
            Log.i("OUTPUT", "parse over");
            Log.i("OUTPUT",book.getSummary()+book.getAuthor());

            Message msg=Message.obtain();
            msg.obj=book;
            hd.sendMessage(msg);
            Log.i("OUTPUT","send over");
        }
    }
    
    //返回键处理
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        Object mHelperUtils;
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();

                } else {
                        finish();
                        System.exit(0);
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
