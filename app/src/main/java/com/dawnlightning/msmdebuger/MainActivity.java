package com.dawnlightning.msmdebuger;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dawnlightning.msmdebuger.Utils.Mobile;
import com.dawnlightning.msmdebuger.adapter.FragmentAdapter;
import com.dawnlightning.msmdebuger.bean.Contact;
import com.dawnlightning.msmdebuger.fragment.HomeFragment;
import com.dawnlightning.msmdebuger.fragment.LightFragment;
import com.dawnlightning.msmdebuger.fragment.MineFragment;
import com.dawnlightning.msmdebuger.fragment.PhoneFragment;
import com.dawnlightning.msmdebuger.widget.EditUsename;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main)
    ViewPager main;
    @Bind(R.id.trends)
    TextView trends;
    @Bind(R.id.follow)
    TextView follow;
    @Bind(R.id.message)
    TextView message;
    @Bind(R.id.mine)
    TextView mine;
    FragmentAdapter adapter;
    public EditText phone;
    public TagGroup phonegroup;
    ArrayList<Fragment> fragmentList= new ArrayList<>();
    private TextView selectnumber;
    private TextView addnumber;
    public  ArrayList<String> phonelist=new ArrayList<>();
    public final  static int SELECTPHONEFROMCONTACT=1;
    public MainActivity(){
        fragmentList.add(new HomeFragment());
        fragmentList.add(new LightFragment());
        fragmentList.add(new PhoneFragment());
        fragmentList.add(new MineFragment());
    }
    public ArrayList<String> getPhonelist(){
        return this.phonelist;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter=new FragmentAdapter(getSupportFragmentManager(),fragmentList);
        main.setAdapter(adapter);
        main.setOffscreenPageLimit(3);
        main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        reset();
                        trends.setSelected(true);
                        break;
                    case 1:
                        reset();
                        follow.setSelected(true);
                        break;
                    case 2:
                        reset();
                        message.setSelected(true);

                        break;
                    case 3:
                        reset();
                        mine.setSelected(true);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        toolbar.setTitleTextColor(getResources().getColor(R.color.jianshu));
        phone=(EditText) toolbar.findViewById(R.id.phone);
        phonegroup=(TagGroup)toolbar.findViewById(R.id.tag_group);
        selectnumber=(TextView) toolbar.findViewById(R.id.select);
        addnumber=(TextView) toolbar.findViewById(R.id.add);
        addnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber=phone.getText().toString();
                if (!TextUtils.isEmpty(phonenumber)&& Mobile.isMobileNO(phonenumber)){
                    phonelist.add(phonenumber);
                    phonegroup.setTags(phonelist);
                    phone.setText("");
                }else{
                    TosatShow("请添加正确的手机号码");
                }
            }
        });
        selectnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(MainActivity.this,ContactActivity.class);
                startActivityForResult(i,MainActivity.SELECTPHONEFROMCONTACT);
            }
        });
        phonegroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                phonelist.remove(tag);
                phonegroup.setTags(phonelist);
            }
        });
        main.setCurrentItem(0);
        trends.setSelected(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        phonegroup.setTags(phonelist);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode)
        {
            case (2) :
            {
                if (requestCode==SELECTPHONEFROMCONTACT){
                    ArrayList<String> list=data.getStringArrayListExtra("phonelist");
                    if (list!=null){
                        for (int i=0;i<list.size();i++){
                            if (Mobile.isMobileNO(list.get(i))){
                                phonelist.add(list.get(i));
                            }

                        }
                        phonegroup.setTags(phonelist);
                    }
                }


                break;
            }

        }
    }


    @OnClick({R.id.trends, R.id.follow, R.id.message, R.id.mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trends:
                reset();
                trends.setSelected(true);
                main.setCurrentItem(0);
                break;
            case R.id.follow:
                reset();
                follow.setSelected(true);
                main.setCurrentItem(1);
                break;
            case R.id.message:
                reset();
                message.setSelected(true);
                main.setCurrentItem(2);
                break;
            case R.id.mine:
                reset();
                mine.setSelected(true);
                main.setCurrentItem(3);
                break;
        }
    }
    private void reset(){
        trends.setSelected(false);
        follow.setSelected(false);
        message.setSelected(false);
        mine.setSelected(false);

    }
    public void TosatShow(String msg){
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
