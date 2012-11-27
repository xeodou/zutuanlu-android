package com.zutuanlu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zutuanlu.utils.EnumUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class RelationAdapter extends BaseAdapter {

	private class ViewHolder{
		TextView nameTextView;
		TextView phoneTextView;
		ImageButton callImageButton;
	}
	
	private Context mContext;
	private ViewHolder viewHolder;
	private ArrayList<HashMap<String, Object>> arrayList;
	private String[] keyString;
	private String name,phone;
	private LayoutInflater layoutInflater;
	private int[] viewID;
	public RelationAdapter(Context context, ArrayList<HashMap<String, Object>> data,
			int resource, String[] from, int[] to) {
		// TODO Auto-generated constructor stub
		mContext = context;
		arrayList = data;
		layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		keyString = from;
		viewID = to;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.indexOf(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@SuppressLint("ParserError")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView != null) {
			viewHolder = (ViewHolder)convertView.getTag();
		} else {
			convertView = layoutInflater.inflate(R.layout.main_list, null);
			viewHolder = new ViewHolder();
			viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.username);
			viewHolder.phoneTextView = (TextView) convertView.findViewById(R.id.telephone);
			viewHolder.callImageButton = (ImageButton) convertView.findViewById(R.id.call);
			convertView.setTag(viewHolder);
		}
		HashMap<String, Object> map = arrayList.get(position);
		if (arrayList != null) {
			name = (String)map.get(keyString[0]);
			phone = (String)map.get(keyString[1]);
			viewHolder.nameTextView.setText(name);
			viewHolder.phoneTextView.setText(phone);
			viewHolder.callImageButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					HashMap<String, Object> map = arrayList.get(position);
					String str = (String)map.get(keyString[1]);
					EnumUtils.callAction(mContext, str);
				}
			});
		}
		return convertView;
	}
	
}
