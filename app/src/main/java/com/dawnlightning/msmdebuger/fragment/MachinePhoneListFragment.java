package com.dawnlightning.msmdebuger.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.ContactActivity;
import com.dawnlightning.msmdebuger.MainActivity;
import com.dawnlightning.msmdebuger.R;
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

/**
 * 作者：Administrator on 2016/10/23 00:38
 * 邮箱：823894716@qq.com
 */
public class MachinePhoneListFragment extends Fragment {
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.add)
    TextView add;
    @Bind(R.id.select)
    TextView select;
    @Bind(R.id.recyclerView_machine)
    RecyclerView recyclerViewPhones;
    ArrayList<Phone> list=new ArrayList<>();
    public final  static int SELECT_PHONE_FROM_FRAGMENT_LIST=80;
    private MsgSender sender;
    private InstructionsBuilder builder;
    private MainActivity mainActivity;
    private PhoneListAdapter adapter;
    private PhoneManage manage=PhoneManage.getManage(MyApp.getApp().getSQLHelper());
    @Override
    public void onAttach(Activity activity) {
        sender=new MsgSender(activity);
        builder=new InstructionsBuilder();
        mainActivity=(MainActivity) activity;
        list=manage.getUserPhone();
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_machine, container, false);
        ButterKnife.bind(this, view);

        adapter=new PhoneListAdapter(list, getActivity(), new PhoneListAdapter.OnClickListener() {
            @Override
            public void delete(Phone phone) {
                adapter.remove(phone);
                adapter.notifyDataSetChanged();
                manage.deletePhone(phone);
            }

            @Override
            public void register(Phone phone,String code) {
                ArrayList phonelist=new ArrayList();
                phonelist.add(phone.getNumber());
                sender.send(phonelist,builder.Register(code));
                phone.setCode(code);
                manage.updatePhone(phone);
            }

            @Override
            public void address(Phone phone,String address) {
                ArrayList phonelist=new ArrayList();
                phonelist.add(phone.getNumber());
                sender.send(phonelist,builder.SetAddress(address));
                phone.setAddress(address);
                manage.updatePhone(phone);
            }

            @Override
            public void call(Phone Phone) {
                CallNumber(Phone.getNumber());
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewPhones.setLayoutManager(layoutManager);
        recyclerViewPhones.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.add, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                String phonenumber=phone.getText().toString();
                if (!TextUtils.isEmpty(phonenumber)&& Mobile.isMobileNO(phonenumber)){
                    Phone phone=new Phone();
                    phone.setNumber(phonenumber);
                    phone.setCode("");
                    phone.setAddress("");
                    adapter.add(phone);
                    adapter.notifyDataSetChanged();
                    manage.insertPhone(phone);
                    this.phone.setText("");
                }else{
                    mainActivity.TosatShow("请添加正确的手机号码");
                }
                break;
            case R.id.select:
                Intent i=new Intent();
                i.setClass(getActivity(),ContactActivity.class);
                startActivityForResult(i,SELECT_PHONE_FROM_FRAGMENT_LIST);
                break;
        }
    }
    private void CallNumber(String phone){

            //这里"tel:"+电话号码 是固定格式，系统一看是以"tel:"开头的，就知道后面应该是电话号码。
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.trim()));
            startActivity(intent);//调用上面这个intent实现拨号



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode)
        {
            case (2) :
            {
                if (requestCode==SELECT_PHONE_FROM_FRAGMENT_LIST){
                    ArrayList<Contact> list=(ArrayList<Contact>) data.getSerializableExtra("phonelist");
                    if (list!=null){
                        for (int i=0;i<list.size();i++){
                            if (Mobile.isMobileNO(list.get(i).getNumber())){
                                Phone phone=new Phone(list.get(i).getNumber(),"","");
                                adapter.add(phone);
                                manage.insertPhone(phone);
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
