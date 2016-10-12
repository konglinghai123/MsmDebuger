package com.dawnlightning.msmdebuger.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dawnlightning.msmdebuger.MainActivity;
import com.dawnlightning.msmdebuger.R;
import com.dawnlightning.msmdebuger.Utils.Mobile;
import com.dawnlightning.msmdebuger.command.InstructionsBuilder;
import com.dawnlightning.msmdebuger.command.MsgSender;
import com.dawnlightning.msmdebuger.widget.EditUsename;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Administrator on 2016/8/20 00:01
 * 邮箱：823894716@qq.com
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.tv_times_true)
    TextView tvTimesTrue;
    @Bind(R.id.sb_light_speed)
    SeekBar sbLightSpeed;
    @Bind(R.id.rg_color)
    RadioGroup rgColor;
    @Bind(R.id.regsiter)
    EditText regsiter;
    @Bind(R.id.address)
    EditText address;
    @Bind(R.id.bt_register)
    TextView btRegister;
    @Bind(R.id.bt_address)
    TextView btAddress;
    @Bind(R.id.send)
    Button send;
    @Bind(R.id.context)
    EditText context;
    private MsgSender sender;
    private InstructionsBuilder builder;
    private MainActivity mainActivity;
    private String color;
    @Override
    public void onAttach(Activity activity) {
        sender=new MsgSender(activity);
        builder=new InstructionsBuilder();
        mainActivity=(MainActivity) activity;
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

        sbLightSpeed.setMax(9);
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
                color=rb.getText().toString();
            }
        });
        color=((RadioButton)(rgColor.findViewById(rgColor.getCheckedRadioButtonId()))).getText().toString();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }




    @OnClick({R.id.bt_register, R.id.send,R.id.bt_address})
    public void onClick(View view) {
        ArrayList<String> phone=mainActivity.getPhonelist();

            switch (view.getId()) {
                case R.id.bt_register:
                    sender.send(phone,builder.Register(regsiter.getText().toString()));
                    break;
                case R.id.send:
                    sender.send(phone,builder.ReadContext(tvTimesTrue.getText().toString(),color,context.getText().toString()));
                    break;
                case R.id.bt_address:
                    sender.send(phone,builder.SetAddress(address.getText().toString()));
                    break;
            }


    }
}
