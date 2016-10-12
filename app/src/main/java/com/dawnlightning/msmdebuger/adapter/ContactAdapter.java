package com.dawnlightning.msmdebuger.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.bean.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * 作者：Administrator on 2016/10/7 02:13
 * 邮箱：823894716@qq.com
 */
public class ContactAdapter extends BaseAdapter{
    private ArrayList<Contact> datas;
    private LayoutInflater layoutInflater;
    private ViewHolder holder;
    private ArrayList<Contact> selectDatas=new ArrayList<>();
    HashMap<Integer, Boolean> state = new HashMap<Integer,Boolean>();
    public ContactAdapter(ArrayList<Contact> datas, Context context){
        this.datas=datas;
        Collections.sort(this.datas);
        this.layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView==null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_list_contact, null);
            holder.Catalog = (TextView) convertView.findViewById(R.id.contact_catalog);
            holder.Name = (TextView) convertView.findViewById(R.id.contact_title);
            holder.Phone = (TextView) convertView.findViewById(R.id.contact_phone);
            holder.isCheck = (CheckBox) convertView.findViewById(R.id.contact_ischeck);
            holder.Line = (TextView) convertView.findViewById(R.id.contact_line);

            holder.isCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox=(CheckBox)v;
                    if (checkBox.isChecked()){
                        selectContact(position,checkBox.isChecked());
                    }else{
                        selectContact(position,checkBox.isChecked());
                    }
                }
            });
            //Log.e("Tag",String.valueOf(item.isCheck()+"["+String.valueOf(position))+"]");

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final Contact item=datas.get(position);

        if (item!=null){
            //如果是第0个那么一定显示#号
            if (position == 0) {
                holder.Catalog.setVisibility(View.VISIBLE);
                if (item.getFirstChar()=='A'){
                    holder.Catalog.setText("A");
                }else{
                    holder.Catalog.setText("#");
                }

                holder.Line.setVisibility(View.VISIBLE);
            } else {

                //如果和上一个item的首字母不同，则认为是新分类的开始
                Contact prevData = datas.get(position - 1);
                if (item.getFirstChar() != prevData.getFirstChar()) {
                    holder.Catalog.setVisibility(View.VISIBLE);
                    holder.Catalog.setText("" + item.getFirstChar());
                    holder.Line.setVisibility(View.VISIBLE);
                } else {
                    holder.Catalog.setVisibility(View.GONE);
                    holder.Line.setVisibility(View.GONE);
                }
            }
            holder.isCheck.setChecked(state.get(position)==null? false : true);
            holder.Name.setText(item.getName());
            holder.Phone.setText(item.getNumber());

        }

        return convertView;
    }
    public void selectContact(int position,boolean isChecked){
        Contact item=datas.get(position);
        if (isChecked){
            state.put(position, isChecked);
            selectDatas.add(item);
        }else{
            state.remove(position);
            selectDatas.remove(item);
        }

    }
   class ViewHolder{
       public TextView Catalog;
       public TextView Line;
       public TextView Name;
       public TextView Phone;
       public CheckBox isCheck;
   }
    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        Contact item = datas.get(position);
        return item.getFirstChar();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = datas.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
    public ArrayList<Contact> getSelectDatas(){
        return this.selectDatas;
    }
    public void setDatas(ArrayList<Contact> contacts){
        this.datas=contacts;
    }
}
