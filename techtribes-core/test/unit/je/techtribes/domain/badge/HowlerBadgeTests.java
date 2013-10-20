package je.techtribes.domain.badge;

import je.techtribes.domain.Talk;
import je.techtribes.util.DateUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HowlerBadgeTests extends TalkBadgeTestsSupport {

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoTalks() {
        HowlerBadge badge = new HowlerBadge();
        Collection<Talk> talks = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(talks).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsANonEmptyList_WhenThereAreSomeTalks() {
        HowlerBadge badge = new HowlerBadge();
        Collection<Talk> talks = new LinkedList<>();
        talks.add(createTalk(1, person1, DateUtils.getToday(), "Jersey"));
        talks.add(createTalk(2, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(3, person3, DateUtils.getXDaysAgo(-1), "Jersey"));

        assertEquals(2, badge.findEligibleContentSources(talks).size());
        assertTrue(badge.findEligibleContentSources(talks).contains(person1));
        assertTrue(badge.findEligibleContentSources(talks).contains(person2));
    }

    @Test
    public void test_GetType() {
        Badge badge = new HowlerBadge();
        assertTrue(badge.getType() == BadgeType.Person);
    }

}
