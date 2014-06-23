package je.techtribes.component.job;

import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.domain.Island;
import je.techtribes.domain.Job;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

class JobResultExtractor implements ResultSetExtractor<Job> {

    @Override
    public Job extractData(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Job job = new Job(id);
        job.setTitle(rs.getString("title"));
        job.setDescription(rs.getString("description"));
        job.setIsland(Island.lookupByChar(rs.getString("island")));

        try {
            job.setUrl(new URL(rs.getString("url")));
        } catch (MalformedURLException e) {
            LoggingComponentFactory.create().warn(this, "Couldn't set URL for " + job.getTitle(), e);
        }

        job.setContentSourceId(rs.getInt("content_source_id"));
        job.setDatePosted(rs.getDate("date_posted"));

        return job;
    }

}
