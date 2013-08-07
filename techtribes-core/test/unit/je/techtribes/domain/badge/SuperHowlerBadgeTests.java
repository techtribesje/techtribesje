package je.techtribes.domain.badge;

import je.techtribes.domain.Talk;
import je.techtribes.util.DateUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SuperHowlerBadgeTests extends TalkBadgeTestsSupport {

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoTalks() {
        SuperHowlerBadge badge = new SuperHowlerBadge();
        Collection<Talk> talks = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(talks).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreSomeTalksButNobodyHasDoneTen() {
        SuperHowlerBadge badge = new SuperHowlerBadge();
        Collection<Talk> talks = new LinkedList<>();
        talks.add(createTalk(1, person1, DateUtils.getToday(), "Jersey"));
        talks.add(createTalk(2, person2, DateUtils.getXDaysAgo(7), "Jersey"));
        talks.add(createTalk(3, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(4, person3, DateUtils.getXDaysAgo(-7), "Jersey"));
        talks.add(createTalk(5, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(6, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(7, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(8, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(9, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(10, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(11, person2, DateUtils.getXDaysAgo(1), "Jersey"));

        assertEquals(0, badge.findEligibleContentSources(talks).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsANonEmptyList_WhenThereAreSomeTalksAndSomebodyHasDoneTen() {
        SuperHowlerBadge badge = new SuperHowlerBadge();
        Collection<Talk> talks = new LinkedList<>();
        talks.add(createTalk(1, person1, DateUtils.getToday(), "Jersey"));
        talks.add(createTalk(2, person2, DateUtils.getXDaysAgo(7), "Jersey"));
        talks.add(createTalk(3, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(4, person3, DateUtils.getXDaysAgo(-7), "Jersey"));
        talks.add(createTalk(5, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(6, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(7, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(8, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(9, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(10, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(11, person2, DateUtils.getXDaysAgo(1), "Jersey"));
        talks.add(createTalk(12, person2, DateUtils.getXDaysAgo(1), "Jersey"));

        assertEquals(1, badge.findEligibleContentSources(talks).size());
        assertTrue(badge.findEligibleContentSources(talks).contains(person2));
    }

}
