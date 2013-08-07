package je.techtribes.component.activity;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.component.tweet.Tweet;
import je.techtribes.domain.*;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
                    id * 1,
                    id * 2,
                    id * 3,
                    id * 4,
                    id * 5,
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
        Activity activity = getActivityComponent().getActivity(contentSource);
        assertEquals("Chris Clark", activity.getContentSource().getName());
        assertEquals(id*1, activity.getNumberOfInternationalTalks());
        assertEquals(id*2, activity.getNumberOfLocalTalks());
        assertEquals(id*3, activity.getNumberOfNewsFeedEntries());
        assertEquals(id*4, activity.getNumberOfTweets());
        assertEquals(id*5, activity.getNumberOfEvents());
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
        assertEquals(10, activityForChrisClark.getNumberOfEvents());

        Activity activityForP247 = getActivityComponent().getActivity(p247);
        assertEquals(3, activityForP247.getNumberOfInternationalTalks());
        assertEquals(6, activityForP247.getNumberOfLocalTalks());
        assertEquals(9, activityForP247.getNumberOfNewsFeedEntries());
        assertEquals(12, activityForP247.getNumberOfTweets());
        assertEquals(15, activityForP247.getNumberOfEvents());

        List<Integer> personIds = new LinkedList<>();
        personIds.add(chrisclark.getId());
        getContentSourceComponent().updateTribeMembers(p247, personIds);

        getActivityComponent().refreshRecentActivity();

        activityForChrisClark = getActivityComponent().getActivity(chrisclark);
        assertEquals(2, activityForChrisClark.getNumberOfInternationalTalks());
        assertEquals(4, activityForChrisClark.getNumberOfLocalTalks());
        assertEquals(6, activityForChrisClark.getNumberOfNewsFeedEntries());
        assertEquals(8, activityForChrisClark.getNumberOfTweets());
        assertEquals(10, activityForChrisClark.getNumberOfEvents());

        activityForP247 = getActivityComponent().getActivity(p247);
        assertEquals(3+2, activityForP247.getNumberOfInternationalTalks());
        assertEquals(6+4, activityForP247.getNumberOfLocalTalks());
        assertEquals(9+6, activityForP247.getNumberOfNewsFeedEntries());
        assertEquals(12+8, activityForP247.getNumberOfTweets());
        assertEquals(15+10, activityForP247.getNumberOfEvents());
    }

    @Test
    public void test_calculateActivityForLastSevenDays_StoresCalculationsInTheDatabase() {
        ContentSource simonbrown = getContentSourceComponent().findByShortName("simonbrown");

        // there is an activity record, but there are no talks, tweets, etc
        tearDown();
        getActivityComponent().calculateActivityForLastSevenDays();
        Activity activity = getActivityComponent().getActivity(simonbrown);
        assertEquals(0, activity.getNumberOfInternationalTalks());
        assertEquals(0, activity.getNumberOfLocalTalks());
        assertEquals(0, activity.getNumberOfNewsFeedEntries());
        assertEquals(0, activity.getNumberOfTweets());
        assertEquals(0, activity.getNumberOfEvents());

        tearDown(); // remove the empty activity records from the database to avoid primary key conflicts

        JdbcTemplate template = getJdbcTemplate();
        template.update("insert into talk (name, description, type, event_name, city, country, content_source_id, url, talk_date, slides_url, video_url) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                "International talk",
                "Description",
                "p",
                "Event name",
                "Oslo",
                "Norway",
                simonbrown.getId(),
                "http://event.com/talk",
                DateUtils.getXDaysAgo(1),
                null,
                null);
        template.update("insert into talk (name, description, type, event_name, city, country, content_source_id, url, talk_date, slides_url, video_url) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                "Local talk",
                "Description",
                "p",
                "Event name",
                "St Helier",
                "Jersey",
                simonbrown.getId(),
                "http://event.com/talk",
                DateUtils.getXDaysAgo(1),
                null,
                null);

        Collection<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        NewsFeedEntry newsFeedEntry = new NewsFeedEntry("http://somedomain.com/link", "Title", "Body", DateUtils.getXDaysAgo(1), simonbrown);
        newsFeedEntries.add(newsFeedEntry);
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        Collection<Tweet> tweets = new LinkedList<>();
        Tweet tweet = new Tweet("simonbrown", 1234567890, "Body", DateUtils.getXDaysAgo(1));
        tweet.setContentSource(simonbrown);
        tweets.add(tweet);
        getTweetComponent().storeTweets(tweets);

        template.update("insert into event (title, description, island, content_source_id, url, event_datetime) values (?, ?, ?, ?, ?, ?)",
                "Event",
                "Description",
                "j",
                simonbrown.getId(),
                "http://event.com/event",
                DateUtils.getXDaysAgo(1));

        // now there is an activity record with information in it
        getActivityComponent().calculateActivityForLastSevenDays();
        activity = getActivityComponent().getActivity(simonbrown);
        assertEquals(1, activity.getNumberOfInternationalTalks());
        assertEquals(1, activity.getNumberOfLocalTalks());
        assertEquals(1, activity.getNumberOfNewsFeedEntries());
        assertEquals(1, activity.getNumberOfTweets());
        assertEquals(1, activity.getNumberOfEvents());
    }

    @After
    public void tearDown() {
        JdbcTemplate template = getJdbcTemplate();
        template.execute("delete from activity");
    }

}
