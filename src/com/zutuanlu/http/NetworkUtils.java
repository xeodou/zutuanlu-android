package com.zutuanlu.http;

import com.zutuanlu.MyApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	public static int networkType(){
		ConnectivityManager connectivityManager = (ConnectivityManager)MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNet = connectivityManager.getActiveNetworkInfo();
		if(activeNet == null) {
			HttpConfig.CURRENT_NET_WORK_TYPE = HttpConfig.TYPE_NET_WORK_DISABLED;
			return HttpConfig.CURRENT_NET_WORK_TYPE;
		}
		int netType = activeNet.getType();
		if (netType == ConnectivityManager.TYPE_WIFI) {
			HttpConfig.CURRENT_NET_WORK_TYPE = HttpConfig.TYPE_WIFI;
		} else if(netType == ConnectivityManager.TYPE_MOBILE) {
			String type = activeNet.getExtraInfo().toLowerCase();
            if(type.equals("cmwap") || type.equals("uniwap") || type.equals("3gwap")){
            	HttpConfig.CURRENT_NET_WORK_TYPE = HttpConfig.TYPE_CM_CU_WAP;
            } else if(type.equals("ctwap") ) {
            	HttpConfig.CURRENT_NET_WORK_TYPE = HttpConfig.TYPE_CT_WAP;
            } else {
            	HttpConfig.CURRENT_NET_WORK_TYPE = HttpConfig.TYPE_OTHER_NET;
            }
		} 
		return HttpConfig.CURRENT_NET_WORK_TYPE;
	}
}
