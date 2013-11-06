package com.example.love_reading;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageButton;

public class Main_loving_book extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_loving_book);
        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton1);
        ImageButton button2 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton button3 = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton button4 = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton button5 = (ImageButton) findViewById(R.id.imageButton5);
        ImageButton button6 = (ImageButton) findViewById(R.id.imageButton6);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_loving_book, menu);
		return true;
	}

}
