package com.dawnlightning.msmdebuger.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawnlightning.msmdebuger.MainActivity;
import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.base.MyApp;
import com.dawnlightning.msmdebuger.command.InstructionsBuilder;
import com.dawnlightning.msmdebuger.command.MsgSender;
import com.dawnlightning.msmdebuger.listener.MaxLengthListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

/**
 * 作者：Administrator on 2016/8/20 00:01
 * 邮箱：823894716@qq.com
 */
public class HomeFragment extends Fragment {


    @Bind(R.id.tv_times_true)
    TextView tvTimesTrue;
    @Bind(R.id.sb_light_speed)
    SeekBar sbLightSpeed;
    @Bind(R.id.context)
    EditText context;
    @Bind(R.id.tv_light)
    TextView tvLight;
    @Bind(R.id.rb_blue)
    RadioButton rbBlue;
    @Bind(R.id.rb_oranger)
    RadioButton rbOranger;
    @Bind(R.id.rb_yellow)
    RadioButton rbYellow;
    @Bind(R.id.rb_reb)
    RadioButton rbReb;
    @Bind(R.id.rg_color)
    RadioGroup rgColor;
    @Bind(R.id.send)
    TextView send;
    @Bind(R.id.stop)
    Button stop;
    @Bind(R.id.tag_group)
    TagGroup tagGroup;
    private String color;
    private final static int MAXLENGTH = 2000;
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
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        sbLightSpeed.setMax(99);
        sbLightSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                tvTimesTrue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        rgColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) group.findViewById(radioButtonId);
                //更新文本内容，以符合选中项
                Log.e("color", rb.getText().toString());
                color = rb.getText().toString();
            }
        });
        color = ((RadioButton) (rgColor.findViewById(rgColor.getCheckedRadioButtonId()))).getText().toString();

        context.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        context.setText(MyApp.getContext());
        context.addTextChangedListener(new MaxLengthListener(MAXLENGTH, context));
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


    @OnClick({R.id.send, R.id.stop})
    public void onClick(View view) {
        ArrayList<String> phone = mainActivity.getPhonelist();

        switch (view.getId()) {
            case R.id.send:
                sender.send(phone, builder.ReadContext(tvTimesTrue.getText().toString(), color, context.getText().toString()));
                MyApp.setContext(context.getText().toString());
                break;
            case R.id.stop:
                sender.send(phone, builder.Stop());
                break;
        }

    }
    public void RestTag(){


        if (tagGroup!=null){
            tagGroup.setTags(mainActivity.phonelist);
        }
    }
}
