package je.techtribes.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utilities for calculating various points in time.
 */
public class DateUtils {

    public static final String APPLICATION_TIME_ZONE = "Europe/London";
    public static final String UTC_TIME_ZONE = "UTC";

    public static Date getNow() {
        return Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE)).getTime();
    }

    public static int getYear() {
        return Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE)).get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE)).get(Calendar.MONTH)+1;
    }

    public static Date getToday() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getEndOfToday() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    public static Date getStartOfYear(int year) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Calendar getStartOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    public static Calendar getEndOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, getLastDayOfMonth(year, month));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal;
    }

    public static Date getEndOfYear(int year) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    public static Date getXDaysAgo(int numberOfDays) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.add(Calendar.DAY_OF_MONTH, -numberOfDays);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getXDaysAgoInUTC(int numberOfDays) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(UTC_TIME_ZONE));
        cal.add(Calendar.DAY_OF_MONTH, -numberOfDays);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        return getDate(year, month, day, 0, 0);
    }

    public static int getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 12);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Calendar getCalendar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 12);

        return cal;
    }

    public static String format(String format, Calendar cal) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone(APPLICATION_TIME_ZONE));

        return dateFormat.format(cal.getTime());
    }

}
