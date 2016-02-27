package me.vanhely.kanshannews.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtils {

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String MMDD = "MM月dd日";


    public static Date getFormatTimeDate(String pubtime, String inputFormat, String outFormat) {

        if (TextUtils.isEmpty(pubtime)) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat(inputFormat, Locale.getDefault());

        Date date = null;
        try {
            pubtime = pubtime.replace("Z", " ");
            pubtime = pubtime.replace("T", " ");

            date = df.parse(pubtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getFormatTime(String pubtime, String inputFormat, String outFormat) {

        Date date = getFormatTimeDate(pubtime, inputFormat, outFormat);

        return (null == date) ? null : (new SimpleDateFormat(outFormat, Locale.getDefault())).format(date);
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    public static String getDateTag(String dateStr) {

        String pre = DateUtils.getFormatTime(dateStr, DateUtils.YYYYMMDD, DateUtils.MMDD);

        Date date = DateUtils.getFormatTimeDate(dateStr, DateUtils.YYYYMMDD, DateUtils.MMDD);

        String week = DateUtils.getWeekOfDate(date);

        return new StringBuilder().append(pre).append(" ").append(week).toString();
    }

}
