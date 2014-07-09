package je.techtribes.component.activity;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.Activity;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Person;
import je.techtribes.domain.Tribe;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class ActivityComponentTests extends AbstractComponentTestsBase {

    private SimpleDateFormat dateTimeFormat;

    @Before
    public void setUp() {
        JdbcTemplate template = getJdbcTemplate();
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        // activity records for today
        for (ContentSource contentSource : getContentSourceComponent().getPeopleAndTribes()) {
            int id = contentSource.getId();
            template.update("insert into activity (content_source_id, international_talks, local_talks, content, tweets, events, last_activity_datetime, activity_datetime) values (?, ?, ?, ?, ?, ?, ?, ?)",
                    id,
                    contentSource.isPerson() ? id * 1 : 0,
                    contentSource.isPerson() ? id * 2 : 0,
                    id * 3,
                    id * 4,
                    contentSource.isTribe() ? id * 5 : 0,
                    dateTimeFormat.format(DateUtils.getDate(2013, 7, 1, 12+id, 0)),
                    dateTimeFormat.format(DateUtils.getToday())
                    );
        }

        // and also some for yesterday (these will be ignored)
        for (ContentSource contentSource : getContentSourceComponent().getPeopleAndTribes()) {
            template.update("insert into activity (content_source_id, international_talks, local_talks, content, tweets, events, last_activity_datetime, activity_datetime) values (?, ?, ?, ?, ?, ?, ?, ?)",
                    contentSource.getId(),
                    0,
                    0,
                    0,
                    0,
                    0,
                    dateTimeFormat.format(DateUtils.getDate(2013, 7, 1, 9, 0)),
                    dateTimeFormat.format(DateUtils.getXDaysAgo(1))
                    );
        }
    }

    @Test
    public void test_getRecentActivity_ReturnsTheMostRecentActivityRecords() {
        getActivityComponent().refreshRecentActivity();
        List<Activity> activityListForPeople = getActivityComponent().getActivityListForPeople();
        assertEquals(2, activityListForPeople.size());
        assertEquals("Chris Clark", activityListForPeople.get(0).getContentSource().getName());
        assertEquals("Simon Brown", activityListForPeople.get(1).getContentSource().getName());

        List<Activity> activityListForBusinessTribes = getActivityComponent().getActivityListForBusinessTribes();
        assertEquals(1, activityListForBusinessTribes.size());
        assertEquals("Prosperity 24.7", activityListForBusinessTribes.get(0).getContentSource().getName());

        List<Activity> activityListForCommunityTribes = getActivityComponent().getActivityListForCommunityTribes();
        assertEquals(1, activityListForCommunityTribes.size());
        assertEquals("techtribes.je", activityListForCommunityTribes.get(0).getContentSource().getName());
    }

    @Test
    public void test_getActivityForContentSource_ReturnsTheCorrectInformationFromTheDatabase()
    {
        ContentSource contentSource = getContentSourceComponent().findByShortName("chrisclark");
        int id = contentSource.getId();
        getActivityComponent().refreshRecentActivity();

        Activity activity = getActivityComponent().getActivity(contentSource);
        assertEquals("Chris Clark", activity.getContentSource().getName());
        assertEquals(id*1, activity.getNumberOfInternationalTalks());
        assertEquals(id*2, activity.getNumberOfLocalTalks());
        assertEquals(id*3, activity.getNumberOfNewsFeedEntries());
        assertEquals(id*4, activity.getNumberOfTweets());
        assertEquals(0, activity.getNumberOfEvents());
        assertEquals("2013-07-01 13:00:00", dateTimeFormat.format(activity.getLastActivityDate())); // UTC date
    }

    @Test
    public void test_getActivityForContentSource_ReturnsAggregatedActivity_WhenThereAreMembersOfATribe() {
        Person chrisclark = (Person)getContentSourceComponent().findByShortName("chrisclark");
        Tribe p247 = (Tribe)getContentSourceComponent().findByShortName("prosperity247");
        getActivityComponent().refreshRecentActivity();

        Activity activityForChrisClark = getActivityComponent().getActivity(chrisclark);
        assertEquals(2, activityForChrisClark.getNumberOfInternationalTalks());
        assertEquals(4, activityForChrisClark.getNumberOfLocalTalks());
        assertEquals(6, activityForChrisClark.getNumberOfNewsFeedEntries());
        assertEquals(8, activityForChrisClark.getNumberOfTweets());
        assertEquals(0, activityForChrisClark.getNumberOfEvents());

        Activity activityForP247 = getActivityComponent().getActivity(p247);
        assertEquals(0, activityForP247.getNumberOfInternationalTalks());
        assertEquals(0, activityForP247.getNumberOfLocalTalks());
        assertEquals(9, activityForP247.getNumberOfNewsFeedEntries());
        assertEquals(12, activityForP247.getNumberOfTweets());
        assertEquals(15, activityForP247.getNumberOfEvents());

        Set<Integer> personIds = new HashSet<>();
        personIds.add(chrisclark.getId());
        getContentSourceComponent().updateTribeMembers(p247, personIds);

        getActivityComponent().refreshRecentActivity();

        activityForChrisClark = getActivityComponent().getActivity(chrisclark);
        assertEquals(2, activityForChrisClark.getNumberOfInternationalTalks());
        assertEquals(4, activityForChrisClark.getNumberOfLocalTalks());
        assertEquals(6, activityForChrisClark.getNumberOfNewsFeedEntries());
        assertEquals(8, activityForChrisClark.getNumberOfTweets());
        assertEquals(0, activityForChrisClark.getNumberOfEvents());

        activityForP247 = getActivityComponent().getActivity(p247);
        assertEquals(0, activityForP247.getNumberOfInternationalTalks());
        assertEquals(0, activityForP247.getNumberOfLocalTalks());
        assertEquals(9, activityForP247.getNumberOfNewsFeedEntries());
        assertEquals(12, activityForP247.getNumberOfTweets());
        assertEquals(15, activityForP247.getNumberOfEvents());
    }

    @After
    public void tearDown() {
        JdbcTemplate template = getJdbcTemplate();
        template.execute("delete from activity");
    }

}
