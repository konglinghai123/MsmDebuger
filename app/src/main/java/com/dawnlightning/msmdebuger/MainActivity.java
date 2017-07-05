package com.dawnlightning.msmdebuger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dawnlightning.msmdebuger.base.MyApp;
import com.dawnlightning.msmdebuger.bean.Phone;
import com.dawnlightning.msmdebuger.dao.PhoneManage;
import com.dawnlightning.msmdebuger.fragment.HomeFragment;
import com.dawnlightning.msmdebuger.fragment.MachinePhoneListFragment;
import com.dawnlightning.msmdebuger.utils.Mobile;
import com.dawnlightning.msmdebuger.adapter.FragmentAdapter;
import com.dawnlightning.msmdebuger.fragment.LightFragment;
import com.dawnlightning.msmdebuger.fragment.MineFragment;
import com.dawnlightning.msmdebuger.fragment.PhoneFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

public class MainActivity extends AppCompatActivity {

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
    ArrayList<Fragment> fragmentList= new ArrayList<>();
    public  ArrayList<String> phonelist=new ArrayList<>();
    private ArrayList<Phone> list=new ArrayList<>();
    private PhoneManage manage=PhoneManage.getManage(MyApp.getApp().getSQLHelper());
    public void RefreshData(){
        list=manage.getUserPhone();
        phonelist.clear();
        for (int i=0;i<list.size();i++){
            if (!TextUtils.isEmpty(list.get(i).getAddress())){
                phonelist.add(list.get(i).getAddress());
            }else{
                phonelist.add(list.get(i).getNumber());
            }
        }

    }
    public MainActivity(){
        RefreshData();
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
                RefreshData();
                switch (position){
                    case 0:
                        reset();
                        trends.setSelected(true);
                        ((HomeFragment)fragmentList.get(0)).RestTag();
                        break;
                    case 1:
                        reset();
                        follow.setSelected(true);
                        ((LightFragment)fragmentList.get(1)).RestTag();
                        break;
                    case 2:
                        reset();
                        message.setSelected(true);
                        break;
                    case 3:
                        reset();
                        mine.setSelected(true);
                        ((MineFragment)fragmentList.get(3)).RestTag();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        main.setCurrentItem(0);
        trends.setSelected(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    @OnClick({R.id.trends, R.id.follow, R.id.message, R.id.mine})
    public void onClick(View view) {
        RefreshData();
        switch (view.getId()) {
            case R.id.trends:
                reset();
                trends.setSelected(true);
                main.setCurrentItem(0);
                ((HomeFragment)fragmentList.get(0)).RestTag();
                break;
            case R.id.follow:
                reset();
                follow.setSelected(true);
                main.setCurrentItem(1);
                ((LightFragment)fragmentList.get(1)).RestTag();
                break;
            case R.id.message:
                reset();
                message.setSelected(true);
                main.setCurrentItem(2);
                fragmentList.get(2);
                break;
            case R.id.mine:
                reset();
                mine.setSelected(true);
                main.setCurrentItem(3);
                ((MineFragment)fragmentList.get(3)).RestTag();
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
