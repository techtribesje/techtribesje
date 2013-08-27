package je.techtribes.util;

import je.techtribes.domain.Event;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Formats objects as an iCalendar, based upon RFC 2445 - http://www.ietf.org/rfc/rfc2445.txt
 */
public class ICalendarFormatter {

    private static final String DATE_TIME_FORMAT = "yyyyMMdd'T'HHmmss";

    public String format(Event event) {
        StringBuilder buf = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));

        addContentLine(buf, "BEGIN", "VCALENDAR");
        addContentLine(buf, "VERSION", "2.0");
        addContentLine(buf, "PRODID", "techtribes.je");
        addContentLine(buf, "BEGIN", "VEVENT");
        addContentLine(buf, "UID", event.getId() + "@techtribes.je");
        addContentLine(buf, "DTSTART;TZID=Europe/London", sdf.format(event.getStartDate()));

        if (event.getEndDate() != null) {
            addContentLine(buf, "DTEND;TZID=Europe/London", sdf.format(event.getEndDate()));
        }

        addContentLine(buf, "SUMMARY", event.getTitle());
        addContentLine(buf, "DESCRIPTION", event.getDescription() + " See " + event.getUrl() + " for more information.");

        if (event.getLocation() != null) {
            addContentLine(buf, "LOCATION", event.getLocation() + ", " + event.getIsland());
        }

        addContentLine(buf, "END", "VEVENT");
        addContentLine(buf, "END", "VCALENDAR");

        return buf.toString();
    }

    private void addContentLine(StringBuilder buf, String name, String value) {
        String line = name + ":" + value;
        String foldedLine = foldLineTo70Characters(line);

        buf.append(foldedLine).append("\r\n");
    }

    private String foldLineTo70Characters(String line) {
        StringBuilder buf = new StringBuilder();
        if (line.length() <= 70) {
            return line;
        } else {
            buf.append(line.substring(0, 70));
            buf.append("\r\n");
            buf.append(" ");
            buf.append(foldLineTo70Characters(line.substring(70)));

            return buf.toString();
        }
    }

}
