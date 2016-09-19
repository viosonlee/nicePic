package lee.vioson.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期操作工具
 */

public class DateUtil {

    private static final String FORMAT = "yyyy-MM-dd HH:mm";

    public static Date str2Date(String str) {
        return str2Date(str, null);
    }

    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            date = sdf.parse(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }

    public static Calendar str2Calendar(String str) {
        return str2Calendar(str, null);

    }

    public static Calendar str2Calendar(String str, String format) {

        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;

    }

    public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }

    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }

    public static String date2Str(Date d, String format) {// yyyy-MM-dd
        // HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(d);
    }

    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }

    public static String getCurDateStr() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-"
                + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
    }

    /**
     * 获得当前日期的字符串格式
     *
     * @param format
     * @return
     */
    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);
    }

    // 格式到秒
    public static String getMillon(long time) {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA).format(time);
    }

    // 格式到天
    public static String getDay(long time) {

        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(time);

    }

    // 格式分
    public static String getMinute(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(time);

    }

    // 将字符串转为时间戳
    public static long getTime(String user_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long l = 0;
        try {
            Date d = sdf.parse(user_time);
            l = d.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
        return l / 1000;
    }

    // 将字符串转为时间戳
    public static long getMillTime(String user_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long l = 0;
        try {
            Date d = sdf.parse(user_time);
            l = d.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
        return l;
    }

    /**
     * 微信方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String wechat_time(String sdate) {
        Date time = new Date(Long.valueOf(sdate) * 1000);
        String ftime = "";
        Calendar cal = Calendar.getInstance();
        int hour = time.getHours();
        String apm;
        if (hour >= 12) {
            apm = "下午";
        } else if (hour >= 6) {
            apm = "上午";
        } else {
            apm = "凌晨";
        }
        SimpleDateFormat df = new SimpleDateFormat("h:mm", Locale.CHINESE);
        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            ftime = apm + df.format(time);
        } else if (days == 1) {
            ftime = "昨天 " + apm + df.format(time);
        } else if (days >= 2 && days <= 6) {
            SimpleDateFormat wf = new SimpleDateFormat("E ", Locale.CHINESE);
            // ftime = wf.format(time) + apm + df.format(time);
            ftime = wf.format(time) + apm + df.format(time);
        } else if (days >= 7) {
            SimpleDateFormat yf = new SimpleDateFormat("MM-dd ", Locale.CHINESE);

            // ftime = yf.format(time) + apm + df.format(time);
            ftime = yf.format(time) + apm + df.format(time);
        }
        return ftime;
    }

}
