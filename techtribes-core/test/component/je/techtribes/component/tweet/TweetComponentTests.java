package je.techtribes.component.tweet;

import com.mongodb.Mongo;
import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Tweet;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TweetComponentTests extends AbstractComponentTestsBase {

    @Test
    public void test_getRecentTweetsByPage_ReturnsAnEmptyList_WhenThereAreNone() {
        assertEquals(0, getTweetComponent().getRecentTweets(1, 10).size());
        assertEquals(0, getTweetComponent().getRecentTweets(4, 100).size());
    }

    @Test
    public void test_getRecentTweetsByPage_ReturnsANonEmptyList_WhenThereAreSome() {
        List<Tweet> tweets = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            tweets.add(createTweet("simonbrown", i, "Tweet body", DateUtils.getXDaysAgo(100-i)));
        }
        getTweetComponent().storeTweets(tweets);

        tweets = getTweetComponent().getRecentTweets(1, 200);
        assertEquals(100, tweets.size());
        int i = 100;
        for (Tweet tweet : tweets) {
            assertEquals(i, tweet.getId());
            i--;
        }
    }

    @Test
    public void test_getRecentTweetsByPage_ReturnsANonEmptyList_WhenThereAreSomeTweetsAndWeRequestThemPageByPage() {
        List<Tweet> tweets = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            tweets.add(createTweet("simonbrown", i, "Tweet body", DateUtils.getXDaysAgo(100-i)));
        }
        getTweetComponent().storeTweets(tweets);

        int i = 100;
        for (int page = 1; page <= 10; page++) {
            tweets = getTweetComponent().getRecentTweets(page, 10);
            assertEquals(10, tweets.size());
            for (Tweet tweet : tweets) {
                assertEquals(i, tweet.getId());
                i--;
            }
        }

        assertEquals(10, getTweetComponent().getRecentTweets(0, 10).size()); // before the first page (assumes page 1)
        assertEquals(0, getTweetComponent().getRecentTweets(11, 10).size()); // past the last page
        assertEquals(1, getTweetComponent().getRecentTweets(12, 9).size()); // incomplete last page
        assertEquals(1, getTweetComponent().getRecentTweets(3, -10).size()); // silly page size (assumes page size of 1)
    }

    @Test
    public void test_getRecentTweetsForContentSourceByPage_ReturnsAnEmptyList_WhenANullContentSourceIsGiven() {
        assertEquals(0, getTweetComponent().getRecentTweets(null, 10).size());
    }

    @Test
    public void test_getRecentTweetsForContentSourceByPage_ReturnsAnEmptyList_WhenThereAreNone() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(0, getTweetComponent().getRecentTweets(contentSource, 10).size());
    }

    @Test
    public void test_getRecentTweetsForContentSourceByPage_ReturnsANonEmptyList_WhenThereAreSome() {
        List<Tweet> tweets = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            tweets.add(createTweet("simonbrown", i, "Tweet body", DateUtils.getXDaysAgo(100-i)));
        }
        for (int i = 1; i <= 100; i++) {
            tweets.add(createTweet("chrisclark", 1000+i, "Tweet body", DateUtils.getXDaysAgo(100-i)));
        }
        getTweetComponent().storeTweets(tweets);

        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        tweets = getTweetComponent().getRecentTweets(contentSource, 20);
        assertEquals(20, tweets.size());
        int i = 100;
        for (Tweet tweet : tweets) {
            assertEquals(i, tweet.getId());
            i--;
        }

        tweets = getTweetComponent().getRecentTweets(contentSource, 200);
        assertEquals(100, tweets.size());
        i = 100;
        for (Tweet tweet : tweets) {
            assertEquals(i, tweet.getId());
            i--;
        }
    }

    @Test
    public void test_getRecentTweetsForContentSourcesByPage_ReturnsAnEmptyList_WhenANullIsGiven() {
        assertEquals(0, getTweetComponent().getRecentTweets(null, 1, 10).size());
    }

    @Test
    public void test_getRecentTweetsForContentSourcesByPage_ReturnsAnEmptyList_WhenAnEmptyCollectionIsGiven() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        assertEquals(0, getTweetComponent().getRecentTweets(contentSources, 1, 10).size());
    }

    @Test
    public void test_getRecentTweetsForContentSourcesByPage_ReturnsAnEmptyList_WhenThereAreNone() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));
        assertEquals(0, getTweetComponent().getRecentTweets(contentSources, 1, 10).size());
    }

    @Test
    public void test_getRecentTweetsForContentSourcesByPage_ReturnsANonEmptyList_WhenThereAreSome() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("chrisclark"));

        List<Tweet> tweets = new LinkedList<>();
        for (int i = 1; i <= 100; i+=2) {
            tweets.add(createTweet("simonbrown", i, "Tweet body", DateUtils.getXDaysAgo(100-i)));
            tweets.add(createTweet("chrisclark", i+1, "Tweet body", DateUtils.getXDaysAgo(100-(i+1))));
        }
        getTweetComponent().storeTweets(tweets);

        tweets = getTweetComponent().getRecentTweets(contentSources, 1, 10);
        assertEquals(10, tweets.size());
        int i = 100;
        for (Tweet tweet : tweets) {
            assertEquals(i, tweet.getId());
            i-=2;
        }

        tweets = getTweetComponent().getRecentTweets(contentSources, 2, 10);
        assertEquals(10, tweets.size());
        i = 80;
        for (Tweet tweet : tweets) {
            assertEquals(i, tweet.getId());
            i-=2;
        }

        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));
        tweets = getTweetComponent().getRecentTweets(contentSources, 1, 10);
        assertEquals(10, tweets.size());
        i = 100;
        for (Tweet tweet : tweets) {
            assertEquals(i, tweet.getId());
            i--;
        }

        tweets = getTweetComponent().getRecentTweets(contentSources, 2, 10);
        assertEquals(10, tweets.size());
        i = 90;
        for (Tweet tweet : tweets) {
            assertEquals(i, tweet.getId());
            i--;
        }
    }

    @Test
    public void test_getNumberOfTweets_ReturnsZero_WhenThereAreNone() {
        assertEquals(0, getTweetComponent().getNumberOfTweets());
    }

    @Test
    public void test_getNumberOfTweets_ReturnsNonZero_WhenThereAreSome() {
        Collection<Tweet> tweets = new LinkedList<>();
        tweets.add(createTweet("simonbrown", 1234567890, "Tweet body", new Date()));
        tweets.add(createTweet("simonbrown", 1234567891, "Tweet body", new Date()));
        tweets.add(createTweet("chrisclark", 1234567892, "Tweet body", new Date()));

        getTweetComponent().storeTweets(tweets);
        assertEquals(3, getTweetComponent().getNumberOfTweets());
    }

    @Test
    public void test_getNumberOfTweetsForContentSource_ReturnsZero_WhenANullContentSourceIsGiven() {
        assertEquals(0, getTweetComponent().getNumberOfTweets(null, DateUtils.getXDaysAgo(7), DateUtils.getToday()));
    }

    @Test
    public void test_getNumberOfTweetsForContentSource_ReturnsZero_WhenANullDateIsGiven() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(0, getTweetComponent().getNumberOfTweets(contentSource, null, DateUtils.getToday()));
        assertEquals(0, getTweetComponent().getNumberOfTweets(contentSource, DateUtils.getXDaysAgo(7), null));
    }

    @Test
    public void test_getNumberOfTweetsForContentSource_ReturnsZero_WhenThereAreNoTweets() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(0, getTweetComponent().getNumberOfTweets(contentSource, DateUtils.getXDaysAgo(7), DateUtils.getToday()));
    }

    @Test
    public void test_getNumberOfTweetsForContentSource_ReturnsNonZero_WhenThereAreSomeTweets() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(0, getTweetComponent().getNumberOfTweets(contentSource, DateUtils.getXDaysAgo(7), DateUtils.getToday()));

        List<Tweet> tweets = new LinkedList<>();
        for (int i = 1; i <= 7; i++) {
            tweets.add(createTweet("simonbrown", i, "Tweet body", DateUtils.getXDaysAgo(7-i)));
        }
        getTweetComponent().storeTweets(tweets);

        assertEquals(3, getTweetComponent().getNumberOfTweets(contentSource, DateUtils.getXDaysAgo(6), DateUtils.getXDaysAgo(4)));
    }

    @Test
    public void test_getNumberOfTweetsForContentSources_ReturnsZero_WhenNullIsSpecified() {
        assertEquals(0, getTweetComponent().getNumberOfTweets(null));
    }

    @Test
    public void test_getNumberOfTweetsForContentSources_ReturnsZero_WhenAnEmptyListIsSpecified() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        assertEquals(0, getTweetComponent().getNumberOfTweets(contentSources));
    }

    @Test
    public void test_getNumberOfTweetsForContentSources_ReturnsZero_WhenThereAreNone() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));
        assertEquals(0, getTweetComponent().getNumberOfTweets(contentSources));
    }

    @Test
    public void test_getNumberOfTweetsForContentSources_ReturnsNonZero_WhenThereAreSome() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));

        Collection<Tweet> tweets = new LinkedList<>();
        tweets.add(createTweet("simonbrown", 1234567890, "Tweet body", new Date()));
        tweets.add(createTweet("simonbrown", 1234567891, "Tweet body", new Date()));
        tweets.add(createTweet("chrisclark", 1234567892, "Tweet body", new Date()));

        getTweetComponent().storeTweets(tweets);
        assertEquals(2, getTweetComponent().getNumberOfTweets(contentSources));

        contentSources.add(getContentSourceComponent().findByShortName("chrisclark"));
        assertEquals(3, getTweetComponent().getNumberOfTweets(contentSources));
    }

    @Test
    public void test_removeTweet_DoesntDoAnything_WhenATweetWithTheSpecifiedIdDoesntExist() {
        Collection<Tweet> tweets = new LinkedList<>();
        tweets.add(createTweet("simonbrown", 1234567890, "Tweet body", new Date()));
        tweets.add(createTweet("simonbrown", 1234567891, "Tweet body", new Date()));
        tweets.add(createTweet("chrisclark", 1234567892, "Tweet body", new Date()));

        getTweetComponent().storeTweets(tweets);
        getTweetComponent().removeTweet(987654321);
        assertEquals(3, getTweetComponent().getNumberOfTweets());
    }

    @Test
    public void test_removeTweet_RemovesTweetWithTheSpecifiedId_WhenAValidIdIsGiven() {
        List<Tweet> tweets = new LinkedList<>();
        tweets.add(createTweet("simonbrown", 1234567890, "Tweet body", DateUtils.getXDaysAgo(3)));
        tweets.add(createTweet("simonbrown", 1234567891, "Tweet body", DateUtils.getXDaysAgo(2)));
        tweets.add(createTweet("chrisclark", 1234567892, "Tweet body", DateUtils.getXDaysAgo(1)));

        getTweetComponent().storeTweets(tweets);
        assertEquals(3, getTweetComponent().getRecentTweets(1, 10).size());
        getTweetComponent().removeTweet(1234567891);
        tweets = getTweetComponent().getRecentTweets(1, 10);
        assertEquals(2, tweets.size());
        assertEquals(1234567892, tweets.get(0).getId());
        assertEquals(1234567890, tweets.get(1).getId());
    }

    @Test
    public void test_storeTweets_DoesntBreak_WhenNullIsSpecified() {
        getTweetComponent().storeTweets(null);
    }

    @Test
    public void test_storeTweets_DoesntBreak_WhenAnEmptyCollectionIsSpecified() {
        getTweetComponent().storeTweets(new LinkedList<Tweet>());
    }

    @Test
    public void test_tweetDetailsAreStoredAndRetrievedCorrectly() {
        Date date = DateUtils.getXDaysAgo(3);
        List<Tweet> tweets = new LinkedList<>();
        tweets.add(createTweet("simonbrown", 1234567890, "Tweet body", date));
        getTweetComponent().storeTweets(tweets);

        Tweet tweet = getTweetComponent().getRecentTweets(1, 1).get(0);
        assertEquals("Simon Brown", tweet.getContentSource().getName());
        assertEquals(1234567890, tweet.getId());
        assertEquals("Tweet body", tweet.getBody());
        assertEquals(date, tweet.getTimestamp());
    }

    private Tweet createTweet(String contentSourceShortName, long id, String body, Date date) {
        return new Tweet(getContentSourceComponent().findByShortName(contentSourceShortName), id, body, date);
    }

    @After
    public void tearDown() {
        Mongo mongo = (Mongo)applicationContext.getBean("mongo");
        mongo.getDB("techtribesje_test").getCollection("tweets").drop();
    }

}
