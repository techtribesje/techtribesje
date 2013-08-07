package je.techtribes.domain.badge;

import je.techtribes.domain.Tweet;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommunityHashtaggerBadgeTests extends BadgeTestsSupport {

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoTweets() {
        CommunityHashtaggerBadge badge = new CommunityHashtaggerBadge();
        Collection<Tweet> tweets = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(tweets).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAllContentSources_WhenThereAreTweetsButNoCommunityHashtags() {
        CommunityHashtaggerBadge badge = new CommunityHashtaggerBadge();
        Collection<Tweet> tweets = new LinkedList<>();
        tweets.add(new Tweet(person1, 1234567890, "tweet", new Date()));
        tweets.add(new Tweet(person2, 1234567891, "tweet", new Date()));
        tweets.add(new Tweet(person2, 1234567892, "tweet", new Date()));

        assertEquals(0, badge.findEligibleContentSources(tweets).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAllContentSources_WhenThereAreTweetsAndBCSJerseyIsMentioned() {
        CommunityHashtaggerBadge badge = new CommunityHashtaggerBadge();
        Collection<Tweet> tweets = new LinkedList<>();
        tweets.add(new Tweet(person1, 1234567890, "tweet", new Date()));
        tweets.add(new Tweet(person2, 1234567891, "tweet #bcsjersey", new Date()));
        tweets.add(new Tweet(person2, 1234567892, "tweet", new Date()));

        assertEquals(1, badge.findEligibleContentSources(tweets).size());
        assertTrue(badge.findEligibleContentSources(tweets).contains(person2));
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAllContentSources_WhenThereAreTweetsAndDigitalJerseyIsMentioned() {
        CommunityHashtaggerBadge badge = new CommunityHashtaggerBadge();
        Collection<Tweet> tweets = new LinkedList<>();
        tweets.add(new Tweet(person1, 1234567890, "tweet", new Date()));
        tweets.add(new Tweet(person2, 1234567891, "tweet #DigitalJersey", new Date()));
        tweets.add(new Tweet(person2, 1234567892, "tweet", new Date()));

        assertEquals(1, badge.findEligibleContentSources(tweets).size());
        assertTrue(badge.findEligibleContentSources(tweets).contains(person2));
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAllContentSources_WhenThereAreTweetsAndTechTribesIsMentioned() {
        CommunityHashtaggerBadge badge = new CommunityHashtaggerBadge();
        Collection<Tweet> tweets = new LinkedList<>();
        tweets.add(new Tweet(person1, 1234567890, "tweet", new Date()));
        tweets.add(new Tweet(person2, 1234567891, "tweet #techtribesje", new Date()));
        tweets.add(new Tweet(person2, 1234567892, "tweet", new Date()));

        assertEquals(1, badge.findEligibleContentSources(tweets).size());
        assertTrue(badge.findEligibleContentSources(tweets).contains(person2));
    }

}
