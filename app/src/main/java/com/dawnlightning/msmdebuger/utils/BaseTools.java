package com.dawnlightning.msmdebuger.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.spreada.utils.chinese.ZHConverter;

import java.io.UnsupportedEncodingException;

/**
 * 作者：Administrator on 2016/10/23 03:31
 * 邮箱：823894716@qq.com
 */
public class BaseTools {
    /** 获取屏幕的宽度 */
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 繁体转简体
     * @param s
     * @return
     */
    public static String big5ToChinese( String s )
    {
        ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);

       String context=converter.convert(s);
        return context;
    }

    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {

        return BCConvert.qj2bj(input);
    }
}
