package com.zutuanlu.utils;

import java.util.ArrayList;

import org.apache.http.client.CookieStore;

import com.zutuanlu.MyApplication;
import com.zutuanlu.R;
import com.zutuanlu.Relation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

public class EnumUtils {

	public static final String DB_NAME = "zutuanlu.db";
	public static final int DB_VERSION = 2;
	public static final String ID = "_id";
	public static final String TABLE_USER = "user";
	public static final String TABLE_RELA = "relations";
	public static final String USER_SP = "special";
	public static final String USER_TNAME = "teachername";
	public static final String USER_MNAME = "matename";
	public static final String RELA_NO = "relaNO";
	public static final String RELA_NAME = "name";
	public static final String RELA_PHONE = "telephone";
	public static final String RELA_LOCATION = "location";
	public static final String RELA_REMARK = "remark";
	public static final String RELA_INDEX = "rela_index";
	
	public static final String HOST = "http://ahpu.cnodejs.net/login.do";
	public static final String HOME = "http://ahpu.cnodejs.net/login";
	
	public static CookieStore cookieStore;
	public static ArrayList<String> questionList;
	public static Relation relation;
	private static boolean click = false ;
	
	public static void Toast(String string){
		Toast toast = Toast.makeText(MyApplication.getInstance(), string, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public static void callAction(Context context,String number){
		number = number.trim();
		if(number.length() > 11){
			click = true;
			selectNumber(context, number);
			return;
		} else{
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_CALL);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("tel:"+ number));
			context.startActivity(intent);
		}
	}
	
	public static void smsAction(Context context, String number){
		number = number.trim();
		if(number.length() > 11){
			selectNumber(context, number);
			return;
		} else {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse("smsto:"+ number));
			intent.putExtra("sms_body", context.getResources().getString(R.string.sms_body));
			context.startActivity(intent);	
		}
	}
	
	private static void selectNumber(final Context context,String string){
		final String arr[] = {string.substring(0, 11), string.substring(11, string.length())};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setItems(arr, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(click){
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_CALL);
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setData(Uri.parse("tel:"+ arr[which]));
					context.startActivity(intent);
				} else{
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_SENDTO);
					intent.setData(Uri.parse("smsto:"+ arr[which]));
					intent.putExtra("sms_body", context.getResources().getString(R.string.sms_body));
					context.startActivity(intent);
				}
			}
		});
		builder.create().show();
	}
	
	
	 public static boolean isNetworkAvailable()
	 {

	  ConnectivityManager cm = (ConnectivityManager)MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
	  if( cm == null )
		  return false;
	  NetworkInfo netinfo = cm.getActiveNetworkInfo();
	  if (netinfo == null ){
	   return false;
	  }
	  if(netinfo.isConnected()){
	   return true;
	  }
	  return false;
	 }
}
