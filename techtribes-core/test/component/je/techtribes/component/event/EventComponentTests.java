package je.techtribes.component.event;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.*;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EventComponentTests extends AbstractComponentTestsBase {

    private int currentYear;

    @Before
    public void setUp() {
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        JdbcTemplate template = getJdbcTemplate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        for (int i = 1; i <= 12; i++) {
            template.update("insert into event (title, description, island, location, content_source_id, url, start_datetime, end_datetime) values (?, ?, ?, ?, ?, ?, ?, ?)",
                    "Event " + i,
                    "Here is a description for event " + i,
                    "j",
                    "Ha'Penny Bridge",
                    4,
                    "http://techtribes.je/event" + i,
                    sdf.format(DateUtils.getDate(currentYear - 1, i, 1, 17, 30)),
                    sdf.format(DateUtils.getDate(currentYear - 1, i, 1, 20, 30))
                    );
        }
        for (int i = 1; i <= 12; i++) {
            template.update("insert into event (title, description, island, content_source_id, url, start_datetime) values (?, ?, ?, ?, ?, ?)",
                    "Event " + (12+i),
                    "Here is a description for event " + (12+i),
                    "g",
                    4,
                    "http://techtribes.je/event" + (12+i),
                    sdf.format(DateUtils.getDate(currentYear + 1, i, 1, 17, 30)));
        }

        // and add an orphaned event (missing content source, so will be filtered)
        template.update("insert into event (title, description, island, content_source_id, url, start_datetime) values (?, ?, ?, ?, ?, ?)",
                "Event X",
                "Here is a description for event X",
                "j",
                99,
                "http://x.com/event",
                sdf.format(DateUtils.getDate(currentYear+1, 1, 1, 17, 30)));
    }

    @After
    public void tearDown() {
        JdbcTemplate template = getJdbcTemplate();
        template.execute("delete from event");
    }

    @Test
    public void testGetNumberOfEvents_ReturnsZero_WhenThereAreNoEvents() {
        assertEquals(0, getEventComponent().getNumberOfEvents(getContentSourceComponent().findByShortName("techtribesje"), DateUtils.getStartOfYear(currentYear-2), DateUtils.getEndOfYear(currentYear-2)));
    }

    @Test
    public void testGetNumberOfEvents_ReturnsNonZero_WhenThereAreEvents() {
        assertEquals(12, getEventComponent().getNumberOfEvents(getContentSourceComponent().findByShortName("techtribesje"), DateUtils.getStartOfYear(currentYear-1), DateUtils.getEndOfYear(currentYear-1)));
    }

    @Test
    public void test_eventDetailsAreCorrectlyRetrievedFromTheDatabase() {
        List<Event> events = getEventComponent().getEvents(getContentSourceComponent().findByShortName("techtribesje"), 24);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        sdf.setTimeZone(TimeZone.getTimeZone(DateUtils.APPLICATION_TIME_ZONE));

        Event event = events.get(0);
        assertEquals("Event 1", event.getTitle());
        assertEquals("Here is a description for event 1", event.getBody());
        assertEquals(Island.Jersey, event.getIsland());
        assertEquals("Ha'Penny Bridge", event.getLocation());
        assertEquals("techtribes.je", event.getContentSource().getName());
        assertEquals("http://techtribes.je/event1", event.getUrl().toString());
        assertEquals("01-Jan-" + (currentYear-1) + " 17:30:00:000", sdf.format(event.getStartDate()));
        assertEquals("01-Jan-" + (currentYear-1) + " 20:30:00:000", sdf.format(event.getEndDate()));

        event = events.get(23);
        assertEquals("Event 24", event.getTitle());
        assertEquals("Here is a description for event 24", event.getBody());
        assertEquals(Island.Guernsey, event.getIsland());
        assertEquals("techtribes.je", event.getContentSource().getName());
        assertEquals("http://techtribes.je/event24", event.getUrl().toString());
        assertEquals("01-Dec-" + (currentYear+1) + " 17:30:00:000", sdf.format(event.getStartDate()));
    }

    @Test
    public void test_getFutureEvents() {
        List<Event> events = getEventComponent().getFutureEvents(6);
        assertEquals(6, events.size());

        // Event 18, 17, 16, 15, 14, 13
        for (int i = 0; i < 6; i++) {
            Event event = events.get(i);
            assertEquals("Event " + (12+(6-i)), event.getTitle());
            assertEquals("techtribes.je", event.getContentSource().getName());
        }
    }

    @Test
    public void test_getEventsForContentSource() {
        List<Event> events = getEventComponent().getEvents(getContentSourceComponent().findByShortName("techtribesje"), 12);
        assertEquals(12, events.size());

        // Event 1 - 12
        for (int i = 0; i < 12; i++) {
            Event event = events.get(i);
            assertEquals("Event " + (i+1), event.getTitle());
            assertEquals("techtribes.je", event.getContentSource().getName());
        }
    }

    @Test
    public void test_getEventsByYear_ReturnsEmptyCollection_WhenThereAreNoEvents() {
        assertTrue(getEventComponent().getEventsByYear(2000).isEmpty());
    }

    @Test
    public void test_getEventsByYear_ReturnsSomeEvents_WhenThereAreSomeEvents() {
        List<Event> events = getEventComponent().getEventsByYear(currentYear + 1);
        assertEquals(12, events.size());

        // Event 13 - 24
        for (int i = 0; i < 12; i++) {
            Event event = events.get(i);
            assertEquals("Event " + (12+i+1), event.getTitle());
            assertEquals("techtribes.je", event.getContentSource().getName());
        }
    }

}
