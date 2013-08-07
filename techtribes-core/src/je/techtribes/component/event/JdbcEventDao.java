package je.techtribes.component.event;

import je.techtribes.domain.ContentSource;
import je.techtribes.util.DateUtils;
import je.techtribes.util.JdbcDatabaseConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class JdbcEventDao implements EventDao {

    private DataSource dataSource;

    JdbcEventDao(JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.dataSource = jdbcDatabaseConfiguration.getDataSource();
    }

    @Override
    public List<Event> getFutureEvents(int pageSize) {
        Date today = DateUtils.getToday();
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<Event> events = select.query("select event.id, event.title, event.description, event.island, event.content_source_id, event.url, event.event_datetime from event, content_source where event.event_datetime > ? and event.content_source_id = content_source.id order by event_datetime asc limit 0,?",
                new Object[] { today, pageSize },
                new EventRowMapper());

        Collections.reverse(events); // reverse order because we want the latest event, first
        return events;
    }

    @Override
    public List<Event> getEventsByYear(int year) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        Date start = DateUtils.getStartOfYear(year);
        Date end = DateUtils.getEndOfYear(year);

        return select.query("select event.id, event.title, event.description, event.island, event.content_source_id, event.url, event.event_datetime from event, content_source where event_datetime between ? and ? and event.content_source_id = content_source.id order by event_datetime desc",
                new Object[]{start, end},
                new EventRowMapper());
    }

    @Override
    public List<Event> getEvents(ContentSource contentSource, int pageSize) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.query("select id, title, description, island, content_source_id, url, event_datetime from event where content_source_id = ? order by event_datetime desc limit 0,?",
            new Object[] { contentSource.getId(), pageSize },
            new EventRowMapper());
    }

    @Override
    public long getNumberOfEvents(ContentSource contentSource, Date start, Date end) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.queryForLong(
                "select count(*) from event where content_source_id = ? and event_datetime between ? and ?",
                contentSource.getId(), start, end);
    }

}
