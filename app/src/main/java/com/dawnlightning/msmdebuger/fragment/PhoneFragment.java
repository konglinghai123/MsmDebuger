package com.dawnlightning.msmdebuger.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import com.dawnlightning.msmdebuger.MainActivity;
import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.adapter.AdminListAdapter;
import com.dawnlightning.msmdebuger.adapter.FragmentAdapter;
import com.dawnlightning.msmdebuger.command.InstructionsBuilder;
import com.dawnlightning.msmdebuger.command.MsgSender;
import com.dawnlightning.msmdebuger.utils.BaseTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Administrator on 2016/10/4 15:29
 * 邮箱：823894716@qq.com
 */
public class PhoneFragment extends Fragment {
    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.phone_viewpager)
    ViewPager phoneViewpager;
    private MsgSender sender;
    private InstructionsBuilder builder;
    private MainActivity mainActivity;

    FragmentAdapter adapter;
    CommonNavigator commonNavigator;
    List<String> mDataList=new ArrayList<String>();
    ArrayList<Fragment> fragmentList= new ArrayList<>();
    private int magicIndicator_item_width=0;
    public PhoneFragment(){
        mDataList.add("设备列表");
        mDataList.add("管理员列表");
        fragmentList.add(new MachinePhoneListFragment());
        fragmentList.add(new AdminPhoneListFragment());


    }
    @Override
    public void onAttach(Activity activity) {
        sender = new MsgSender(activity);
        builder = new InstructionsBuilder();
        mainActivity = (MainActivity) activity;
        magicIndicator_item_width= (BaseTools.getWindowsWidth(getActivity()))/mDataList.size();
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_phone, container, false);
        ButterKnife.bind(this, view);

        initViewPager();
        initPageTitleView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    /**
     * 初始化viewpager
     */

    private void initViewPager(){
        adapter=new FragmentAdapter(getActivity().getSupportFragmentManager(),fragmentList);
        phoneViewpager.setAdapter(adapter);
        phoneViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
                if (position==1){
                    ((AdminPhoneListFragment)fragmentList.get(position)).RefreshData();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);

            }
        });

        phoneViewpager.setCurrentItem(0);

    }
    /**
     * 初始化顶部页面指示器
     */
    private  void  initPageTitleView(){
        commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final  int index) {
                final ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(index));
                //clipPagerTitleView.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                clipPagerTitleView.setTextSize(30);
                clipPagerTitleView.setTextColor(Color.parseColor("#919191"));
                clipPagerTitleView.setClipColor(Color.parseColor("#d56e5c"));

                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phoneViewpager.setCurrentItem(index);
                        clipPagerTitleView.setClipColor(Color.parseColor("#d56e5c"));
                    }
                });

                return clipPagerTitleView;
            }
            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;    // 没有指示器，因为title的指示作用已经很明显了
            }
        });
        magicIndicator.setNavigator(commonNavigator);
    }

}
