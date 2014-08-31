package je.techtribes.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date formatter used to render dates as (for example), "Today at 14:30:00".
 */
public class FriendlyDateFormatter {

    private static final String DATE_FORMAT = "EEE dd MMM yyyy";
    private static final String TIME_FORMAT = "HH:mm";

    public String formatAsDateWithTime(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.setTime(date);

        String formattedDate = formatDate(cal);
        String formattedTime = formatTime(cal);

        return formattedDate + " at " + formattedTime;
    }

    public String formatAsDateWithoutTime(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        cal.setTime(date);

        return formatDate(cal);
    }

    private String formatDate(Calendar cal) {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        } else if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                   cal.get(Calendar.DAY_OF_YEAR) == (today.get(Calendar.DAY_OF_YEAR)-1)) {
            return "Yesterday";
        } else if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                   cal.get(Calendar.DAY_OF_YEAR) == (today.get(Calendar.DAY_OF_YEAR)+1)) {
            return "Tomorrow";
        } else {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setTimeZone(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
            return dateFormat.format(cal.getTime());
        }
    }

    private String formatTime(Calendar cal) {
        DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));
        return dateFormat.format(cal.getTime());
    }

}
