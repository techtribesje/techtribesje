package je.techtribes.component.event;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Event;

import java.util.Date;
import java.util.List;

/**
 * Provides access to information about local events (e.g. meetups, user groups, etc).
 */
@Component
public interface EventComponent {

    /**
     * Gets a list of events that are in the future, ordered by latest first.
     */
    List<Event> getFutureEvents(int pageSize);

    /**
     * Gets a list of events in a given year, ordered by latest first.
     */
    List<Event> getEventsByYear(int year);

    /**
     * Gets a list of events for the given ContentSource, ordered by latest first.
     */
    List<Event> getEvents(ContentSource contentSource, int pageSize);

    /**
     * Gets the number of events for a given ContentSource, between the specified dates, ordered by latest first.
     */
    long getNumberOfEvents(ContentSource contentSource, Date start, Date end);

}
