package com.dawnlightning.msmdebuger.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

public class FragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    public void clearlist(){
        this.fragmentsList.clear();
    }
    public void setFragmentsList(ArrayList<Fragment> fragmentsList){
        this.fragmentsList=fragmentsList;
    }

}