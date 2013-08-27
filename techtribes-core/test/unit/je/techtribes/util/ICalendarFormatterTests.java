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
                "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:techtribes.je\n" +
                "BEGIN:VEVENT\n" +
                "UID:123@techtribes.je\n" +
                "DTSTART;TZID=Europe/London:20130829T163000\n" +
                "DTEND;TZID=Europe/London:20130829T190000\n" +
                "SUMMARY:Tech Tribes meetup 17\n" +
                "DESCRIPTION:Tech talk and beer (or wine or a soft drink or two) with Jersey's techy community. See http://lanyrd.com/2013/tech-tribes-meetup-17/ for more information.\n" +
                "LOCATION:Ha'Penny Bridge, St Helier, Jersey\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR", formatter.format(event));
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
                "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:techtribes.je\n" +
                "BEGIN:VEVENT\n" +
                "UID:123@techtribes.je\n" +
                "DTSTART;TZID=Europe/London:20130829T163000\n" +
                "SUMMARY:Tech Tribes meetup 17\n" +
                "DESCRIPTION:Tech talk and beer (or wine or a soft drink or two) with Jersey's techy community. See http://lanyrd.com/2013/tech-tribes-meetup-17/ for more information.\n" +
                "LOCATION:Ha'Penny Bridge, St Helier, Jersey\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR", formatter.format(event));
   }

    @Test
    public void test_format_ReturnsAnICalendarRecordWithoutFullLocation_WhenEventLocationIsntPopulated() throws Exception {
        ICalendarFormatter formatter = new ICalendarFormatter();
        Event event = new Event(123);
        event.setTitle("Tech Tribes meetup 17");
        event.setDescription("Tech talk and beer (or wine or a soft drink or two) with Jersey's techy community.");
        event.setIsland(Island.Jersey);
        event.setUrl(new URL("http://lanyrd.com/2013/tech-tribes-meetup-17/"));
        event.setStartDate(DateUtils.getDate(2013, 8, 29, 16, 30));
        event.setEndDate(DateUtils.getDate(2013, 8, 29, 19, 00));

        assertEquals(
                "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:techtribes.je\n" +
                "BEGIN:VEVENT\n" +
                "UID:123@techtribes.je\n" +
                "DTSTART;TZID=Europe/London:20130829T163000\n" +
                "DTEND;TZID=Europe/London:20130829T190000\n" +
                "SUMMARY:Tech Tribes meetup 17\n" +
                "DESCRIPTION:Tech talk and beer (or wine or a soft drink or two) with Jersey's techy community. See http://lanyrd.com/2013/tech-tribes-meetup-17/ for more information.\n" +
                "LOCATION:Jersey\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR", formatter.format(event));
   }

}