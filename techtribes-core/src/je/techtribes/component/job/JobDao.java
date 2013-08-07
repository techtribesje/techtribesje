package je.techtribes.component.job;

import je.techtribes.domain.ContentSource;

import java.util.List;

interface JobDao {

    public List<Job> getRecentJobs(int pageSize);

    public List<Job> getRecentJobs(ContentSource contentSource, int pageSize, boolean includeExpiredJobs);

}
