package je.techtribes.component.event;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Event;

import java.util.Date;
import java.util.List;

interface EventDao {

    List<Event> getFutureEvents(int pageSize);

    List<Event> getEventsByYear(int year);

    List<Event> getEvents(ContentSource contentSource, int pageSize);

    Event getEvent(int id);

    long getNumberOfEvents(ContentSource contentSource, Date start, Date end);

}
