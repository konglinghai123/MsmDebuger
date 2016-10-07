package com.dawnlightning.msmdebuger.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2016/10/5 08:24
 * 邮箱：823894716@qq.com
 */
public class LightAdapter extends BaseAdapter {
    private List<String>  listcolor=new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private Action action;
    public LightAdapter(Context context,Action action){
        listcolor.add("红");
        listcolor.add("蓝");
        listcolor.add("橙");
        listcolor.add("黄");
        this.layoutInflater=LayoutInflater.from(context);
        this.action=action;
    }

    @Override
    public int getCount() {
        return listcolor.size();
    }

    @Override
    public Object getItem(int position) {
        return listcolor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.item_light,null);
            viewHolder.color=(TextView) convertView.findViewById(R.id.item_tv_color);
            viewHolder.open=(Button)convertView.findViewById(R.id.open);
            viewHolder.close=(Button)convertView.findViewById(R.id.close);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.color.setText(listcolor.get(position));
        viewHolder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.open(listcolor.get(position));
            }
        });

        viewHolder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.close(listcolor.get(position));
            }
        });
        return convertView;
    }
    class ViewHolder{
        public TextView color;
        public Button open;
        public Button close;
    }
    public interface Action{
        void open(String color);
        void close(String color);
    }

}
