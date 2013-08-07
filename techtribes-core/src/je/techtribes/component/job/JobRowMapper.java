package je.techtribes.component.job;

import je.techtribes.domain.Job;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class JobRowMapper implements RowMapper<Job> {

    @Override
    public Job mapRow(ResultSet rs, int line) throws SQLException {
        JobResultExtractor extractor = new JobResultExtractor();
        return extractor.extractData(rs);
    }

}