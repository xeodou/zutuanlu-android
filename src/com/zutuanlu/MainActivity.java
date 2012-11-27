package com.zutuanlu;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zutuanlu.db.DataHelper;
import com.zutuanlu.http.HttpConfig;
import com.zutuanlu.http.HttpRouter;
import com.zutuanlu.http.NetworkUtils;
import com.zutuanlu.utils.EnumUtils;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Dialog dialog;
    private EditText sEditText, tEditText, mEditText;
    private TextView textView01, textView02, textView03;
    private Button commitBtn;
    private ProgressDialog _dialog;
    private final int MSG_ERROR = 0x001;
    private final int MSG_SUCCESS = 0x002;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false); 
//        _dialog = ProgressDialog.show(this, null,getResources().getString(R.string.loading));
//        _dialog.setCancelable(true);
		loadLayout();
    }
    
    Runnable layoutRunnable = new  Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
            Result result  = HttpRouter.getCookieStore();
            if(result.getCode() < 0 ){
            	sendMsg(MSG_ERROR, result.getMsg());
            	return;
            }
            if(EnumUtils.questionList.size() > 0){

            }
		}
    	
    };
    private void loadLayout(){
    	DataHelper db = new DataHelper(this);
    	User user = db.getUser();
    	db.close();
    	if(user.special == null){
    		if(NetworkUtils.networkType() == HttpConfig.TYPE_NET_WORK_DISABLED){
    			Toast.makeText(this, R.string.network_unable, Toast.LENGTH_SHORT).show();
    			return;
    		}
            setContentView(R.layout.login);
            sEditText = (EditText) findViewById(R.id.specialEdittext);
            tEditText = (EditText) findViewById(R.id.tnameEdittext);
            mEditText = (EditText) findViewById(R.id.mnameEdittext);
            textView01 = (TextView)findViewById(R.id.textView1);
            textView02 = (TextView) findViewById(R.id.textView2);
            textView03 = (TextView) findViewById(R.id.textView3);
        	textView01.setText("用户名:");
        	textView02.setText("密码:");
            sEditText.setFocusable(true);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            sEditText.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
					if (actionId == EditorInfo.IME_ACTION_NEXT) {
						tEditText.setFocusable(true);
					}
					return false;
				}
			});
            
            tEditText.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
					if (actionId == EditorInfo.IME_ACTION_NEXT) {
						mEditText.setFocusable(true);
					}
					return false;
				}
			});
            
            mEditText.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
					if (actionId == EditorInfo.IME_ACTION_DONE) {
			            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

					}
					return false;
				}
			});
            
            commitBtn = (Button) findViewById(R.id.commitbtn);
            if(!EnumUtils.isNetworkAvailable()){
            	commitBtn.setEnabled(false);
            	EnumUtils.Toast(getResources().getString(R.string.error_network));
            	return;
            }
            commitBtn.setOnClickListener(commitListener);
            
    	} else {
            if(!EnumUtils.isNetworkAvailable()){
            	EnumUtils.Toast(getResources().getString(R.string.offline));
            }
            showList();
    	}
    }
    
    private OnClickListener commitListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (sEditText.getText().length() <= 0 || tEditText.getText().length() <= 0) {
				sendMsg(MSG_ERROR, getIdString(R.string.error_not_null));
				return;
			}
			
			dialog = ProgressDialog.show(MainActivity.this, null , getResources().getString(R.string.loading));
			dialog.setCancelable(true);
			Thread thread = new Thread(runnable);
			thread.start();
		}
	};
    
    private void showList(){
        Intent intent = new Intent();
        intent.setClass(this, ListActivity.class);
        startActivity(intent);
        this.finish();
    }
    
    private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			User user = new User();
			user.special = sEditText.getText().toString();
			user.tName = tEditText.getText().toString();
//			user.mName = mEditText.getText().toString();
			if(HttpRouter.login(user)){
				sendMsg(MSG_SUCCESS, null);
			} else {
				sendMsg(MSG_ERROR, null);
			}
			
		}
	};
	
	private String getIdString(int id){
		return getResources().getString(id);
	}
	
	private void sendMsg(int code, String content){
		Message msg = new Message();
		msg.what = code;
		msg.obj = content;
		handler.sendMessage(msg);
	}
	
	private void toastMsg(String msg){
		EnumUtils.Toast(msg);
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_SUCCESS:
				showList();
				break;
			case MSG_ERROR:
				if(msg.obj != null){
					toastMsg(msg.obj.toString());
				}
			}
			dismissDialog();
		}
		
	};
	
	private void dismissDialog(){
		if(dialog != null){
			dialog.dismiss();
		}
		if(_dialog != null){
			_dialog.dismiss();
		}
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