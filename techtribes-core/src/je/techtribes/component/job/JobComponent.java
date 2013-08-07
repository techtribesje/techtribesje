package je.techtribes.component.job;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Job;

import java.util.List;

/**
 * Provides access to information about recently posted jobs.
 */
@Component
public interface JobComponent {

    /**
     * Gets a list of the most recently posted jobs, latest first.
     */
    List<Job> getRecentJobs(int pageSize);

    /**
     * Gets a list of the most recently posted jobs, latest first, for the given ContentSource.
     * Optionally, expired jobs can be included.
     */
    List<Job> getRecentJobs(ContentSource contentSource, int pageSize, boolean includeExpiredJobs);

}
