package com.dawnlightning.msmdebuger.dao;

import android.content.ContentValues;
import android.database.SQLException;

import com.dawnlightning.msmdebuger.bean.Contact;
import com.dawnlightning.msmdebuger.bean.Phone;
import com.dawnlightning.msmdebuger.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PhoneManage {
	public static PhoneManage phoneManage;

	private PhoneDao phoneDao;



	private PhoneManage(SQLHelper paramDBHelper) throws SQLException {
		if (phoneDao == null)
			phoneDao = new PhoneDao(paramDBHelper.getContext());

		return;
	}

	/**
	 * 初始化频道管理类
	 * @paramparamDBHelper
	 * @throws SQLException
	 */
	public static PhoneManage getManage(SQLHelper dbHelper)throws SQLException {
		if (phoneManage == null)
			phoneManage = new PhoneManage(dbHelper);
		return phoneManage;
	}

	/**
	 * 获取用户保存的电话号码，注册码，地址
	 *
	 */
	public ArrayList<Phone> getUserPhone() {
		Object cacheList = phoneDao.listCache(null,null);
		if (cacheList != null && !((ArrayList) cacheList).isEmpty()) {
			ArrayList<Map<String, String>> maplist = (ArrayList) cacheList;
			int count = maplist.size();
			ArrayList<Phone> list = new ArrayList<Phone>();
			for (int i = 0; i < count; i++) {
				Phone navigate = new Phone();
				navigate.setNumber(maplist.get(i).get(SQLHelper.PHONE));
				navigate.setAddress(maplist.get(i).get(SQLHelper.ADDRESS));
				navigate.setCode(maplist.get(i).get(SQLHelper.CODE));
				list.add(navigate);
			}
			return list;
		}else{
			return new ArrayList<>();
		}


	}
	public ArrayList<Contact> getAdmin(){
		Object cacheList = phoneDao.listAdmin(null,null);
		if (cacheList != null && !((ArrayList) cacheList).isEmpty()) {
			ArrayList<Map<String, String>> maplist = (ArrayList) cacheList;
			int count = maplist.size();
			ArrayList<Contact> list = new ArrayList<Contact>();
			for (int i = 0; i < count; i++) {
				Contact navigate = new Contact();
				navigate.setNumber(maplist.get(i).get(SQLHelper.PHONE));
				navigate.setName(maplist.get(i).get(SQLHelper.REMARK));
				list.add(navigate);
			}
			return list;
		}else{
			return new ArrayList<>();
		}
	}
	public void deleteAdmin(Contact contact){
		phoneDao.deleteAdmin(SQLHelper.PHONE+"=?",new String[]{contact.getNumber()});
	}
	public void deletePhone(Phone phone){
		phoneDao.deleteCache(SQLHelper.PHONE+"=?",new String[]{phone.getNumber()});
	}


	public void updateAdmin(Contact contact){
		ContentValues cv = new ContentValues();
		cv.put(SQLHelper.REMARK,contact.getName());
		phoneDao.updateAdmin(cv,SQLHelper.PHONE+"=?",new String[]{contact.getNumber()});
	}
	public void updatePhone(Phone phone){
		ContentValues cv = new ContentValues();
		cv.put(SQLHelper.CODE,phone.getCode());
		cv.put(SQLHelper.ADDRESS,phone.getAddress());
		phoneDao.updateCache(cv,SQLHelper.PHONE+"=?",new String[]{phone.getNumber()});
	}
	public void insertAdmin(Contact contact){
		phoneDao.addAdmin(contact);
	}
	public void insertPhone(Phone phone){
		phoneDao.addCache(phone);
	}
}
