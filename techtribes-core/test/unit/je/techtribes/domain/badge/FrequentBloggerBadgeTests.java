package je.techtribes.domain.badge;

import je.techtribes.domain.Activity;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FrequentBloggerBadgeTests extends ActivityBadgeTestsSupport {

    @Override
    protected ActivityBadge createBadge() {
        return new FrequentBloggerBadge();
    }

    @Test
    public void test_findEligibleContentSources_ReturnsEmptyList_WhenThereAreNoNewsFeedEntries() {
        ActivityBadge badge = createBadge();
        List<Activity> activityList = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(activityList).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsEmptyList_WhenNobodyHasBloggedMoreThanOnce() {
        ActivityBadge badge = createBadge();
        assertEquals(0, badge.findEligibleContentSources(activityList).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsEmptyList_WhenSomebodyHasBloggedMoreThanOnce() {
        ActivityBadge badge = createBadge();
        activityList = new LinkedList<>();
        activityList.add(new Activity(person1, 1, 1, 1, 1, 1));
        activityList.add(new Activity(person2, 1, 1, 2, 1, 1)); // <- 2 blog entries
        activityList.add(new Activity(person3, 1, 1, 1, 1, 1));
        assertEquals(1, badge.findEligibleContentSources(activityList).size());
        assertTrue(badge.findEligibleContentSources(activityList).contains(person2));
    }

    @Test
    public void test_GetType() {
        Badge badge = new FrequentBloggerBadge();
        assertTrue(badge.getType() == BadgeType.PersonAndTribe);
    }

}
