package com.dawnlightning.msmdebuger.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.dawnlightning.msmdebuger.db.SQLHelper;

import java.io.File;

/**
 * 作者：Administrator on 2016/10/22 23:57
 * 邮箱：823894716@qq.com
 */
public class MyApp extends Application {
    private static MyApp mAppApplication;
    private SQLHelper sqlHelper;
    private final  static String  SEND_CONTEXT="context";

    public static  void setContext(String context){
        SharedPreferences sp=mAppApplication.getSharedPreferences(SEND_CONTEXT, Context.MODE_PRIVATE);
        sp.edit().putString(SEND_CONTEXT, context).commit();
    }
    public static String getContext(){
        SharedPreferences sp=mAppApplication.getSharedPreferences(SEND_CONTEXT, Context.MODE_PRIVATE);
        return sp.getString(SEND_CONTEXT,"");
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mAppApplication = this;
    }

    /** 获取Application */
    public static MyApp getApp() {
        return mAppApplication;
    }

    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mAppApplication);
        return sqlHelper;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
        //整体摧毁的时候调用这个方法
    }


}
