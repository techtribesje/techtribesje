package je.techtribes.component.activityupdater;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.*;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ActivityUpdaterTests extends AbstractComponentTestsBase {

    private SimpleDateFormat dateTimeFormat;

    @Before
    public void setUp() {
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));
    }

    @Test
    public void test_calculateActivityForLastSevenDays_StoresCalculationsInTheDatabase() {
        ContentSource simonbrown = getContentSourceComponent().findByShortName("simonbrown");

        // there is an activity record, but there are no talks, tweets, etc
        tearDown();
        getActivityUpdater().calculateActivityForLastSevenDays();
        getActivityComponent().refreshRecentActivity();

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

        template.update("insert into event (title, description, island, content_source_id, url, start_datetime) values (?, ?, ?, ?, ?, ?)",
                "Event",
                "Description",
                "j",
                simonbrown.getId(),
                "http://event.com/event",
                DateUtils.getXDaysAgo(1));

        // now there is an activity record with information in it
        getActivityUpdater().calculateActivityForLastSevenDays();
        getActivityComponent().refreshRecentActivity();

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
