package com.dawnlightning.msmdebuger.command;

import java.util.List;

/**
 * 作者：Administrator on 2016/10/5 10:24
 * 邮箱：823894716@qq.com
 */
public class InstructionsBuilder implements InstructionBuilderInterface{
    @Override
    public String Test() {
        return Instructions.TEST;
    }

    @Override
    public String ReadContext(String times, String color, String context) {
        return String.format(Instructions.CONTEXT, times,color,context);
    }

    @Override
    public String OpenLight(String color) {
        return String.format(Instructions.OPENLIGHT,color);
    }

    @Override
    public String CloseLight(String color) {
        return String.format(Instructions.CLOSELIGHT,color);
    }

    @Override
    public String Register(String code) {
        return String.format(Instructions.REGISTER,code);
    }

    @Override
    public String SetPhone(List<String> list) {
        String strphone="";

        for (int i=0;i<list.size();i++){
            if (i<list.size()-1){
                strphone=list.get(i)+"*"+strphone;
            }else{
                strphone=strphone+list.get(i);
            }

        }


        return String.format(Instructions.PHONES,strphone);
    }

    @Override
    public String OpenAlarm() {
        return String.format(Instructions.OPENALARM);
    }

    @Override
    public String ColseAlarm() {
        return String.format(Instructions.CLOSEALARM);
    }

    @Override
    public String SetLightSpeedAndTimes(String speed, String times) {
        if (Integer.parseInt(speed)<10){
            speed="0"+speed;
        }
        if (Integer.parseInt(times)<10){
            times="0"+times;
        }
        return String.format(Instructions.SETLIGHTSPEEDORTIMES,speed,times);
    }

    @Override
    public String OpenPowerContorl() {
        return String.format(Instructions.OPENPOWERCONTROL);
    }

    @Override
    public String ClosePowerContorl() {
        return String.format(Instructions.CLOSEPOWERCONTROL);
    }

    @Override
    public String SetAddress(String address) {
        return String.format(Instructions.SETADDRESS,address);
    }
    public String Search(){
        return Instructions.SEARCH;
    }

    public String SetReadSpeed(String speed){
        return String.format(Instructions.READ,speed);
    }
}
