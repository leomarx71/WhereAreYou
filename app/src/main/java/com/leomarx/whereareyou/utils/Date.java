package com.leomarx.whereareyou.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by RicardoSantos on 10/01/17.
 */

public class Date {
    public static String now(){
        Calendar cc = Calendar.getInstance();
        java.util.Date date = cc.getTime();
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return format2.format(date);
    }
}
