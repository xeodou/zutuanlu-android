package com.zutuanlu;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.umeng.analytics.MobclickAgent;
import com.zutuanlu.utils.EnumUtils;

import android.R.anim;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends Activity {

	private TextView nameTextView,phoneTextView, locationTextView, remarkTextView;
	private ImageButton telButton, smsButton;
	private Relation relation;
	private Button send_wx;
    private static final String APP_ID = "wxe86107960513ec97";
    private IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		regToWx();
		setContentView(R.layout.personinfo);
		addActionBar();
		relation = EnumUtils.relation;
		
		nameTextView = (TextView) findViewById(R.id.name);
		phoneTextView = (TextView) findViewById(R.id.phone);
		locationTextView = (TextView) findViewById(R.id.location);
		remarkTextView = (TextView) findViewById(R.id.remark);
		send_wx = (Button) findViewById(R.id.send_wx);
		
		nameTextView.setText(relation.name);
		phoneTextView.setText(relation.phone);
		locationTextView.setText(relation.location);
		remarkTextView.setText(relation.remark);
		
		
		telButton = (ImageButton) findViewById(R.id.callaction);
		telButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				telTo();
			}
		});
		smsButton = (ImageButton) findViewById(R.id.msgaction);
		smsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				smsTo();
			}
		});
		
		send_wx.setOnClickListener(wx_clickListener);
		if(isHighLevel()){
			telButton.setVisibility(View.GONE);
			smsButton.setVisibility(View.GONE);
		}
	}
	@TargetApi(11)
	private void addActionBar(){
		if(isHighLevel()){
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
	
	private boolean isHighLevel(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return true;
		}
		return false;
	}
	
	private void telTo(){
		EnumUtils.callAction(ProfileActivity.this, relation.phone);
	}
	
	private void smsTo(){
		EnumUtils.smsAction(ProfileActivity.this, relation.phone);
	}
	
    private void regToWx(){
    	api = WXAPIFactory.createWXAPI(this, APP_ID, true);
    	api.registerApp(APP_ID);
    }
    
	
	private OnClickListener wx_clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sendToWx();
		}
	};
	
	private void sendToWx(){
		WXTextObject textObject = new WXTextObject();
		textObject.text = relation.name +"phone NO. is "+relation.phone;
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObject;
		msg.description = "test";
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.message = msg;
		req.transaction = String.valueOf(System.currentTimeMillis());
		api.sendReq(req);
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.info_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
            finish();
			break;

		case R.id.menu_tel:
			telTo();
			break;
		case R.id.menu_sms:
			smsTo();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
