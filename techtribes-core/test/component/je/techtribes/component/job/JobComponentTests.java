package je.techtribes.component.job;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.Island;
import je.techtribes.domain.Job;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class JobComponentTests extends AbstractComponentTestsBase {

    @Before
    public void setUp() {
        JdbcTemplate template = getJdbcTemplate();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));
        for (int i = 30; i >= 1; i--) {
            addJobIntoDatabase(template,
                    "Job " + i,
                    "Here is a description for job " + i,
                    "j",
                    3,
                    "http://www.prosperity247.com/job" + i,
                    cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
    }

    private void addJobIntoDatabase(JdbcTemplate template, String title, String description, String island, int contentSourceId, String url, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        template.update("insert into job (title, description, island, content_source_id, url, date_posted) values (?, ?, ?, ?, ?, ?)",
                title,
                description,
                island,
                contentSourceId,
                url,
                sdf.format(date));
    }

    @After
    public void tearDown() {
        JdbcTemplate template = getJdbcTemplate();
        template.execute("delete from job");
    }

    @Test
    public void test_getRecentJobsForContentSource_ReturnsEmptyList_WhenThereAreNoJobs() {
        List<Job> jobs = getJobComponent().getRecentJobs(getContentSourceComponent().findByShortName("techtribesje"), 100, true);
        assertEquals(0, jobs.size());
    }

    @Test
    public void test_getRecentJobsForContentSource_ReturnsAllJobs_WhenThereAreJobsAndWeWantExpiredJobs() {
        addJobIntoDatabase(getJdbcTemplate(),
                "Another job",
                "Another job description",
                "j",
                getContentSourceComponent().findByShortName("techtribesje").getId(),
                "http://techtribes.je/anotherjob",
                Calendar.getInstance(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE)).getTime());

        List<Job> jobs = getJobComponent().getRecentJobs(getContentSourceComponent().findByShortName("prosperity247"), 100, true);
        assertEquals(30, jobs.size());

        int i = 30;
        for (Job job : jobs) {
            assertEquals("Job " + i, job.getTitle());
            i--;
        }
    }

    @Test
    public void test_getRecentJobsForContentSource_ReturnsSomeJobs_WhenThereAreJobsAndWeDontWantExpiredJobs() {
        addJobIntoDatabase(getJdbcTemplate(),
                "Another job",
                "Another job description",
                "j",
                getContentSourceComponent().findByShortName("techtribesje").getId(),
                "http://techtribes.je/anotherjob",
                Calendar.getInstance(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE)).getTime());

        List<Job> jobs = getJobComponent().getRecentJobs(getContentSourceComponent().findByShortName("prosperity247"), 100, false);
        assertEquals(29, jobs.size()); // 28 in the past, plus 1 job today

        int i = 30;
        for (Job job : jobs) {
            assertEquals("Job " + i, job.getTitle());
            i--;
        }
    }

    @Test
    public void test_getRecentJobsForContentSource_ReturnsAPageOfJobs_WhenThereAreJobsAndWeWantAPageOfJobs() {
        addJobIntoDatabase(getJdbcTemplate(),
                "Another job",
                "Another job description",
                "j",
                getContentSourceComponent().findByShortName("techtribesje").getId(),
                "http://techtribes.je/anotherjob",
                Calendar.getInstance(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE)).getTime());

        List<Job> jobs = getJobComponent().getRecentJobs(getContentSourceComponent().findByShortName("prosperity247"), 10, false);
        assertEquals(10, jobs.size());

        int i = 30;
        for (Job job : jobs) {
            assertEquals("Job " + i, job.getTitle());
            i--;
        }
    }

    @Test
    public void test_getRecentJobs_ReturnsAPageOfJobs_WhenThereAreJobsAndWeWantAPageOfJobs() {
        List<Job> jobs = getJobComponent().getRecentJobs(10);
        assertEquals(10, jobs.size());

        int i = 30;
        for (Job job : jobs) {
            assertEquals("Job " + i, job.getTitle());
            i--;
        }
    }

    @Test
    public void test_jobDetailsAreCorrectlyRetrievedFromTheDatabase() {
        List<Job> jobs = getJobComponent().getRecentJobs(getContentSourceComponent().findByShortName("prosperity247"), 1, false);
        Job job = jobs.get(0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        assertEquals("Job 30", job.getTitle());
        assertEquals("Here is a description for job 30", job.getDescription());
        assertEquals("http://www.prosperity247.com/job30", job.getUrl().toString());
        assertEquals(Island.Jersey, job.getIsland());
        assertEquals(sdf.format(new Date()), sdf.format(job.getDatePosted()));
        assertEquals("Prosperity 24.7", job.getContentSource().getName());
    }

}
