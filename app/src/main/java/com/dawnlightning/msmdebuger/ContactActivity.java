/*
 * Copyright (c) 2015 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dawnlightning.msmdebuger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.dawnlightning.msmdebuger.Utils.HanziToPinyin;
import com.dawnlightning.msmdebuger.adapter.ContactAdapter;
import com.dawnlightning.msmdebuger.bean.Contact;
import com.dawnlightning.msmdebuger.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户联系人列表
 *
 * @author kymjs (http://www.kymjs.com/) on 7/24/15.
 */
public class ContactActivity extends Activity implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {
    public final  static int RESULTCODE=1;
    private ListView mListView;
    private TextView mFooterView;
    private ArrayList<Contact> datas = new ArrayList<>();
    private ContactAdapter mAdapter;
    private final int UPDATE_LIST=1;
    private ProgressDialog proDialog;
    private Toolbar toolbar;
    Thread getcontacts;
    private ImageButton sure;
    class GetContacts implements Runnable{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            String[] projection = new String[] {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_ID
            };
            String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
            String[] selectionArgs = null;
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
            Cursor cursor=managedQuery(uri, projection, selection, selectionArgs, sortOrder);
            Cursor phonecur = null;

            while (cursor.moveToNext()){
                Contact data = new Contact();
                // 取得联系人名字
                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                String name = cursor.getString(nameFieldColumnIndex);
                // 取得联系人ID
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                phonecur = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "  + contactId, null, null);
                // 取得电话号码(可能存在多个号码)
                while (phonecur.moveToNext()){
                    String strPhoneNumber = phonecur.getString(phonecur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    if(strPhoneNumber.length()>4)
//                        contactsList.add("18610011001"+"\n测试");
                    //contactsList.add(strPhoneNumber+"\n"+name+"");
                    data.setNumber(strPhoneNumber.replace(" ",""));

                }

                data.setName(name);
                data.setId(Integer.parseInt(contactId));
                data.setPinyin(HanziToPinyin.getPinYin(data.getName()));
                datas.add(data);
            }
            if(phonecur!=null) {
                phonecur.close();
            }
            cursor.close();
            Message msg1=new Message();
            msg1.what=UPDATE_LIST;
            updateListHandler.sendMessage(msg1);
        }
    }
    Handler updateListHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case UPDATE_LIST:
                    if (proDialog != null) {
                        proDialog.dismiss();
                    }
                    updateList();
            }
        }
    };
    private void updateList(){
        mFooterView.setText(datas.size() + "位联系人");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        SideBar mSideBar = (SideBar) findViewById(R.id.school_friend_sidrbar);
        TextView mDialog = (TextView) findViewById(R.id.school_friend_dialog);
        EditText mSearchInput =(EditText)findViewById(R.id.school_friend_member_search_input);
        mListView= (ListView) findViewById(R.id.school_friend_member);
        toolbar=(Toolbar)findViewById(R.id.select_toolbar);
        toolbar.setTitle("选择联系人");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sure=(ImageButton) toolbar.findViewById(R.id.sure);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                ArrayList<String> list=new ArrayList<String>();
                for(int i=0;i< mAdapter.getSelectDatas().size();i++){
                    list.add(mAdapter.getSelectDatas().get(i).getNumber());
                }
                mIntent.putStringArrayListExtra("phonelist",list);
                setResult(RESULTCODE, mIntent);
                finish();
            }
        });
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);

        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(ContactActivity.this, R.layout.item_list_contact_count, null);
        mListView.addFooterView(mFooterView);
        getcontacts=new Thread(new GetContacts());
        getcontacts.start();
        proDialog = ProgressDialog.show(this, "loading","loading", true, true);
        mAdapter = new ContactAdapter(datas,ContactActivity.this);
        mListView.setAdapter(mAdapter);
    }






    @Override
    public void onTouchingLetterChanged(String s) {
//        int position = 0;
//        // 该字母首次出现的位置
//        if (mAdapter != null) {
//            position = mAdapter.getPositionForSection(s.charAt(0));
//        }
//        if (position != -1) {
//            mListView.setSelection(position);
//        } else if (s.contains("#")) {
//            mListView.setSelection(0);
//        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        ArrayList<Contact> temp = new ArrayList<>(datas);
//        for (Contact data : datas) {
//            if (data.getName().contains(s) || data.getPinyin().contains(s)) {
//            } else {
//                temp.remove(data);
//            }
//        }
//        if (mAdapter != null) {
////            mAdapter.setDatas(temp);
////            mAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
