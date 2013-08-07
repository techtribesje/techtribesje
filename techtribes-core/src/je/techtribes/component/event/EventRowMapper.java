package je.techtribes.component.event;

import je.techtribes.domain.Event;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int line) throws SQLException {
        EventResultExtractor extractor = new EventResultExtractor();
        return extractor.extractData(rs);
    }

}