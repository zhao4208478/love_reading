package com.example.love_reading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.love_reading.BookInfo;

public class BookView extends Activity {
    private Intent intent;
    private TextView title,author,publisher,date,isbn,summary;
    private ImageView cover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bookview);

        title=(TextView)findViewById(R.id.bookview_title);
        author=(TextView)findViewById(R.id.bookview_author);
        publisher=(TextView)findViewById(R.id.bookview_publisher);
        date=(TextView)findViewById(R.id.bookview_publisherdate);
        isbn=(TextView)findViewById(R.id.bookview_isbn);
        summary=(TextView)findViewById(R.id.bookview_summary);
        cover=(ImageView)findViewById(R.id.bookview_cover);

        intent=getIntent();
        //BookInfo book=(BookInfo) getIntent().getSerializableExtra(BookInfo.class.getName());
        BookInfo book=(BookInfo)intent.getParcelableExtra(BookInfo.class.getName());

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        date.setText(book.getPublishDate());
        isbn.setText(book.getISBN());
        summary.setText(book.getSummary());
        cover.setImageBitmap(book.getBitmap());
    }
    
  //���ؼ�����
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
