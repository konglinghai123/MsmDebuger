package com.dawnlightning.msmdebuger.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dawnlightning.msmdebuger.MainActivity;
import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.adapter.LightAdapter;
import com.dawnlightning.msmdebuger.command.InstructionsBuilder;
import com.dawnlightning.msmdebuger.command.MsgSender;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

/**
 * 作者：Administrator on 2016/10/4 15:29
 * 邮箱：823894716@qq.com
 */
public class MineFragment extends Fragment {


    @Bind(R.id.listlight)
    ListView listlight;
    @Bind(R.id.tag_group)
    TagGroup tagGroup;


    private LightAdapter lightAdapter;
    private MsgSender sender;
    private InstructionsBuilder builder;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        sender = new MsgSender(activity);
        builder = new InstructionsBuilder();
        mainActivity = (MainActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        lightAdapter = new LightAdapter(getActivity(), new LightAdapter.Action() {
            @Override
            public void open(String color) {
                ArrayList<String> phone = mainActivity.phonelist;

                sender.send(phone, builder.OpenLight(color));


            }

            @Override
            public void close(String color) {
                ArrayList<String> phone = mainActivity.phonelist;

                sender.send(phone, builder.CloseLight(color));


            }
        });
        listlight.setAdapter(lightAdapter);
        tagGroup.setTags(mainActivity.phonelist);
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                mainActivity.phonelist.remove(tag);
                tagGroup.setTags(mainActivity.phonelist);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.test, R.id.alarm_open, R.id.alarm_close, R.id.light_open, R.id.light_close, R.id.search})
    public void onClick(View view) {
        ArrayList<String> phone = mainActivity.phonelist;
        switch (view.getId()) {
            case R.id.alarm_open:
                sender.send(phone, builder.OpenAlarm());
                break;
            case R.id.alarm_close:
                sender.send(phone, builder.ColseAlarm());
                break;
            case R.id.light_open:
                sender.send(phone, builder.OpenPowerContorl());
                break;
            case R.id.light_close:
                sender.send(phone, builder.ClosePowerContorl());
                break;
            case R.id.search:
                sender.send(phone, builder.Search());
                break;
            case R.id.test:
                sender.send(phone, builder.Test());
                break;

        }

    }
    public void RestTag(){
        if (tagGroup!=null){
            tagGroup.setTags(mainActivity.phonelist);
        }
    }
}
