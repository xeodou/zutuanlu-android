package com.zutuanlu.db;

import com.zutuanlu.utils.EnumUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public MySQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + 
				EnumUtils.TABLE_USER +"("+
				EnumUtils.ID + " Interger primary key,"+
				EnumUtils.USER_SP + " verchar,"+
				EnumUtils.USER_TNAME + " verchar,"+
				EnumUtils.USER_MNAME + " verchar"+
				");"
				);
		db.execSQL("CREATE TABLE IF NOT EXISTS " + 
				EnumUtils.TABLE_RELA + "("+
				EnumUtils.ID + " Interger primary key,"+
				EnumUtils.RELA_NO + " verchar,"+
				EnumUtils.RELA_NAME + " verchar,"+
				EnumUtils.RELA_LOCATION + " verchar,"+
				EnumUtils.RELA_PHONE + " verchar,"+
				EnumUtils.RELA_REMARK + " verchar"+
				");"
				);
		db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS " + 
				EnumUtils.RELA_INDEX + " ON " + EnumUtils.TABLE_RELA+"(" +
				EnumUtils.ID + "," + 
				EnumUtils.RELA_NO + ");"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP IF IS EXITS " + EnumUtils.TABLE_RELA);
		db.execSQL("DROP IF IS EXITS " + EnumUtils.TABLE_USER);
		onCreate(db);
	}

}
