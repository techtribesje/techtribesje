package je.techtribes.domain.badge;

import je.techtribes.domain.Island;
import je.techtribes.domain.Talk;
import je.techtribes.util.DateUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JetsetterBadgeTests extends TalkBadgeTestsSupport {

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoTalks() {
        JetsetterBadge badge = new JetsetterBadge();
        Collection<Talk> talks = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(talks).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreSomeTalksButTheSpeakersHaventTravelled() {
        JetsetterBadge badge = new JetsetterBadge();
        Collection<Talk> talks = new LinkedList<>();

        person1.setIsland(Island.Jersey);
        person2.setIsland(Island.Guernsey);
        talks.add(createTalk(1, person1, DateUtils.getToday(), "Jersey"));
        talks.add(createTalk(2, person2, DateUtils.getToday(), "Guernsey"));

        assertEquals(0, badge.findEligibleContentSources(talks).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreSomeTalksAndButTheSpeakersHaventTravelledOutsideOfTheChannelIslands() {
        JetsetterBadge badge = new JetsetterBadge();
        Collection<Talk> talks = new LinkedList<>();

        person1.setIsland(Island.Jersey);
        person2.setIsland(Island.Guernsey);
        talks.add(createTalk(1, person1, DateUtils.getToday(), "Guernsey"));
        talks.add(createTalk(2, person2, DateUtils.getToday(), "Jersey"));

        assertEquals(0, badge.findEligibleContentSources(talks).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsANonEmptyList_WhenThereAreSomeTalksAndASpeakerHasSpokenOutsideOfTheChannelIslands() {
        JetsetterBadge badge = new JetsetterBadge();
        Collection<Talk> talks = new LinkedList<>();

        person1.setIsland(Island.Jersey);
        person2.setIsland(Island.Guernsey);
        talks.add(createTalk(1, person1, DateUtils.getToday(), "Norway"));
        talks.add(createTalk(2, person2, DateUtils.getToday(), "Jersey"));

        assertEquals(1, badge.findEligibleContentSources(talks).size());
        assertTrue(badge.findEligibleContentSources(talks).contains(person1));
    }

}
