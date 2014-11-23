package je.techtribes.component.activityupdater;

import je.techtribes.AbstractUpdaterComponentTestsBase;
import je.techtribes.domain.*;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ActivityUpdaterTests extends AbstractUpdaterComponentTestsBase {

    private SimpleDateFormat dateTimeFormat;

    @Before
    public void setUp() throws Exception {
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        addContentSources();
    }

    private void addContentSources() throws Exception {
        Person simonbrown = new Person();
        simonbrown.setName("Simon Brown");
        simonbrown.setIsland(Island.Jersey);
        simonbrown.setProfile("Here is some profile text about Simon Brown");
        simonbrown.setUrl(new URL("http://www.simonbrown.je"));
        simonbrown.setTwitterId("simonbrown");
        getContentSourceComponent().add(simonbrown);

        Person chrisclark = new Person();
        chrisclark.setName("Chris Clark");
        chrisclark.setIsland(Island.Jersey);
        chrisclark.setProfile("Here is some profile text about Chris Clark");
        chrisclark.setUrl(new URL("http://www.chrisclark.je"));
        chrisclark.setTwitterId("nomanualreqd");
        getContentSourceComponent().add(chrisclark);

        Tribe p247 = new Tribe(ContentSourceType.Business);
        p247.setName("Prosperity 24.7");
        p247.setIsland(Island.Jersey);
        p247.setProfile("Here is some profile text about P247");
        p247.setTwitterId("p247");
        p247.setUrl(new URL("http://www.prosperity247.com"));
        p247.setGitHubId(null);
        getContentSourceComponent().add(p247);

        Tribe techtribesje = new Tribe(ContentSourceType.Community);
        techtribesje.setName("techtribes.je");
        techtribesje.setIsland(Island.Jersey);
        techtribesje.setProfile("Here is some profile text about Tech Tribes");
        techtribesje.setTwitterId("techtribesje");
        techtribesje.setUrl(new URL("http://www.techtribes.je"));
        techtribesje.setGitHubId(null);
        getContentSourceComponent().add(techtribesje);

        Tribe coding = new Tribe(ContentSourceType.Tech);
        coding.setName("Coding");
        coding.setIsland(Island.None);
        coding.setProfile("Here is some profile text about the coding tribe");
        coding.setSearchTerms("coding programming java .net ruby php python perl");
        getContentSourceComponent().add(coding);

        Tribe dqmag = new Tribe(ContentSourceType.Media);
        dqmag.setName("DQ Magazine");
        dqmag.setIsland(Island.Jersey);
        dqmag.setProfile("Here is some profile text about DQ Magazine");
        dqmag.setTwitterId("dqmag");
        dqmag.setUrl(new URL("http://www.dqmagazine.com"));
        getContentSourceComponent().add(dqmag);
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
        Tweet tweet = new Tweet("simonbrown", 1234567890, "Body", DateUtils.getXDaysAgo(1), null);
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
