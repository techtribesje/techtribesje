package je.techtribes.util;

import je.techtribes.domain.Event;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ICalendarFormatter {

    private static final String DATE_TIME_FORMAT = "yyyyMMdd'T'HHmmss";

    public String format(Event event) {
        StringBuilder buf = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));

        buf.append("BEGIN:VCALENDAR");
        buf.append(System.lineSeparator());

        buf.append("VERSION:2.0");
        buf.append(System.lineSeparator());

        buf.append("PRODID:techtribes.je");
        buf.append(System.lineSeparator());

        buf.append("BEGIN:VEVENT");
        buf.append(System.lineSeparator());

        buf.append("UID:").append(event.getId()).append("@techtribes.je");
        buf.append(System.lineSeparator());

        buf.append("DTSTART;TZID=Europe/London:").append(sdf.format(event.getStartDate()));
        buf.append(System.lineSeparator());

        if (event.getEndDate() != null) {
            buf.append("DTEND;TZID=Europe/London:").append(sdf.format(event.getEndDate()));
            buf.append(System.lineSeparator());
        }

        buf.append("SUMMARY:").append(event.getTitle());
        buf.append(System.lineSeparator());

        buf.append("DESCRIPTION:").append(event.getDescription()).append(" See ").append(event.getUrl()).append(" for more information.");
        buf.append(System.lineSeparator());

        buf.append("LOCATION:");
        if (event.getLocation() != null) {
            buf.append(event.getLocation()).append(", ");
        }
        buf.append(event.getIsland());

        buf.append(System.lineSeparator());
        buf.append("END:VEVENT");

        buf.append(System.lineSeparator());
        buf.append("END:VCALENDAR");

        return buf.toString();
    }

}
