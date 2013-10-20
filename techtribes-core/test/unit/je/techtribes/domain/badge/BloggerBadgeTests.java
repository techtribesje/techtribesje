package je.techtribes.domain.badge;

import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Person;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BloggerBadgeTests {

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoNewsFeedEntries() {
        BloggerBadge badge = new BloggerBadge();
        Collection<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(newsFeedEntries).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAllContentSources_WhenThereAreNewsFeedEntries() {
        BloggerBadge badge = new BloggerBadge();
        Collection<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        Person person1 = new Person(1);
        Person person2 = new Person(2);
        newsFeedEntries.add(new NewsFeedEntry("permalink", "title", "body", new Date(), person1));
        newsFeedEntries.add(new NewsFeedEntry("permalink", "title", "body", new Date(), person2));
        newsFeedEntries.add(new NewsFeedEntry("permalink", "title", "body", new Date(), person2));

        assertEquals(2, badge.findEligibleContentSources(newsFeedEntries).size());
        assertTrue(badge.findEligibleContentSources(newsFeedEntries).contains(person1));
        assertTrue(badge.findEligibleContentSources(newsFeedEntries).contains(person2));
    }

    @Test
    public void test_GetType() {
        Badge badge = new BloggerBadge();
        assertTrue(badge.getType() == BadgeType.PersonAndTribe);
    }

}
