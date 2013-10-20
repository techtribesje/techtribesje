package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TribalBadgeTests extends BadgeTestsSupport {

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoContentSources() {
        TribalBadge badge = new TribalBadge();
        Collection<ContentSource> contentSources = new LinkedList<>();
        assertEquals(0, badge.findEligibleContentSources(null).size());
        assertEquals(0, badge.findEligibleContentSources(contentSources).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsAnEmptyList_WhenThereAreNoTribalRelationships() {
        TribalBadge badge = new TribalBadge();
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(person1);
        contentSources.add(person2);
        contentSources.add(person3);
        contentSources.add(tribe1);
        contentSources.add(tribe2);
        contentSources.add(tribe3);

        assertEquals(0, badge.findEligibleContentSources(contentSources).size());
    }

    @Test
    public void test_findEligibleContentSources_ReturnsSomeContentSources_WhenThereAreTribalRelationships() {
        TribalBadge badge = new TribalBadge();
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(person1);
        contentSources.add(person2);
        contentSources.add(person3);
        contentSources.add(tribe1);
        contentSources.add(tribe2);
        contentSources.add(tribe3);

        tribe2.add(person3);
        tribe2.add(person2);

        assertEquals(3, badge.findEligibleContentSources(contentSources).size());
        assertTrue(badge.findEligibleContentSources(contentSources).contains(person2));
        assertTrue(badge.findEligibleContentSources(contentSources).contains(person3));
        assertTrue(badge.findEligibleContentSources(contentSources).contains(tribe2));
    }

    @Test
    public void test_GetType() {
        Badge badge = new TribalBadge();
        assertTrue(badge.getType() == BadgeType.PersonAndTribe);
    }

}
