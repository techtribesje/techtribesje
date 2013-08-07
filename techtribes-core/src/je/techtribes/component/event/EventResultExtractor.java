package je.techtribes.component.event;

import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.domain.Event;
import je.techtribes.domain.Island;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

class EventResultExtractor implements ResultSetExtractor<Event> {

    @Override
    public Event extractData(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Event event = new Event(id);
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setIsland(Island.lookupByChar(rs.getString("island")));

        try {
            event.setUrl(new URL(rs.getString("url")));
        } catch (MalformedURLException e) {
            LoggingComponentFactory.create().warn(this, "Couldn't set URL for " + event.getId(), e);
        }

        event.setContentSourceId(rs.getInt("content_source_id"));
        event.setDate(rs.getTimestamp("event_datetime"));

        return event;
    }

}
