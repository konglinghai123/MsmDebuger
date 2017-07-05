package com.dawnlightning.msmdebuger.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "msgdebuger.db";// 数据库名称
	public static final int VERSION = 3;
	
	public static final String TABLE_CHANNEL = "msgdebuger_phone";//数据表
	public static final String TABLE_ADMIN = "msgdebuger_phone_admin";//数据表

	public static final String ID = "id";//
	public static final String PHONE = "phone";
	public static final String ADDRESS = "address";
	public static final String CODE = "code";
	public static final String REMARK="remark";
	private Context context;
	public SQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext(){
		return context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建数据库后，对数据库的操作
		String sql = "create table if not exists "+TABLE_CHANNEL +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PHONE + " TEXT , " +
				ADDRESS + " TEXT , " +
				CODE+ " TEXT"+
				")";
		db.execSQL(sql);
		// TODO 创建数据库后，对数据库的操作
		String admin = "create table if not exists "+TABLE_ADMIN +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PHONE + " TEXT , " +
				REMARK+ " TEXT"+
				")";
		db.execSQL(admin);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作
		onCreate(db);
	}

}
