package je.techtribes.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActivityTests {

    private Activity activity;

    @Test
    public void testBasicScoresForPerson() {
        ContentSource person = new Person();
        activity = new Activity(person, 1, 1, 3, 10, 0);

        assertEquals(100, activity.getInternationalTalkScore());
        assertEquals(50, activity.getLocalTalkScore());
        assertEquals(75, activity.getNewsFeedEntryScore());
        assertEquals(10, activity.getTwitterScore());
        assertEquals(0, activity.getEventScore());

        assertEquals(235, activity.getScore(), 0.0);
        assertEquals(235, activity.getScore(), 0.0);
    }

    @Test
    public void testTwitterScoreForPersonIsCappedAt25() {
        ContentSource person = new Person();
        activity = new Activity(person, 1, 1, 3, 49, 0);

        assertEquals(25, activity.getTwitterScore());

        assertEquals(250, activity.getScore(), 0.0);
        assertEquals(250, activity.getScore(), 0.0);
    }

    @Test
    public void testBasicScoresForBusinessTribeWithZeroMembers() {
        ContentSource tribe = new Tribe(ContentSourceType.Business);
        activity = new Activity(tribe, 1, 1, 3, 10, 0);

        assertEquals(0, activity.getInternationalTalkScore());
        assertEquals(0, activity.getLocalTalkScore());
        assertEquals(75, activity.getNewsFeedEntryScore());
        assertEquals(10, activity.getTwitterScore());
        assertEquals(0, activity.getEventScore());

        assertEquals(85, activity.getScore(), 0.0);
    }

    @Test
    public void testTwitterScoreForTribeIsCappedAt25() {
        ContentSource tribe = new Tribe(ContentSourceType.Community);
        activity = new Activity(tribe, 0, 0, 3, 49, 1);

        assertEquals(25, activity.getTwitterScore());

        assertEquals(200, activity.getScore(), 0.0);
    }

    @Test
    public void testGetEventScore_ReturnsTheScore_WhenTheTribeIsACommunityTribe() {
        ContentSource tribe = new Tribe(ContentSourceType.Community);
        activity = new Activity(tribe, 1, 1, 1, 1, 1);

        assertEquals(100, activity.getEventScore());
    }

    @Test
    public void testGetEventScore_ReturnsZero_WhenTheTribeIsNotACommunityTribe() {
        ContentSource tribe = new Tribe(ContentSourceType.Business);
        activity = new Activity(tribe, 1, 1, 1, 1, 1);

        assertEquals(0, activity.getEventScore());
    }

    @Test
    public void testGetInternationalTalkScore_ReturnsTheScore_WhenTheContentSourceIsAPerson() {
        ContentSource person = new Person();
        activity = new Activity(person, 1, 0, 0, 0, 0);

        assertEquals(100, activity.getInternationalTalkScore());
    }

    @Test
    public void testGetInternationalTalkScore_ReturnsZero_WhenTheContentSourceIsATribe() {
        ContentSource tribe = new Tribe(ContentSourceType.Business);
        activity = new Activity(tribe, 1, 0, 0, 0, 0);

        assertEquals(0, activity.getInternationalTalkScore());
    }

    @Test
    public void testGetLocalTalkScore_ReturnsTheScore_WhenTheContentSourceIsAPerson() {
        ContentSource person = new Person();
        activity = new Activity(person, 0, 1, 0, 0, 0);

        assertEquals(50, activity.getLocalTalkScore());
    }

    @Test
    public void testGetLocalTalkScore_ReturnsZero_WhenTheContentSourceIsATribe() {
        ContentSource tribe = new Tribe(ContentSourceType.Business);
        activity = new Activity(tribe, 0, 1, 0, 0, 0);

        assertEquals(0, activity.getLocalTalkScore());
    }

}
