package je.techtribes.component.job;

import je.techtribes.domain.ContentSource;
import je.techtribes.util.DateUtils;
import je.techtribes.util.JdbcDatabaseConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

class JdbcJobDao implements JobDao {

    private DataSource dataSource;

    JdbcJobDao(JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.dataSource = jdbcDatabaseConfiguration.getDataSource();
    }

    @Override
    public List<Job> getRecentJobs(int pageSize) {
        Date xDaysAgo = DateUtils.getXDaysAgoInUTC(Job.JOB_LIFESPAN_IN_DAYS);
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.query("select job.id, job.title, job.description, job.island, job.content_source_id, job.url, job.date_posted from job, content_source where date_posted >= ? and job.content_source_id = content_source.id order by date_posted desc limit 0,?",
                new Object[] { xDaysAgo, pageSize },
                new JobRowMapper());
    }

    @Override
    public List<Job> getRecentJobs(ContentSource contentSource, int pageSize, boolean includeExpiredJobs) {
        Date xDaysAgo = DateUtils.getXDaysAgoInUTC(Job.JOB_LIFESPAN_IN_DAYS);
        JdbcTemplate select = new JdbcTemplate(dataSource);
        if (includeExpiredJobs) {
            return select.query("select id, title, description, island, content_source_id, url, date_posted from job where content_source_id = ? order by date_posted desc limit 0,?",
                new Object[] { contentSource.getId(), pageSize },
                new JobRowMapper());
        } else {
            return select.query("select id, title, description, island, content_source_id, url, date_posted from job where content_source_id = ? and date_posted >= ? order by date_posted desc limit 0,?",
                new Object[] { contentSource.getId(), xDaysAgo, pageSize },
                new JobRowMapper());
        }
    }

}
