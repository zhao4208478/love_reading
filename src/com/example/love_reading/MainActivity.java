package com.example.love_reading;

import com.example.love_reading.R.id;
import com.example.love_reading.seeyou.CaptureActivityPortrait;

import android.R.drawable;
import android.R.layout;
import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;  
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private ImageButton button1;
	public ImageButton button2;
	public ImageButton button3;
	public ImageButton button4;
	public ImageButton button5;
	public ImageButton button6;
    private String isbn;
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
		isbn = intent.getStringExtra("bookId");
		Log.i(tag, "result2:" + isbn);
	}
    
}
