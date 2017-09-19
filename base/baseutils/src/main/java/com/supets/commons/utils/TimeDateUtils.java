package com.supets.commons.utils;

import android.text.format.Time;

import com.supets.commons.model.MYRemainTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeDateUtils {

    public static final double ONE_YEAR = 1000 * 3600 * 24 * 365d;
    public static final double ONE_DAY = 1000 * 3600 * 24d;
    public static final double ONE_HOUR = 1000 * 3600d;
    public static final double ONE_MINUTE = 1000 * 60d;

    public static long stringToMilliSeconds(String time) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date date = sf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public static Time stringToTime(String time) {
        Time t = new Time();
        t.set(stringToMilliSeconds(time));
        return t;
    }

    public static MYRemainTime formatRemainTime(long millis, boolean withoutDay) {
        MYRemainTime remainTime = formatRemainTime(millis);
        if (withoutDay) {
            remainTime.hour += (remainTime.day * 24);
            remainTime.day = 0;
        }
        return remainTime;
    }

    public static MYRemainTime formatRemainTime(long millis) {
        int day = (int) (millis / MYRemainTime.OneDay);
        millis %= MYRemainTime.OneDay;

        int hour = (int) (millis / MYRemainTime.OneHour);
        millis %= MYRemainTime.OneHour;

        int minute = (int) (millis / MYRemainTime.OneMinute);
        millis %= MYRemainTime.OneMinute;

        int second = (int) (millis / MYRemainTime.OneSecond);
        int msec = (int) (millis % MYRemainTime.OneSecond);

        return new MYRemainTime(day, hour, minute, second, msec);
    }

    public static String[] formatCountTime(long millis) {
        String[] countTime = new String[3];
        int h = (int) (millis / ONE_HOUR);
        int m = (int) ((millis % ONE_HOUR) / ONE_MINUTE);
        int s = (int) (((millis % ONE_HOUR) % ONE_MINUTE) / 1000);
        countTime[0] = formatNumberTwo(h);
        countTime[1] = formatNumberTwo(m);
        countTime[2] = formatNumberTwo(s);

        return countTime;
    }

    public static String formatNumberTwo(int number) {
        return number > 9 ? Integer.toString(number) : ("0" + number);
    }

    private static boolean isSameDay(Time lhs, Time rhs) {
        return (lhs.year == rhs.year) && (lhs.month == rhs.month) && (lhs.monthDay == rhs.monthDay);
    }

    public static boolean isToday(Time current, Time time) {
        return isSameDay(current, time);
    }

    public static boolean isToday(Time time) {
        Time current = new Time();
        current.setToNow();
        return isSameDay(current, time);
    }

    public static boolean isTomorrow(Time time) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        Time tomorrow = new Time();
        tomorrow.set(c.getTimeInMillis());
        return isSameDay(tomorrow, time);
    }

    public static boolean isTheDayAfterTomorrow(Time time) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 2);
        Time tomorrow = new Time();
        tomorrow.set(c.getTimeInMillis());
        return isSameDay(tomorrow, time);
    }

    public static Time getYesterday() {
        Time current = new Time();
        current.setToNow();
        long time = current.normalize(true) - 24 * 3600 * 1000;
        Time yesterday = new Time();
        yesterday.set(time);
        yesterday.normalize(true);
        return yesterday;
    }

    private static boolean isYesterday(Time time) {
        Time yesterday = getYesterday();
        return isSameDay(time, yesterday);
    }

    private static boolean isInThisWeek(Time current, Time time) {
        return (current.year == time.year) && (current.getWeekNumber() == time.getWeekNumber());
    }

    /**
     * 格式化结束时间显示（下一年）
     *
     * @param strTime
     * @param regix
     * @return
     */
    public static String formatEndTime(String strTime, String regix) {
        if (strTime == null) {
            return "";
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        SimpleDateFormat sf2 = new SimpleDateFormat(regix, Locale.CHINA);
        try {
            Date date = sf.parse(strTime);
            if (isNextYear(date.getTime())) {
                return formatTime(strTime, regix);
            }
            return sf2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatTimeMDHM_CN(String strTime) {
        return formatEndTime(strTime, "MM月dd日 HH:mm");
    }

    public static String formatTimeYMDHM_(String strTime) {
        return formatTime(strTime, "yyyy-MM-dd HH:mm");
    }

    public static String formatTimeYMDHM_CN(String strTime) {
        return formatTime(strTime, "yyyy年MM月dd日 HH:mm");
    }

    private static String formatTime(String strTime, String regix) {
        if (strTime == null) {
            return "";
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        SimpleDateFormat sf2 = new SimpleDateFormat(regix, Locale.CHINA);
        try {
            Date date = sf.parse(strTime);
            return sf2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatDateToLocale(Date time) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            return sf.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNextYear(long time) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(new Date());
        int nowyear = cale.get(Calendar.YEAR);
        cale.setTime(new Date(time));
        int newyear = cale.get(Calendar.YEAR);
        return nowyear < newyear;
    }

    /**
     * 格式化php的long时间戳-->标准的字符串时间
     *
     * @param strTime
     * @return
     */
    public static String formatLong9ToLocale(String strTime) {
        if (strTime == null) {
            return "";
        }
        try {
            return formatLong9ToLocale(Long.valueOf(strTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatLong9ToLocale(long strTime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = new Date(strTime * 1000);
            return sf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatRecordTime(int time) {
        int yu = (int) (time % 60);
        return time / 60 + ":" + yu / 10 + "" + yu % 10;

    }

    public static String formatRecordTime2(int time) {
        if (time==0){
            time=1;
        }
        int yu =time % 60;
        return yu / 10 + "" + yu % 10+"\"";
    }
//
//    public static String formatGrouponStartTime(String strTime) {
//        if (strTime == null) {
//            return "";
//        }
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//
//        String format= MiaTextUtils.getString(R.string.groupon_start_time);
//
//        SimpleDateFormat sf2 = new SimpleDateFormat(format, Locale.CHINA);
//        try {
//            Date date = sf.parse(strTime);
//            return sf2.format(date);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}
