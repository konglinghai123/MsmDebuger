package com.dawnlightning.msmdebuger.command;

import java.util.List;

/**
 * 作者：Administrator on 2016/10/5 10:25
 * 邮箱：823894716@qq.com
 */
public interface InstructionBuilderInterface {
    String Test();
    String ReadContext(String times,String color,String context);
    String OpenLight(String color);
    String CloseLight(String color);
    String Register(String code);
    String SetPhone(List<String> list);
    String OpenAlarm();
    String ColseAlarm();
    String SetLightSpeedAndTimes(String speed,String times);
    String OpenPowerContorl();
    String ClosePowerContorl();
    String SetAddress(String address);
}
