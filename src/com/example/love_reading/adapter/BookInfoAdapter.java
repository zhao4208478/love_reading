package com.example.love_reading.adapter;

import java.util.ArrayList;

import com.example.love_reading.sql.ContentBean;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**   
 * 请在此处填写类的描述
 * 
 * @Project ScanBook
 * @Package com.scrawlor.scanbook.adapter
 * @Class BookInfoAdapter
 * @Date 2013-5-26 下午2:50:06
 * @author scrawlor
 * @version 
 * @since 
 */
public class BookInfoAdapter extends BaseAdapter{
	private ArrayList<ContentBean> arrayList;
	private LayoutInflater inflater;
	private TextView textView;
	private String title;
	private String time;
	private String content;
	
	public BookInfoAdapter(ArrayList<ContentBean> arrayList, Context context) {
		this.arrayList = arrayList;
		inflater = LayoutInflater.from(context);
	}
	/* @see android.widget.Adapter#getCount()
	 *getCount
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	/* @see android.widget.Adapter#getItem(int)
	 *getItem
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	/* @see android.widget.Adapter#getItemId(int)
	 *getItemId
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 *getView
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			    return convertView;
	}

}
