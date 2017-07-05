package com.dawnlightning.msmdebuger.bean;

/**
 * 作者：Administrator on 2016/10/22 23:34
 * 邮箱：823894716@qq.com
 */
public class Phone {
    private String number;
    private String address;
    private String code;
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public Phone(){

    }
    public Phone(String number, String address, String code) {
        this.number = number;
        this.address = address;
        this.code = code;
    }
}
