package com.zutuanlu.db;

import java.util.ArrayList;
import java.util.List;

import com.zutuanlu.Relation;
import com.zutuanlu.User;
import com.zutuanlu.utils.EnumUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataHelper {
	public MySQLiteHelper mySQLiteHelper;
	public SQLiteDatabase db;
	
	public DataHelper(Context context){
		mySQLiteHelper = new MySQLiteHelper(context, EnumUtils.DB_NAME, null, EnumUtils.DB_VERSION);
		db = mySQLiteHelper.getWritableDatabase();
	}
	
	public void close(){
		db.close();
		mySQLiteHelper.close();
	}
	
	public User getUser(){
		User user = new User();
		Cursor c = db.query(EnumUtils.TABLE_USER, null, null, null, null, null, null);
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			user.special = c.getString(1);
			user.tName = c.getString(2);
			user.mName = c.getString(3);
		}
		c.close();
		return user;
	}
	
	public long insertUser (User user) {
		ContentValues values = new ContentValues();
		values.put(EnumUtils.USER_SP, user.special);
		values.put(EnumUtils.USER_TNAME, user.tName);
		values.put(EnumUtils.USER_MNAME, user.mName);
		long _uid = db.insert(EnumUtils.TABLE_USER, null, values);
		return _uid;
	}
	
	public int deleteUser(){
		int _uid = db.delete(EnumUtils.TABLE_USER, EnumUtils.USER_SP + "!=''", null);
		return _uid;
	}
	
	public long insertRelattion(Relation relation){
		ContentValues values = new ContentValues();
		values.put(EnumUtils.RELA_NO, relation.id);
		values.put(EnumUtils.RELA_NAME, relation.name);
		values.put(EnumUtils.RELA_PHONE, relation.phone);
		values.put(EnumUtils.RELA_LOCATION, relation.location);
		values.put(EnumUtils.RELA_REMARK, relation.remark);
		long uid = db.insert(EnumUtils.TABLE_RELA, null, values);
		return uid;
	}
	public boolean isExists(String id){
		Cursor c = db.query(EnumUtils.TABLE_RELA, null,EnumUtils.RELA_NO+"="+id , null, null, null, null);
		int count = c.getCount();
		c.close();
		if(count > 0){
			return true;
		}else{
			return false;
		} 
	}
	public long updateRelation(Relation relation, String id){
		ContentValues values = new ContentValues();
		values.put(EnumUtils.RELA_NO, relation.id);
		values.put(EnumUtils.RELA_NAME, relation.name);
		values.put(EnumUtils.RELA_PHONE, relation.phone);
		values.put(EnumUtils.RELA_LOCATION, relation.location);
		values.put(EnumUtils.RELA_REMARK, relation.remark);
		long uid = db.update(EnumUtils.TABLE_RELA, values, EnumUtils.RELA_NO+"=" + id, null);
		return uid;
	}
	
	public List<Relation> getRelations(){
		List<Relation> list = new ArrayList<Relation>();
		Cursor cursor = db.query(EnumUtils.TABLE_RELA, null, null, null, null, null, EnumUtils.RELA_NO +" ASC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			Relation relation = new Relation();
			relation.id = cursor.getString(1);
			relation.name = cursor.getString(2);
			relation.location = cursor.getString(3);
			relation.phone = cursor.getString(4);
			relation.remark = cursor.getString(5);
			list.add(relation);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
}
