package com.zutuanlu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.umeng.analytics.MobclickAgent;
import com.zutuanlu.db.DataHelper;
import com.zutuanlu.http.HttpRouter;
import com.zutuanlu.utils.EnumUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class ListActivity extends Activity {

	private ListView mListView;
	private List<Relation> list;
	private Dialog dialog;
	private RelationAdapter adapter;
	private ImageButton refreshBtn;
	ArrayList<HashMap<String, Object>> relations;
	private boolean isSyc = false;
	private int menu = Menu.FIRST + 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mListView = (ListView) findViewById(R.id.relationlist);
		relations = new ArrayList<HashMap<String, Object>>();
		
		adapter = new RelationAdapter(this, relations, R.layout.main_list, new String[]{"name", "phone"}, new int[]{R.id.username, R.id.telephone});
		mListView.setAdapter(adapter);
		dialog = ProgressDialog.show(this, null, getResources().getString(R.string.loading));
		Thread thread = new Thread(runnable);
		thread.run();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				EnumUtils.relation = list.get(position);
				intent.setClass(ListActivity.this, ProfileActivity.class);
				startActivity(intent);
			}
		});
		
		refreshBtn = (ImageButton) findViewById(R.id.refresh_btn);
		refreshBtn.setOnClickListener(sycClickListener);
	}
 
	private OnClickListener sycClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sycContact();
		}
	};
	
	private void sycContact(){
		isSyc = true;
		dialog = ProgressDialog.show(ListActivity.this, null, getResources().getString(R.string.loading));
		dialog.setCancelable(true);
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			DataHelper db = new DataHelper(ListActivity.this);
			if(isSyc){
				User user = db.getUser();
				if(!HttpRouter.login(user)){
					Message message = new Message();
					message.what = 2;
					handler.sendMessage(message);
					return;
				}
			}
			list = db.getRelations();
			db.close();
			if(relations.size() > 0) relations.clear();
			for (Relation relation : list) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", relation.name);
				String phone = relation.phone.substring(0, 11);
				if(relation.phone.length() > 11 && relation.phone.substring(11, relation.phone.length()) != null){
					phone = phone + "\n" + relation.phone.substring(11, relation.phone.length());
				}
				map.put("phone", phone);
				relations.add(map);
			}			
			
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				adapter.notifyDataSetChanged();
				EnumUtils.Toast(getResources().getString(R.string.update_ok));
			}
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_menu, menu);
		menu.add(0, this.menu, 0, R.string.logou).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == this.menu) {
			ProgressDialog pdialog = ProgressDialog.show(this, null, getResources().getString(R.string.deleting));
			DataHelper db = new DataHelper(this);
			db.deleteUser();
			db.close();
			pdialog.dismiss();
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			finish();
		} else if(item.getItemId() == R.id.refresh){
			sycContact();
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
