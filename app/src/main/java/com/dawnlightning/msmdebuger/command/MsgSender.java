package com.dawnlightning.msmdebuger.command;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 作者：Administrator on 2016/10/5 10:42
 * 邮箱：823894716@qq.com
 */
public class MsgSender {
    private Context context;
    public MsgSender(Context context){
        this.context=context;
    }
    public void send(ArrayList<String> numbers, String message){
        if (numbers.size()!=0){
            Toast.makeText(context,"短信发送中...",Toast.LENGTH_SHORT).show();

            for (String number:numbers) {
                String SENT = "sms_sent";
                String DELIVERED = "sms_delivered";
                PendingIntent sentPI = PendingIntent.getActivity(context, 0, new Intent(SENT), 0);
                PendingIntent deliveredPI = PendingIntent.getActivity(context, 0, new Intent(DELIVERED), 0);

                SmsManager smsm = SmsManager.getDefault();
                smsm.sendTextMessage(number, null, message, sentPI, deliveredPI);
            }
        }else{
            Toast.makeText(context,"请输入手机号码",Toast.LENGTH_SHORT).show();
        }

    }
}
