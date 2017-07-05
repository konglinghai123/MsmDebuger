package com.dawnlightning.msmdebuger.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.ContactActivity;
import com.dawnlightning.msmdebuger.MainActivity;
import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.adapter.AdminListAdapter;
import com.dawnlightning.msmdebuger.adapter.PhoneListAdapter;
import com.dawnlightning.msmdebuger.base.MyApp;
import com.dawnlightning.msmdebuger.bean.Contact;
import com.dawnlightning.msmdebuger.bean.Phone;
import com.dawnlightning.msmdebuger.command.InstructionsBuilder;
import com.dawnlightning.msmdebuger.command.MsgSender;
import com.dawnlightning.msmdebuger.dao.PhoneManage;
import com.dawnlightning.msmdebuger.utils.Mobile;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

/**
 * 作者：Administrator on 2016/10/23 04:04
 * 邮箱：823894716@qq.com
 */
public class AdminPhoneListFragment extends Fragment {


    ArrayList<Phone> list = new ArrayList<>();
    ArrayList<Contact> adminlist = new ArrayList<>();
    public final static int SELECT_PHONE_FROM_FRAGMENT_LIST_ADMIN = 100;
    @Bind(R.id.admin)
    EditText admin;
    @Bind(R.id.add)
    TextView add;
    @Bind(R.id.select)
    TextView select;
    @Bind(R.id.recyclerView_admin)
    RecyclerView recyclerViewAdmin;
    @Bind(R.id.authority)
    Button authority;
    @Bind(R.id.tag_group)
    TagGroup tagGroup;
    private MsgSender sender;
    private InstructionsBuilder builder;
    private MainActivity mainActivity;
    private AdminListAdapter adapter;
    private PhoneManage manage = PhoneManage.getManage(MyApp.getApp().getSQLHelper());

    @Override
    public void onAttach(Activity activity) {
        sender = new MsgSender(activity);
        builder = new InstructionsBuilder();
        mainActivity = (MainActivity) activity;
        list = manage.getUserPhone();
        adminlist=manage.getAdmin();
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_admin, container, false);
        ButterKnife.bind(this, view);
        initTag();
        adapter=new AdminListAdapter(adminlist, getActivity(), new AdminListAdapter.OnClickListener() {
            @Override
            public void delete(Contact contact) {
                adapter.remove(contact);
                adapter.notifyDataSetChanged();
                manage.deleteAdmin(contact);
            }

            @Override
            public void remark(Contact contact,String name) {
                contact.setName(name);
                manage.updateAdmin(contact);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdmin.setLayoutManager(layoutManager);
        recyclerViewAdmin.setAdapter(adapter);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.add, R.id.select, R.id.authority})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                String phonenumber=admin.getText().toString();
                if (!TextUtils.isEmpty(phonenumber)&& Mobile.isMobileNO(phonenumber)){
                   if (adapter.getItemCount()<5){
                        Contact phone=new Contact();
                        phone.setNumber(phonenumber);
                        phone.setName("");
                        phone.setPinyin("");
                        phone.setId(0);
                        adapter.add(phone);
                        adapter.notifyDataSetChanged();
                        manage.insertAdmin(phone);
                        this.admin.setText("");
                    }else {
                        mainActivity.TosatShow("添加号码超过5个");
                    }

                }else{
                    mainActivity.TosatShow("请添加正确的手机号码");
                }
                break;
            case R.id.select:
                Intent i=new Intent();
                i.setClass(getActivity(),ContactActivity.class);
                startActivityForResult(i,SELECT_PHONE_FROM_FRAGMENT_LIST_ADMIN);
                break;
            case R.id.authority:
                ArrayList<String> phones=new ArrayList<>();
                for (int z=0;z<list.size();z++){
                    phones.add(list.get(z).getNumber());
                }
                ArrayList<String> admins=new ArrayList<>();
                for (int y=0;y<adapter.getDatas().size();y++){
                    admins.add(adapter.getDatas().get(y).getNumber());
                }
                if (phones.size()>0){
                    if (admins.size()>0){
                        sender.send(phones,builder.SetPhone(admins));
                    }else{
                        mainActivity.TosatShow("请添加管理员号码");
                    }

                }else{
                    mainActivity.TosatShow("请添加设备号码");
                }

                break;
        }
    }
    public void initTag(){
        ArrayList<String> taglist=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            if (!TextUtils.isEmpty(list.get(i).getAddress())){
                taglist.add(list.get(i).getAddress());
            }else{
                taglist.add(list.get(i).getNumber());
            }
        }
        if (tagGroup!=null){
            tagGroup.setTags(taglist);
        }

    }
    public void RefreshData(){
        list = manage.getUserPhone();
        adminlist=manage.getAdmin();
        initTag();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode)
        {
            case (2) :
            {
                if (requestCode==SELECT_PHONE_FROM_FRAGMENT_LIST_ADMIN){
                    ArrayList<Contact> list=(ArrayList<Contact>) data.getSerializableExtra("phonelist");
                    if (list!=null){
                        if(adapter.getItemCount()>5){
                            for (int i=0;i<5;i++){
                                if (Mobile.isMobileNO(list.get(i).getNumber().replaceAll(" ","").replaceAll("-",""))){
                                    adapter.add(list.get(i));
                                    manage.insertAdmin(list.get(i));
                                }
                            }
                        }else{
                            for (int i=0;i<list.size();i++){
                                if (Mobile.isMobileNO(list.get(i).getNumber().replaceAll(" ","").replaceAll("-",""))){
                                    adapter.add(list.get(i));
                                    manage.insertAdmin(list.get(i));
                                }

                            }
                        }

                        adapter.notifyDataSetChanged();

                    }
                }
                break;
            }

        }
    }
}
