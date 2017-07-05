package com.dawnlightning.msmdebuger.dao;

import android.content.ContentValues;

import com.dawnlightning.msmdebuger.bean.Contact;
import com.dawnlightning.msmdebuger.bean.Phone;
import java.util.List;
import java.util.Map;

public interface PhoneDaoInface {
	public boolean addCache(Phone item);
	public boolean addAdmin(Contact item);
	public boolean deleteCache(String whereClause, String[] whereArgs);
	public boolean deleteAdmin(String whereClause, String[] whereArgs);
	public boolean updateCache(ContentValues values, String whereClause,
							   String[] whereArgs);
	public boolean updateAdmin(ContentValues values, String whereClause,
							   String[] whereArgs);

	public Map<String, String> viewCache(String selection,
										 String[] selectionArgs);

	public List<Map<String, String>> listCache(String selection,
											   String[] selectionArgs);
	public List<Map<String, String>> listAdmin(String selection,
											   String[] selectionArgs);

	public void clearFeedTable();
}
