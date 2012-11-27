package com.zutuanlu.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;

public class HttpManager {

	private static final DefaultHttpClient mClient;
	static{
		
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		HttpProtocolParams.setUserAgent(params, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5");
		
		ConnPerRoute connPerRoute = new ConnPerRouteBean(12);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
		ConnManagerParams.setMaxTotalConnections(params, 20);
		
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		
		HttpClientParams.setRedirecting(params, false);
		
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 443));
		
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		mClient = new DefaultHttpClient(conMgr, params);
	}
	
	private HttpManager(){
		
	}
	
	public static HttpResponse execute(HttpHead head) throws IOException{
		return mClient.execute(head);
	}
	
	public static HttpResponse execute(HttpHost host, HttpGet get) throws ClientProtocolException, IOException{
		return mClient.execute(host, get);
	}
	
	public static HttpResponse execute(HttpGet get) throws IOException{
		return mClient.execute(get);
	}
	
	public static HttpResponse execute(HttpPost post) throws IOException{
		return mClient.execute(post);
	}
	
	public static HttpResponse execute(HttpGet get, HttpContext httpContext) throws IOException{
		return mClient.execute(get, httpContext);
	}
	
	public static HttpResponse execute(HttpPost post, HttpContext httpContext) throws IOException{
		return mClient.execute(post, httpContext);
	}
	
	public static synchronized CookieStore getCookieStore(){
		return mClient.getCookieStore();
	}
}
