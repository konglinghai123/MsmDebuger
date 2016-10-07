package com.dawnlightning.msmdebuger.adapter;

import android.content.Context;
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

/**
 * 作者：Administrator on 2016/10/7 02:13
 * 邮箱：823894716@qq.com
 */
public class ContactAdapter extends BaseAdapter{
    private ArrayList<Contact> datas;
    private LayoutInflater layoutInflater;
    private ViewHolder holder;
    private ArrayList<Contact> selectDatas=new ArrayList<>();
    public ContactAdapter(ArrayList<Contact> datas, Context context){
        this.datas=datas;
        Collections.sort(datas);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            holder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.item_list_contact,null);
            holder.Catalog= (TextView) convertView.findViewById(R.id.contact_catalog);
            holder.Name= (TextView) convertView.findViewById(R.id.contact_title);
            holder.Phone= (TextView) convertView.findViewById(R.id.contact_phone);
            holder.isCheck= (CheckBox) convertView.findViewById(R.id.contact_ischeck);
            holder.Line=(TextView)convertView.findViewById(R.id.contact_line); 
            convertView.setTag(holder);
        }else{
            convertView.getTag();
        }
       final Contact item=datas.get(position);

        if (item!=null){
            //如果是第0个那么一定显示#号
            if (position == 0) {
                holder.Catalog.setVisibility(View.VISIBLE);
                holder.Catalog.setText("#");
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
            holder.Name.setText(item.getName());
            holder.Phone.setText(item.getNumber());
            holder.isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        selectDatas.add(item);
                    }else{
                        selectDatas.remove(item);
                    }
                }
            });
        }

        return convertView;
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
