package com.dawnlightning.msmdebuger.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.MainActivity;
import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.command.InstructionsBuilder;
import com.dawnlightning.msmdebuger.command.MsgSender;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

/**
 * 作者：Administrator on 2016/10/4 15:30
 * 邮箱：823894716@qq.com
 */
public class LightFragment extends Fragment {
    @Bind(R.id.tv_light_speed_true)
    TextView tvLightSpeedTrue;
    @Bind(R.id.sb_light_speed)
    SeekBar sbLightSpeed;
    @Bind(R.id.tv_light_time_true)
    TextView tvLightTimeTrue;
    @Bind(R.id.sb_light_time)
    SeekBar sbLightTime;
    @Bind(R.id.tv_read_speed_true)
    TextView read_speed;
    @Bind(R.id.sb_read_speed)
    SeekBar sbReadspeed;
    @Bind(R.id.tag_group)
    TagGroup tagGroup;
    @Bind(R.id.send)
    Button send;
    @Bind(R.id.tv_read_speed)
    TextView tvReadSpeed;
    @Bind(R.id.send_readspeed)
    Button sendReadspeed;

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
        final View view = inflater.inflate(R.layout.fragment_light, container, false);
        ButterKnife.bind(this, view);

        sbLightSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                tvLightSpeedTrue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvLightTimeTrue.setText(String.valueOf(1) + "分钟");
        sbLightTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                tvLightTimeTrue.setText(String.valueOf(progress) + "分钟");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        read_speed.setText(String.valueOf(1));
        sbReadspeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                read_speed.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    @OnClick({R.id.send, R.id.send_readspeed})
    public void onClick(View view) {
        ArrayList<String> phone = mainActivity.getPhonelist();
        switch (view.getId()) {
            case R.id.send:

                sender.send(phone, builder.SetLightSpeedAndTimes(tvLightSpeedTrue.getText().toString(), tvLightTimeTrue.getText().toString().replaceAll("分钟", "")));
                break;
            case R.id.send_readspeed:
                sender.send(phone, builder.SetReadSpeed(read_speed.getText().toString()));
                break;

        }

    }
    public void RestTag(){
        if (tagGroup!=null){
            tagGroup.setTags(mainActivity.phonelist);
        }

    }
}
