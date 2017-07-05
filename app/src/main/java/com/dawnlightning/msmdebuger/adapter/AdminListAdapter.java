package com.dawnlightning.msmdebuger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.bean.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2016/10/23 04:05
 * 邮箱：823894716@qq.com
 */
public class AdminListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Contact> datas;
     Context context;
    OnClickListener listener;

    public ArrayList<Contact> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Contact> datas) {
        this.datas = datas;
    }

    public interface OnClickListener{
        void delete(Contact contact);
        void remark(Contact contact,String name);
    }
    public AdminListAdapter(ArrayList<Contact> datas, Context context, OnClickListener listener){
        this.datas=datas;
        this.context=context;
        this.listener=listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_list, parent,
                false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final  RecyclerView.ViewHolder holder, final int position) {
        ((ItemViewHolder) holder).phone.setText(datas.get(position).getNumber());
        ((ItemViewHolder) holder).title.setText(datas.get(position).getName());
        ((ItemViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(datas.get(position));
            }
        });
        ((ItemViewHolder) holder).bt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.remark(datas.get(position),((ItemViewHolder) holder).title.getText().toString());
            }
        });

    }

    public void add(Contact contact){
        this.datas.add(contact);
    }
    public void addAll(ArrayList<Contact> contacts){
        this.datas.addAll(contacts);
    }
    public void remove(Contact contact){
        this.datas.remove(contact);
    }
    @Override
    public int getItemCount() {
        return datas.size()==0 ? 0:datas.size();
    }
    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView phone;
        Button delete;
        EditText title;
        TextView bt_title;

        public ItemViewHolder(View view) {
            super(view);
            phone=(TextView)view.findViewById(R.id.item_phone_admin_list_contact_phone);
            delete=(Button)view.findViewById(R.id.item_phone_admin_list_delete);
            title=(EditText)view.findViewById(R.id.item_phone_admin_list_ed_title);
            bt_title=(TextView)view.findViewById(R.id.item_phone_admin_list_bt_title);

        }
    }
}
