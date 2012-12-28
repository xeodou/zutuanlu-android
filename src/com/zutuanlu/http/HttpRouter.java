package com.zutuanlu.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zutuanlu.ListActivity;
import com.zutuanlu.MyApplication;
import com.zutuanlu.R;
import com.zutuanlu.Relation;
import com.zutuanlu.Result;
import com.zutuanlu.User;
import com.zutuanlu.db.DataHelper;
import com.zutuanlu.utils.EnumUtils;

@SuppressLint("ParserError")
public class HttpRouter {
	
	private static String csrf;
	
	public static boolean login(User user){
		BasicHttpContext mHttpContext = new BasicHttpContext();
		CookieStore mCookieStore = EnumUtils.cookieStore;
		mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
		try {
			HttpPost post = new HttpPost(EnumUtils.HOST);
			post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//			String[] array = {user.special, user.tName, user.mName};
			nameValuePairs.add(new BasicNameValuePair("name", user.special));
			nameValuePairs.add(new BasicNameValuePair("pass", user.tName));
			nameValuePairs.add(new BasicNameValuePair("_csrf", getCookieStore().getMsg()));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			HttpResponse response = HttpManager.execute(post, mHttpContext);
			if(response.getStatusLine().getStatusCode() == 200){
				String doc = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				Pattern pattern = Pattern.compile("<h1 style=\"font-weight:400;\">(.*)</h1>"); 
				Matcher matcher = pattern.matcher(doc);
				if(matcher.find() && matcher.group(1) != null){
					Looper.prepare();
					EnumUtils.Toast(MyApplication.getInstance().getResources().getString(R.string.error_msg) + matcher.group(1));
					Looper.loop();
					return false;
				} else if(matcher.find() && matcher.group(1) == null) {
					pattern = Pattern.compile("<tr>(.*)</tr>");
					matcher = pattern.matcher(doc);
					if (matcher.find()) {
						Log.i("matcher", matcher.group());
					}
				}
			} else if(response.getStatusLine().getStatusCode() == 302){
				mCookieStore = HttpManager.getCookieStore();
				mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
				HttpGet get = new HttpGet("http://ahpu.cnodejs.net");
				response = HttpManager.execute(get, mHttpContext);
				if (response.getStatusLine().getStatusCode() == 200) {
					
					DataHelper db = new DataHelper(MyApplication.getInstance());
					db.insertUser(user);
					
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					StringBuilder sb = new StringBuilder();
					String doc = null;
					while((doc = reader.readLine()) != null){
						sb.append(doc+"\n");
					}
					is.close();
					doc = sb.toString();
					Pattern pattern = Pattern.compile("<td>([\\s\\S\\r\\n\\.]*?)</td>");
					Matcher matcher = pattern.matcher(doc);
					ArrayList<String> array = new ArrayList<String>();
					
					while (matcher.find()) {
						array.add(matcher.group(1));
					}
					
					for(int i = 0 ; i < array.size(); i += 5){
						Relation relation = new Relation();
						relation.id = array.get(i);
						relation.name = array.get(i + 1);
						
						String regEx = "[^0-9]";
				        Pattern p = Pattern.compile(regEx);
				        Matcher m = p.matcher(array.get(i + 2));
				        relation.phone = m.replaceAll("").trim();
				        
				        p = Pattern.compile("<[aA]\\s*(href=[^>]+)>(.*?)</[aA]>");
				        m = p.matcher(array.get(i + 3));
				        if(m.find()){
				        	relation.remark = m.group(2);
				        }
				        
						relation.location = array.get(i+4);
						if(!db.isExists(relation.id)){
							db.insertRelattion(relation);
						} else{
							db.updateRelation(relation,relation.id);
						}
					}
					db.close();
					return true;
				}
			} else {
				Looper.prepare();
				EnumUtils.Toast(MyApplication.getInstance().getResources().getString(R.string.error_msg) + EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
				Looper.loop();
				return false;
			}
			Log.i("LOG", response.getStatusLine().getStatusCode() + "");
//			Log.i("entity", EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
//	private static String getCSRF(){
//		try {
//			
//			Document doc = Jsoup.connect(EnumUtils.HOME).ignoreContentType(true)
//					.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5")
//					.timeout(20 * 1000).get();
//			
//			Elements elements = doc.getElementsByTag("input");
//			for(Element element : elements){
//				if(element.attr("name").contentEquals("_csrf")){
//					String value = element.attr("value");
//					if(value != null){
//						return value;
//					}
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
	
	private static Result getCookieStore(){
		HttpGet get = new HttpGet(EnumUtils.HOME);
		Result result = new Result();
		try {
			HttpResponse response = HttpManager.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String doc = null;
				while((doc = reader.readLine()) != null){
					sb.append(doc+"\n");
				}
				is.close();
				doc = sb.toString();
				Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"_csrf\" value=\"(.+)\" />");
				Matcher matcher = pattern.matcher(doc);
				if(matcher.find()) {
				     csrf = matcher.group(1);
				} 
				pattern = Pattern.compile("<span class=\"help-block\">(.*)</span>");
				matcher = pattern.matcher(doc);
				ArrayList<String> list = new ArrayList<String>();
				while(matcher.find()){
					list.add(matcher.group(1));
					Log.i("---", matcher.group(1));
				}
				EnumUtils.questionList = list;
				EnumUtils.cookieStore = HttpManager.getCookieStore();
				result.setResult(0, csrf);
				return result;
			}else{
				result.setResult(-1, "web server error!please check & restart !");
				return result;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result.setResult(-2, e.getMessage());
			return result;
		}
	}
}
