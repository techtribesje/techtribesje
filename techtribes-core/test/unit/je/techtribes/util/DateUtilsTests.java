package je.techtribes.util;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateUtilsTests {

    private DateFormat dateFormat;

    @Before
    public void setUp() {
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
    }

    @Test
    public void test_getStartOfYear() {
        Date date = DateUtils.getStartOfYear(2013);
        assertEquals("01-Jan-2013 00:00:00:000", dateFormat.format(date));
   }

    @Test
    public void test_getEndOfYear() {
        Date date = DateUtils.getEndOfYear(2013);
        assertEquals("31-Dec-2013 23:59:59:999", dateFormat.format(date));
   }

    @Test
    public void test_getToday() {
        Date date = DateUtils.getToday();

        Calendar today = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        assertEquals(dateFormat.format(today.getTime()), dateFormat.format(date));
   }

    @Test
    public void test_getEndOfToday() {
        Date date = DateUtils.getEndOfToday();

        Calendar today = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        today.set(Calendar.MILLISECOND, 999);
        assertEquals(dateFormat.format(today.getTime()), dateFormat.format(date));
   }

    @Test
    public void test_getXDaysAgo_WhereXIsZero() {
        Date date = DateUtils.getXDaysAgo(0);

        Calendar today = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        today.add(Calendar.DAY_OF_MONTH, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        assertEquals(dateFormat.format(today.getTime()), dateFormat.format(date));
   }

    @Test
    public void test_getXDaysAgo_WhereXIsOne() {
        Date date = DateUtils.getXDaysAgo(1);

        Calendar today = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        today.add(Calendar.DAY_OF_MONTH, -1);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        assertEquals(dateFormat.format(today.getTime()), dateFormat.format(date));
   }

    @Test
    public void test_getXDaysAgoInUTC_WhereXIsZero() {
        Date date = DateUtils.getXDaysAgoInUTC(0);

        Calendar today = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));
        today.add(Calendar.DAY_OF_MONTH, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        assertEquals(dateFormat.format(today.getTime()), dateFormat.format(date));
   }

    @Test
    public void test_getXDaysAgoInUTC_WhereXIsOne() {
        Date date = DateUtils.getXDaysAgoInUTC(1);

        Calendar today = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));
        today.add(Calendar.DAY_OF_MONTH, -1);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        assertEquals(dateFormat.format(today.getTime()), dateFormat.format(date));
   }

    @Test
    public void test_getDate() {
        Date date = DateUtils.getDate(2013, 7, 1, 17, 30);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        sdf.setTimeZone(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));
        assertEquals("01-Jul-2013 16:30:00:000", sdf.format(date));
    }

    @Test
    public void test_getLastDayOfMonth() {
        assertEquals(31, DateUtils.getLastDayOfMonth(2014, 1));
        assertEquals(28, DateUtils.getLastDayOfMonth(2014, 2));
        assertEquals(31, DateUtils.getLastDayOfMonth(2014, 3));
    }

}