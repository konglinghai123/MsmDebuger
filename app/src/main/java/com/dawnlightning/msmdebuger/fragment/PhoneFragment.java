package com.dawnlightning.msmdebuger.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dawnlightning.msmdebuger.MainActivity;
import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.Utils.Mobile;
import com.dawnlightning.msmdebuger.adapter.PhoneAdapter;
import com.dawnlightning.msmdebuger.command.InstructionsBuilder;
import com.dawnlightning.msmdebuger.command.MsgSender;
import com.dawnlightning.msmdebuger.widget.EditUsename;
import com.dawnlightning.msmdebuger.widget.ExpandListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Administrator on 2016/10/4 15:29
 * 邮箱：823894716@qq.com
 */
public class PhoneFragment extends Fragment {
    @Bind(R.id.add_phone)
    EditUsename addPhone;
    @Bind(R.id.add)
    Button add;
    @Bind(R.id.listphone)
    ExpandListView listphone;
    @Bind(R.id.sure)
    Button sure;

    PhoneAdapter adapter;

    private MsgSender sender;
    private InstructionsBuilder builder;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        sender=new MsgSender(activity);
        builder=new InstructionsBuilder();
        mainActivity=(MainActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragement_phone, container, false);
        ButterKnife.bind(this, view);
        adapter = new PhoneAdapter(getActivity());
        listphone.setAdapter(adapter);

        addPhone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
                return false;
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.add,R.id.sure})
    public void onClick(View view) {

            switch (view.getId()) {
                case R.id.add:
                    if (!TextUtils.isEmpty(addPhone.getText().toString())){
                        if (adapter.getCount()<5){
                            adapter.add(addPhone.getText().toString());
                            adapter.notifyDataSetChanged();
                            addPhone.setText("");
                        }else{
                            mainActivity.TosatShow("当前超出5个号码");
                        }

                    }else{
                        mainActivity.TosatShow("请添加号码");
                    }

                    break;
                case R.id.sure:
                    ArrayList<String> phone=mainActivity.phonelist;
                    sender.send(phone,builder.SetPhone(adapter.getlist()));
                    break;
            }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {

            case (1) :
            {

                if (resultCode == Activity.RESULT_OK)
                {

                    Uri contactData = data.getData();

                    Cursor c = mainActivity.managedQuery(contactData, null, null, null, null);

                    c.moveToFirst();

                    String phoneNum=this.getContactPhone(c);
                    addPhone.setText(phoneNum.replaceAll(" ",""));

                }

                break;

            }

        }
    }

    //获取联系人电话
    private String getContactPhone(Cursor cursor)
    {

        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult="";
        //System.out.print(phoneNum);
        if (phoneNum > 0)
        {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = mainActivity.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId,
                    null, null);
            //int phoneCount = phones.getCount();
            //allPhoneNum = new ArrayList<String>(phoneCount);
            if (phones.moveToFirst())
            {
                // 遍历所有的电话号码
                for (;!phones.isAfterLast();phones.moveToNext())
                {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch(phone_type)
                    {
                        case 2:
                            phoneResult=phoneNumber;
                            break;
                    }
                    //allPhoneNum.add(phoneNumber);
                }
                if (!phones.isClosed())
                {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }

}
