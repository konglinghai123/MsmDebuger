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
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.dawnlightning.msmdebuger.utils.HanziToPinyin;
import com.dawnlightning.msmdebuger.adapter.ContactAdapter;
import com.dawnlightning.msmdebuger.bean.Contact;
import com.dawnlightning.msmdebuger.widget.SideBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户联系人列表
 *
 * @author kymjs (http://www.kymjs.com/) on 7/24/15.
 */
public class ContactActivity extends Activity implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {
    public final  static int RESULTCODE=2;
    private ListView mListView;
    private TextView mFooterView;
    private ArrayList<Contact> datas = new ArrayList<>();
    private ContactAdapter mAdapter;

    private ProgressDialog proDialog;
    private Toolbar toolbar;
    private ImageButton sure;
    private Map<Integer, Contact> contactIdMap = null;

    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
    /**
     * 初始化数据库查询参数
     */
    private void init() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");

    }
    /**
     *
     * @author Administrator
     *
     */
    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                contactIdMap = new HashMap<Integer, Contact>();
                datas = new ArrayList<Contact>();
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    String sortKey = cursor.getString(3);
                    int contactId = cursor.getInt(4);
                    Long photoId = cursor.getLong(5);
                    String lookUpKey = cursor.getString(6);

                    if (contactIdMap.containsKey(contactId)) {
                        // 无操作
                    } else {
                        // 创建联系人对象
                        Contact contact = new Contact();
                        contact.setName(name);
                        contact.setNumber(number);
                        contact.setId(contactId);
                        contact.setPinyin(HanziToPinyin.getPinYin(name));
                        datas.add(contact);

                        contactIdMap.put(contactId, contact);
                    }
                }
                if (datas.size() > 0) {
                    updateList();
                }
            }
            super.onQueryComplete(token, cookie, cursor);
        }

    }
    private void updateList(){
        if (proDialog != null) {
            proDialog.dismiss();
        }
        mAdapter = new ContactAdapter(datas,ContactActivity.this);
        mListView.setAdapter(mAdapter);
        mFooterView.setText(datas.size() + "位联系人");
        //mAdapter.notifyDataSetChanged();
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
                mIntent.putExtra("phonelist",(Serializable) mAdapter.getSelectDatas());
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
        // 实例化
        asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
        init();
        proDialog = ProgressDialog.show(this, "loading","loading", true, true);

    }






    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        } else if (s.contains("#")) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ArrayList<Contact> temp = new ArrayList<>(datas);
        for (Contact data : datas) {
            if (data.getName().contains(s) || data.getPinyin().contains(s)) {
            } else {
                temp.remove(data);
            }
        }
        if (mAdapter != null) {
           mAdapter.setDatas(temp);
           mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
