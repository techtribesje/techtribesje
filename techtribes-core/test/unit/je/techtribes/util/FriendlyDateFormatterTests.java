package je.techtribes.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class FriendlyDateFormatterTests {

    private FriendlyDateFormatter df;

    @Before
    public void setUp() {
        df = new FriendlyDateFormatter();
    }

    @Test
    public void testFriendlyFormatWithoutTime_ReturnsToday_WhenDateIsToday() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));

        assertEquals("Today", df.formatAsDateWithoutTime(cal.getTime()));
    }

    @Test
    public void testFriendlyFormatWithTime_ReturnsTodayAndTime_WhenDateIsToday() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 30);

        assertEquals("Today at 14:30", df.formatAsDateWithTime(cal.getTime()));
    }

    @Test
    public void testFriendlyFormatWithoutTime_ReturnsYesterday_WhenDateIsYesterday() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.add(Calendar.DAY_OF_YEAR, -1);

        assertEquals("Yesterday", df.formatAsDateWithoutTime(cal.getTime()));
    }

    @Test
    public void testFriendlyFormatWithTime_ReturnsYesterdayAndTime_WhenDateIsYesterday() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 30);

        assertEquals("Yesterday at 14:30", df.formatAsDateWithTime(cal.getTime()));
    }

    @Test
    public void testFriendlyFormatWithoutTime_ReturnsTomorrow_WhenDateIsTomorrow() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        assertEquals("Tomorrow", df.formatAsDateWithoutTime(cal.getTime()));
    }

    @Test
    public void testFriendlyFormatWithTime_ReturnsTomorrowAndTime_WhenDateIsTomorrow() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 30);

        assertEquals("Tomorrow at 14:30", df.formatAsDateWithTime(cal.getTime()));
    }

    @Test
    public void testFriendlyFormatWithoutTime_ReturnsDate_WhenDateIsInThePast() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 06);
        cal.set(Calendar.DAY_OF_MONTH, 14);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        assertEquals("Sat 14 Jul 2012", df.formatAsDateWithoutTime(cal.getTime()));
    }

    @Test
    public void testFriendlyFormatWithTime_ReturnsDateAndTime_WhenDateIsInThePastAndThereIsATimeComponent() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 06);
        cal.set(Calendar.DAY_OF_MONTH, 14);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 30);

        assertEquals("Sat 14 Jul 2012 at 14:30", df.formatAsDateWithTime(cal.getTime()));
    }

}
