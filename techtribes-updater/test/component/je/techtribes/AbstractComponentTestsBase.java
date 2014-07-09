package je.techtribes;

import com.mongodb.Mongo;
import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.activityupdater.ActivityUpdater;
import je.techtribes.component.badge.BadgeComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.event.EventComponent;
import je.techtribes.component.github.GitHubComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.Island;
import je.techtribes.domain.Person;
import je.techtribes.domain.Tribe;
import je.techtribes.util.DateUtils;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.TimeZone;

public abstract class AbstractComponentTestsBase {

    protected static ApplicationContext applicationContext;

    @BeforeClass
    public static void loadApplicationContext() throws Exception {
        // the live JVMs are running in UTC
        TimeZone.setDefault(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        initMongo();
        initMySql();

        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        addContentSources();
    }

    private static void initMongo() {
        Mongo mongo = (Mongo)applicationContext.getBean("mongo");
        mongo.getDB("techtribesje_test").getCollection("tweets").drop();
        mongo.getDB("techtribesje_test").getCollection("newsFeedEntries").drop();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        DataSource dataSource = (DataSource)applicationContext.getBean("dataSource");
        return new JdbcTemplate(dataSource);
    }

    private static void initMySql() {
        JdbcTemplate template = getJdbcTemplate();
        File schemaDirectory = new File("techtribes-core/sql/schema/");
        File[] sqlFiles = schemaDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".sql");
            }
        });
        if (sqlFiles != null) {
            for (File sqlFile : sqlFiles) {
                Resource resource = new FileSystemResource(sqlFile);
                JdbcTestUtils.executeSqlScript(template, resource, false);
            }
        }
    }

    private static void addContentSources() throws Exception {
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

    protected static ContentSourceComponent getContentSourceComponent() {
        return (ContentSourceComponent)applicationContext.getBean("contentSourceComponent");
    }

    protected static EventComponent getEventComponent() {
        return (EventComponent)applicationContext.getBean("eventComponent");
    }

    protected static GitHubComponent getGitHubComponent() {
        return (GitHubComponent)applicationContext.getBean("gitHubComponent");
    }

    protected static TalkComponent getTalkComponent() {
        return (TalkComponent)applicationContext.getBean("talkComponent");
    }

    protected static NewsFeedEntryComponent getNewsFeedEntryComponent() {
        return (NewsFeedEntryComponent)applicationContext.getBean("newsFeedEntryComponent");
    }

    protected static TweetComponent getTweetComponent() {
        return (TweetComponent)applicationContext.getBean("tweetComponent");
    }

    protected static ActivityComponent getActivityComponent() {
        return (ActivityComponent)applicationContext.getBean("activityComponent");
    }

    protected static ActivityUpdater getActivityUpdater() {
        return (ActivityUpdater)applicationContext.getBean("activityUpdater");
    }

    protected static BadgeComponent getBadgeComponent() {
        return (BadgeComponent)applicationContext.getBean("badgeComponent");
    }

}
