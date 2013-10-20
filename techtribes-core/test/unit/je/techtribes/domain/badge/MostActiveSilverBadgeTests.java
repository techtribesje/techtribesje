package je.techtribes.domain.badge;

import je.techtribes.domain.Activity;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MostActiveSilverBadgeTests extends ActivityBadgeTestsSupport {

    @Override
    protected ActivityBadge createBadge() {
        return new MostActiveSilverBadge();
    }

    @Test
    public void test_findEligibleContentSources_ReturnsEmptyList_WhenThereAreNone() {
        ActivityBadge badge = createBadge();
        List<Activity> activityList = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(activityList).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAListOfOneContentSource_WhenPassedAnActivityList() {
        ActivityBadge badge = createBadge();
        assertEquals(1, badge.findEligibleContentSources(activityList).size());
        assertTrue(badge.findEligibleContentSources(activityList).contains(person2));
    }

    @Test
    public void test_GetType() {
        Badge badge = new MostActiveSilverBadge();
        assertTrue(badge.getType() == BadgeType.PersonAndTribe);
    }

}
