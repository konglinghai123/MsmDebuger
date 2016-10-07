package com.dawnlightning.msmdebuger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2016/10/5 08:41
 * 邮箱：823894716@qq.com
 */
public class PhoneAdapter extends BaseAdapter {
    private List<String> listphone=new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    public PhoneAdapter(Context context){
        this.layoutInflater=LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return listphone.size();
    }

    @Override
    public Object getItem(int position) {
        return listphone.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.item_phone,null);
            viewHolder.phone=(TextView)convertView.findViewById(R.id.item_tv_phone);
            viewHolder.delete=(ImageButton)convertView.findViewById(R.id.item_delete);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        viewHolder.phone.setText(listphone.get(position));
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    public void add(String phone){
        this.listphone.add(phone);
    }
    private void remove(int position){
        this.listphone.remove(position);

    }
    public List<String> getlist(){
        return this.listphone;
    }
    class ViewHolder{
        public TextView phone;
        public ImageButton delete;
    }

}
