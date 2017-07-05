package com.dawnlightning.msmdebuger.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.bean.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2016/10/23 00:00
 * 邮箱：823894716@qq.com
 */
public class PhoneListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public  static final int TYPE_ITEM = 0;
    public  static final  int TYPE_HEADER = 1;
    public ArrayList<Phone> data;
    public Context context;
    public OnClickListener listterner;
    public PhoneListAdapter(ArrayList<Phone> data,Context context,OnClickListener listerner){
        this.data=data;
        this.context=context;
        this.listterner=listerner;
    }

    public ArrayList<Phone> getData() {
        return data;
    }

    public void setData(ArrayList<Phone> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_HEADER){
            View view = LayoutInflater.from(context).inflate(R.layout.item_phone_list_header, parent,
                    false);
            return new HeaderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_phone_list, parent,
                    false);
            return new ItemViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(final  RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).phone.setText(data.get(position-1).getNumber());
            ((ItemViewHolder) holder).register.setText(data.get(position-1).getCode());
            ((ItemViewHolder) holder).address.setText(data.get(position-1).getAddress());
            ((ItemViewHolder) holder).bt_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listterner.address(data.get(position - 1), ((ItemViewHolder) holder).address.getText().toString());
                }
            });
            ((ItemViewHolder) holder).bt_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listterner.register(data.get(position - 1), ((ItemViewHolder) holder).register.getText().toString());
                }
            });
            ((ItemViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listterner.delete(data.get(position - 1));
                }
            });
            ((ItemViewHolder) holder).call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listterner.call(data.get(position-1));
                }
            });
        }else{
            if (data.size()==0){
                ((HeaderViewHolder) holder).tips.setText("请添加设备号码");
            }else{
                ((HeaderViewHolder) holder).tips.setText("当前设备列表");
            }
        }


    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 1 : data.size()+1;
    }

     class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView phone;
        Button delete;
         Button call;
        EditText register;
        EditText address;
        TextView bt_register;
        TextView bt_address;

        public ItemViewHolder(View view) {
            super(view);
            phone=(TextView)view.findViewById(R.id.item_phone_list_tv_phone);
            delete=(Button)view.findViewById(R.id.item_phone_list_delete);
            register=(EditText)view.findViewById(R.id.item_phone_list_ed_register);
            address=(EditText)view.findViewById(R.id.item_phone_list_ed_address);
            bt_register=(TextView)view.findViewById(R.id.item_phone_list_bt_register);
            bt_address=(TextView)view.findViewById(R.id.item_phone_list_bt_address);
            call=(Button)view.findViewById(R.id.item_phone_list_call);

        }
    }
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tips;


        public HeaderViewHolder(View view) {
            super(view);
            tips=(TextView)view.findViewById(R.id.item_phone_list_tv_tips);

        }
    }
    public void setList(ArrayList<Phone> list){
        this.data=list;
    }
    public void addAll(List<Phone> list){
        this.data.addAll(list);
    }
    public void add(Phone phone){
        this.data.add(phone);
    }
    public void remove(Phone phone){
        this.data.remove(phone);
    }
    public Phone getPhone(int position){
        return this.data.get(position);
    }
    public void clearList(){
        this.data.clear();
    }
    public interface OnClickListener{
        void delete(Phone phone);
        void register(Phone phone,String code);
        void address(Phone phone,String address);
        void call(Phone Phone);
    }
}
