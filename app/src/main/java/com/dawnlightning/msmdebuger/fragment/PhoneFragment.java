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
import android.widget.EditText;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.ContactActivity;
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
    EditText addPhone;
    @Bind(R.id.add)
    TextView add;
    @Bind(R.id.select)
    TextView select;
    @Bind(R.id.listphone)
    ExpandListView listphone;
    @Bind(R.id.sure)
    Button sure;
    public final static int REQUEST_CODE_FROM_FRAGMENT=5;
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

//        addPhone.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                startActivityForResult(intent, 1);
//                return false;
//            }
//        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.add,R.id.sure,R.id.select})
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
                case R.id.select:
                    Intent i=new Intent();
                    i.setClass(getActivity(),ContactActivity.class);
                    startActivityForResult(i,REQUEST_CODE_FROM_FRAGMENT);
                    break;
            }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode)
        {
            case (2) :
            {
                if (requestCode==REQUEST_CODE_FROM_FRAGMENT){
                    ArrayList<String> list=data.getStringArrayListExtra("phonelist");
                    if (list!=null){
                        if (list.size()<=5){
                            for (int i=0;i<list.size();i++){
                                if (Mobile.isMobileNO(list.get(i))){
                                    adapter.add(list.get(i));
                                }

                            }
                        }else{
                            for (int i=0;i<5;i++){
                                if (Mobile.isMobileNO(list.get(i))){
                                    adapter.add(list.get(i));
                                }
                            }
                            mainActivity.TosatShow("所选号码不能超过5");
                        }

                        adapter.notifyDataSetChanged();
                    }

                }

                break;
            }

        }
    }



}
