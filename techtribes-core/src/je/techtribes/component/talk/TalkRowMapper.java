package je.techtribes.component.talk;

import je.techtribes.domain.Talk;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class TalkRowMapper implements RowMapper<Talk> {

    @Override
    public Talk mapRow(ResultSet rs, int line) throws SQLException {
        TalkResultExtractor extractor = new TalkResultExtractor();
        return extractor.extractData(rs);
    }

}