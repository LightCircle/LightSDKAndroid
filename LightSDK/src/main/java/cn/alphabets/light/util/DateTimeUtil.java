package cn.alphabets.light.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期操作
 * Created by lin on 14/12/4.
 */
public class DateTimeUtil {

    /**
     * 格式化日期
     * @param date 日期
     * @param format 格式
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null || format == null) {
            return "";
        }

        DateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm");
    }

    public static String shortDate(Date date) {
        return format(date, "MM-dd");
    }

    public static String date(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String time(Date date) {
        return format(date, "HH:mm");
    }


}
