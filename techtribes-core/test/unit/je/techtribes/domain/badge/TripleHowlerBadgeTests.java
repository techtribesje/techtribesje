package je.techtribes.domain.badge;

import je.techtribes.domain.Talk;
import je.techtribes.util.DateUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TripleHowlerBadgeTests extends TalkBadgeTestsSupport {

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoTalks() {
        TripleHowlerBadge badge = new TripleHowlerBadge();
        Collection<Talk> talks = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(talks).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreSomeTalksButNobodyHasDoneThree() {
        TripleHowlerBadge badge = new TripleHowlerBadge();
        Collection<Talk> talks = new LinkedList<>();
        talks.add(createTalk(1, person1, DateUtils.getToday(), "Jersey"));
        talks.add(createTalk(2, person2, DateUtils.getXDaysAgo(7), "Jersey"));
        talks.add(createTalk(3, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(4, person3, DateUtils.getXDaysAgo(-7), "Jersey"));

        assertEquals(0, badge.findEligibleContentSources(talks).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsANonEmptyList_WhenThereAreSomeTalksAndSomebodyHasDoneThree() {
        TripleHowlerBadge badge = new TripleHowlerBadge();
        Collection<Talk> talks = new LinkedList<>();
        talks.add(createTalk(1, person1, DateUtils.getToday(), "Jersey"));
        talks.add(createTalk(2, person2, DateUtils.getXDaysAgo(7), "Jersey"));
        talks.add(createTalk(3, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(4, person3, DateUtils.getXDaysAgo(-7), "Jersey"));
        talks.add(createTalk(5, person2, DateUtils.getXDaysAgo(14), "Jersey"));

        assertEquals(1, badge.findEligibleContentSources(talks).size());
        assertTrue(badge.findEligibleContentSources(talks).contains(person2));
    }

    @Test
    public void test_GetType() {
        Badge badge = new TripleHowlerBadge();
        assertTrue(badge.getType() == BadgeType.Person);
    }

}
