package je.techtribes.component.job;

import je.techtribes.component.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.util.ContentItemFilter;
import je.techtribes.util.JdbcDatabaseConfiguration;

import java.util.List;

class JobComponentImpl extends AbstractComponent implements JobComponent {

    private JobDao jobDao;
    private ContentSourceComponent contentSourceComponent;

    JobComponentImpl(JdbcDatabaseConfiguration jdbcDatabaseConfiguration, ContentSourceComponent contentSourceComponent) {
        this.jobDao = new JdbcJobDao(jdbcDatabaseConfiguration);
        this.contentSourceComponent = contentSourceComponent;
    }

    @Override
    public List<Job> getRecentJobs(int pageSize) {
        try {
            List<Job> jobs = jobDao.getRecentJobs(pageSize);
            filterAndEnrich(jobs);

            return jobs;
        } catch (Exception e) {
            JobException jce = new JobException("Error getting recent jobs", e);
            logError(jce);
            throw jce;
        }
    }

    @Override
    public List<Job> getRecentJobs(ContentSource contentSource, int pageSize, boolean includeExpiredJobs) {
        try {
            List<Job> jobs = jobDao.getRecentJobs(contentSource, pageSize, includeExpiredJobs);
            filterAndEnrich(jobs);

            return jobs;
        } catch (Exception e) {
            JobException jce = new JobException("Error getting recent jobs for content source with ID " + contentSource.getId(), e);
            logError(jce);
            throw jce;
        }
    }

    private void filterAndEnrich(List<Job> jobs) {
        ContentItemFilter<Job> filter = new ContentItemFilter<>();
        filter.filter(jobs, contentSourceComponent, true);
    }

}
