package je.techtribes.util;

import je.techtribes.domain.Event;
import je.techtribes.domain.Island;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ICalendarFormatterTests {

    @Test
    public void test_format_ReturnsAFullICalendarRecord_WhenAllEventAttributesArePopulated() throws Exception {
        ICalendarFormatter formatter = new ICalendarFormatter();
        Event event = new Event(123);
        event.setTitle("Tech Tribes meetup 17");
        event.setDescription("Tech talk and beer (or wine or a soft drink or two) with Jersey's techy community.");
        event.setIsland(Island.Jersey);
        event.setLocation("Ha'Penny Bridge, St Helier");
        event.setUrl(new URL("http://lanyrd.com/2013/tech-tribes-meetup-17/"));
        event.setStartDate(DateUtils.getDate(2013, 8, 29, 16, 30));
        event.setEndDate(DateUtils.getDate(2013, 8, 29, 19, 00));

        assertEquals(
                "BEGIN:VCALENDAR\r\n" +
                "VERSION:2.0\r\n" +
                "PRODID:techtribes.je\r\n" +
                "BEGIN:VEVENT\r\n" +
                "UID:123@techtribes.je\r\n" +
                "DTSTART;TZID=Europe/London:20130829T163000\r\n" +
                "DTEND;TZID=Europe/London:20130829T190000\r\n" +
                "SUMMARY:Tech Tribes meetup 17\r\n" +
                "DESCRIPTION:Tech talk and beer (or wine or a soft drink or two) with J\r\n ersey's techy community.\r\n" +
                "URL:http://lanyrd.com/2013/tech-tribes-meetup-17/\r\n" +
                "LOCATION:Ha'Penny Bridge\\, St Helier\\, Jersey\r\n" +
                "END:VEVENT\r\n" +
                "END:VCALENDAR\r\n", formatter.format(event));
   }

    @Test
    public void test_format_ReturnsAnICalendarRecordWithoutEndDate_WhenEventEndDateIsntPopulated() throws Exception {
        ICalendarFormatter formatter = new ICalendarFormatter();
        Event event = new Event(123);
        event.setTitle("Tech Tribes meetup 17");
        event.setDescription("Tech talk and beer (or wine or a soft drink or two) with Jersey's techy community.");
        event.setIsland(Island.Jersey);
        event.setLocation("Ha'Penny Bridge, St Helier");
        event.setUrl(new URL("http://lanyrd.com/2013/tech-tribes-meetup-17/"));
        event.setStartDate(DateUtils.getDate(2013, 8, 29, 16, 30));

        assertEquals(
                "BEGIN:VCALENDAR\r\n" +
                "VERSION:2.0\r\n" +
                "PRODID:techtribes.je\r\n" +
                "BEGIN:VEVENT\r\n" +
                "UID:123@techtribes.je\r\n" +
                "DTSTART;TZID=Europe/London:20130829T163000\r\n" +
                "SUMMARY:Tech Tribes meetup 17\r\n" +
                "DESCRIPTION:Tech talk and beer (or wine or a soft drink or two) with J\r\n ersey's techy community.\r\n" +
                "URL:http://lanyrd.com/2013/tech-tribes-meetup-17/\r\n" +
                "LOCATION:Ha'Penny Bridge\\, St Helier\\, Jersey\r\n" +
                "END:VEVENT\r\n" +
                "END:VCALENDAR\r\n", formatter.format(event));
   }

    @Test
    public void test_format_ReturnsAnICalendarRecordWithoutLocation_WhenEventLocationIsntPopulated() throws Exception {
        ICalendarFormatter formatter = new ICalendarFormatter();
        Event event = new Event(123);
        event.setTitle("Tech Tribes meetup 17");
        event.setDescription("Tech talk and beer (or wine or a soft drink or two) with Jersey's techy community.");
        event.setIsland(Island.Jersey);
        event.setUrl(new URL("http://lanyrd.com/2013/tech-tribes-meetup-17/"));
        event.setStartDate(DateUtils.getDate(2013, 8, 29, 16, 30));
        event.setEndDate(DateUtils.getDate(2013, 8, 29, 19, 00));

        assertEquals(
                "BEGIN:VCALENDAR\r\n" +
                "VERSION:2.0\r\n" +
                "PRODID:techtribes.je\r\n" +
                "BEGIN:VEVENT\r\n" +
                "UID:123@techtribes.je\r\n" +
                "DTSTART;TZID=Europe/London:20130829T163000\r\n" +
                "DTEND;TZID=Europe/London:20130829T190000\r\n" +
                "SUMMARY:Tech Tribes meetup 17\r\n" +
                "DESCRIPTION:Tech talk and beer (or wine or a soft drink or two) with J\r\n ersey's techy community.\r\n" +
                "URL:http://lanyrd.com/2013/tech-tribes-meetup-17/\r\n" +
                "END:VEVENT\r\n" +
                "END:VCALENDAR\r\n", formatter.format(event));
   }

}