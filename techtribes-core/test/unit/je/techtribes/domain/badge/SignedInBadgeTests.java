package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SignedInBadgeTests extends BadgeTestsSupport {

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoContentSources() {
        SignedInBadge badge = new SignedInBadge();
        Collection<ContentSource> contentSources = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(contentSources).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoContentSourcesThatHaveSignedIn() {
        SignedInBadge badge = new SignedInBadge();
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(person1);
        contentSources.add(person2);
        contentSources.add(person3);

        assertEquals(0, badge.findEligibleContentSources(contentSources).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsSomeContentSources_WhenThereAreContentSourcesThatHaveSignedIn() {
        SignedInBadge badge = new SignedInBadge();
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(person1);
        contentSources.add(person2);
        person2.setSignedInBefore(true);
        contentSources.add(person3);

        assertEquals(1, badge.findEligibleContentSources(contentSources).size());
        assertTrue(badge.findEligibleContentSources(contentSources).contains(person2));
    }

    @Test
    public void test_GetType() {
        Badge badge = new SignedInBadge();
        assertTrue(badge.getType() == BadgeType.PersonAndTribe);
    }

}
