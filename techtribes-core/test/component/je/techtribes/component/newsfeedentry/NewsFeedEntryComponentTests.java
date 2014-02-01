package je.techtribes.component.newsfeedentry;

import com.mongodb.Mongo;
import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NewsFeedEntryComponentTests extends AbstractComponentTestsBase {

    @Test
    public void test_getRecentNewsFeedEntriesByPage_ReturnsAnEmptyList_WhenThereAreNone() {
        assertEquals(0, getNewsFeedEntryComponent().getRecentNewsFeedEntries(1, 10).size());
        assertEquals(0, getNewsFeedEntryComponent().getRecentNewsFeedEntries(4, 100).size());
    }

    @Test
    public void test_getRecentNewsFeedEntriesByPage_ReturnsANonEmptyList_WhenThereAreSome() {
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            newsFeedEntries.add(createNewsFeedEntry("simonbrown", i, "News feed entry title", "News feed entry body", DateUtils.getXDaysAgo(100-i)));
        }
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        newsFeedEntries = getNewsFeedEntryComponent().getRecentNewsFeedEntries(1, 200);
        assertEquals(100, newsFeedEntries.size());
        int i = 100;
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            assertEquals("http://www.somedomain.com/" + i, newsFeedEntry.getPermalink());
            i--;
        }
    }

    @Test
    public void test_getRecentNewsFeedEntriesByPage_ReturnsANonEmptyList_WhenThereAreSomeNewsFeedEntriesAndWeRequestThemPageByPage() {
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            newsFeedEntries.add(createNewsFeedEntry("simonbrown", i, "News feed entry title", "News feed entry body", DateUtils.getXDaysAgo(100-i)));
        }
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        int i = 100;
        for (int page = 1; page <= 10; page++) {
            newsFeedEntries = getNewsFeedEntryComponent().getRecentNewsFeedEntries(page, 10);
            assertEquals(10, newsFeedEntries.size());
            for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
                assertEquals("http://www.somedomain.com/" + i, newsFeedEntry.getPermalink());
                i--;
            }
        }

        assertEquals(10, getNewsFeedEntryComponent().getRecentNewsFeedEntries(0, 10).size()); // before the first page (assumes page 1)
        assertEquals(0, getNewsFeedEntryComponent().getRecentNewsFeedEntries(11, 10).size()); // past the last page
        assertEquals(1, getNewsFeedEntryComponent().getRecentNewsFeedEntries(12, 9).size()); // incomplete last page
        assertEquals(1, getNewsFeedEntryComponent().getRecentNewsFeedEntries(3, -10).size()); // silly page size (assumes page size of 1)
    }

    @Test
    public void test_getRecentNewsFeedEntriesForContentSourceByPage_ReturnsAnEmptyList_WhenANullContentSourceIsGiven() {
        assertEquals(0, getNewsFeedEntryComponent().getRecentNewsFeedEntries(null, 10).size());
    }

    @Test
    public void test_getRecentNewsFeedEntriesForContentSourceByPage_ReturnsAnEmptyList_WhenThereAreNone() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(0, getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSource, 10).size());
    }

    @Test
    public void test_getRecentNewsFeedEntriesForContentSourceByPage_ReturnsANonEmptyList_WhenThereAreSome() {
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            newsFeedEntries.add(createNewsFeedEntry("simonbrown", i, "News feed entry title", "News feed entry body", DateUtils.getXDaysAgo(100-i)));
        }
        for (int i = 1; i <= 100; i++) {
            newsFeedEntries.add(createNewsFeedEntry("chrisclark", 1000+i, "News feed entry title", "News feed entry body", DateUtils.getXDaysAgo(100-i)));
        }
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        newsFeedEntries = getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSource, 20);
        assertEquals(20, newsFeedEntries.size());
        int i = 100;
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            assertEquals("http://www.somedomain.com/" + i, newsFeedEntry.getPermalink());
            i--;
        }

        newsFeedEntries = getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSource, 200);
        assertEquals(100, newsFeedEntries.size());
        i = 100;
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            assertEquals("http://www.somedomain.com/" + i, newsFeedEntry.getPermalink());
            i--;
        }
    }

    @Test
    public void test_getRecentNewsFeedEntriesForContentSourcesByPage_ReturnsAnEmptyList_WhenANullIsGiven() {
        assertEquals(0, getNewsFeedEntryComponent().getRecentNewsFeedEntries(null, 1, 10).size());
    }

    @Test
    public void test_getRecentNewsFeedEntriesForContentSourcesByPage_ReturnsAnEmptyList_WhenAnEmptyCollectionIsGiven() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        assertEquals(0, getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSources, 1, 10).size());
    }

    @Test
    public void test_getRecentNewsFeedEntriesForContentSourcesByPage_ReturnsAnEmptyList_WhenThereAreNone() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));
        assertEquals(0, getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSources, 1, 10).size());
    }

    @Test
    public void test_getRecentNewsFeedEntriesForContentSourcesByPage_ReturnsANonEmptyList_WhenThereAreSome() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("chrisclark"));

        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        for (int i = 1; i <= 100; i+=2) {
            newsFeedEntries.add(createNewsFeedEntry("simonbrown", i, "News feed entry title", "News feed entry body", DateUtils.getXDaysAgo(100-i)));
            newsFeedEntries.add(createNewsFeedEntry("chrisclark", i+1, "News feed entry title", "News feed entry body", DateUtils.getXDaysAgo(100-(i+1))));
        }
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        newsFeedEntries = getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSources, 1, 10);
        assertEquals(10, newsFeedEntries.size());
        int i = 100;
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            assertEquals("http://www.somedomain.com/" + i, newsFeedEntry.getPermalink());
            i-=2;
        }

        newsFeedEntries = getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSources, 2, 10);
        assertEquals(10, newsFeedEntries.size());
        i = 80;
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            assertEquals("http://www.somedomain.com/" + i, newsFeedEntry.getPermalink());
            i-=2;
        }

        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));
        newsFeedEntries = getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSources, 1, 10);
        assertEquals(10, newsFeedEntries.size());
        i = 100;
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            assertEquals("http://www.somedomain.com/" + i, newsFeedEntry.getPermalink());
            i--;
        }

        newsFeedEntries = getNewsFeedEntryComponent().getRecentNewsFeedEntries(contentSources, 2, 10);
        assertEquals(10, newsFeedEntries.size());
        i = 90;
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            assertEquals("http://www.somedomain.com/" + i, newsFeedEntry.getPermalink());
            i--;
        }
    }

    @Test
    public void test_getNumberOfNewsFeedEntries_ReturnsZero_WhenThereAreNone() {
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries());
    }

    @Test
    public void test_getNumberOfNewsFeedEntries_ReturnsNonZero_WhenThereAreSome() {
        Collection<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        newsFeedEntries.add(createNewsFeedEntry("simonbrown", 1234567890, "News feed entry title", "News feed entry body", new Date()));
        newsFeedEntries.add(createNewsFeedEntry("simonbrown", 1234567891, "News feed entry title", "News feed entry body", new Date()));
        newsFeedEntries.add(createNewsFeedEntry("chrisclark", 1234567892, "News feed entry title", "News feed entry body", new Date()));

        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);
        assertEquals(3, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries());
    }

    @Test
    public void test_getNumberOfNewsFeedEntriesForContentSource_ReturnsZero_WhenANullContentSourceIsGiven() {
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(null, DateUtils.getXDaysAgo(7), DateUtils.getToday()));
    }

    @Test
    public void test_getNumberOfNewsFeedEntriesForContentSource_ReturnsZero_WhenANullDateIsGiven() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSource, null, DateUtils.getToday()));
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSource, DateUtils.getXDaysAgo(7), null));
    }

    @Test
    public void test_getNumberOfNewsFeedEntriesForContentSource_ReturnsZero_WhenThereAreNoNewsFeedEntries() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSource, DateUtils.getXDaysAgo(7), DateUtils.getToday()));
    }

    @Test
    public void test_getNumberOfNewsFeedEntriesForContentSource_ReturnsNonZero_WhenThereAreSomeNewsFeedEntries() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSource, DateUtils.getXDaysAgo(7), DateUtils.getToday()));

        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        for (int i = 1; i <= 7; i++) {
            newsFeedEntries.add(createNewsFeedEntry("simonbrown", i, "News feed entry title", "News feed entry body", DateUtils.getXDaysAgo(7-i)));
        }
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        assertEquals(3, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSource, DateUtils.getXDaysAgo(6), DateUtils.getXDaysAgo(4)));
    }

    @Test
    public void test_getNumberOfNewsFeedEntriesForContentSources_ReturnsZero_WhenNullIsSpecified() {
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(null));
    }

    @Test
    public void test_getNumberOfNewsFeedEntriesForContentSources_ReturnsZero_WhenAnEmptyListIsSpecified() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSources));
    }

    @Test
    public void test_getNumberOfNewsFeedEntriesForContentSources_ReturnsZero_WhenThereAreNone() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSources));
    }

    @Test
    public void test_getNumberOfNewsFeedEntriesForContentSources_ReturnsNonZero_WhenThereAreSome() {
        Collection<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));

        Collection<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        newsFeedEntries.add(createNewsFeedEntry("simonbrown", 1234567890, "News feed entry title", "News feed entry body", new Date()));
        newsFeedEntries.add(createNewsFeedEntry("simonbrown", 1234567891, "News feed entry title", "News feed entry body", new Date()));
        newsFeedEntries.add(createNewsFeedEntry("chrisclark", 1234567892, "News feed entry title", "News feed entry body", new Date()));

        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);
        assertEquals(2, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSources));

        contentSources.add(getContentSourceComponent().findByShortName("chrisclark"));
        assertEquals(3, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries(contentSources));
    }

    @Test
    public void test_storeNewsFeedEntries_DoesntBreak_WhenNullIsSpecified() {
        getNewsFeedEntryComponent().storeNewsFeedEntries(null);
    }

    @Test
    public void test_storeNewsFeedEntries_DoesntBreak_WhenAnEmptyCollectionIsSpecified() {
        getNewsFeedEntryComponent().storeNewsFeedEntries(new LinkedList<NewsFeedEntry>());
    }

    @Test
    public void test_newsFeedEntryDetailsAreStoredAndRetrievedCorrectly() {
        Date date = DateUtils.getXDaysAgo(3);
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        newsFeedEntries.add(createNewsFeedEntry("simonbrown", 1234567890, "News feed entry title", "News feed entry body", date));
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        NewsFeedEntry newsFeedEntry = getNewsFeedEntryComponent().getRecentNewsFeedEntries(1, 1).get(0);
        assertEquals("Simon Brown", newsFeedEntry.getContentSource().getName());
        assertEquals("http://www.somedomain.com/1234567890", newsFeedEntry.getPermalink());
        assertEquals("News feed entry title 1234567890", newsFeedEntry.getTitle());
        assertEquals("News feed entry body", newsFeedEntry.getBody());
        assertEquals(date, newsFeedEntry.getTimestamp());
    }

    @Test
    public void test_StoringANewsFeedEntry_WhenANewsFeedEntryAlreadyExistsWithADifferentPermalink_DoesntResultInADuplicate() {
        Date date = DateUtils.getXDaysAgo(3);
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        newsFeedEntries.add(new NewsFeedEntry("http://www.somedomain.com/123", "Title", "Body", date, getContentSourceComponent().findByShortName("simonbrown")));
        newsFeedEntries.add(new NewsFeedEntry("http://www.somedomain.com/321", "Title", "Body", date, getContentSourceComponent().findByShortName("simonbrown")));
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        // check that only the first was stored
        assertEquals(1, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries());
        assertEquals("http://www.somedomain.com/123", getNewsFeedEntryComponent().getRecentNewsFeedEntries(1, 1).get(0).getPermalink());
    }

    @Test
    public void test_StoringANewsFeedEntry_WhenANewsFeedEntryAlreadyExistsWithADifferentDate_DoesntResultInADuplicate() {
        Date yesterday = DateUtils.getXDaysAgo(1);
        Date today = DateUtils.getToday();
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        newsFeedEntries.add(new NewsFeedEntry("http://www.somedomain.com/123", "Title", "Body", yesterday, getContentSourceComponent().findByShortName("simonbrown")));
        newsFeedEntries.add(new NewsFeedEntry("http://www.somedomain.com/123", "Title", "Body", today, getContentSourceComponent().findByShortName("simonbrown")));
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        // check that only the first was stored
        assertEquals(1, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries());
        assertEquals(yesterday, getNewsFeedEntryComponent().getRecentNewsFeedEntries(1, 1).get(0).getTimestamp());
    }

    @Test
    public void test_RemovingANewsFeedEntry_WhenANewsFeedEntryExists_RemovesTheNewsFeedEntry() {
        Date today = DateUtils.getToday();
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        newsFeedEntries.add(new NewsFeedEntry("http://www.somedomain.com/123", "Title", "Body", today, getContentSourceComponent().findByShortName("simonbrown")));
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        assertEquals(1, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries());
        getNewsFeedEntryComponent().removeNewsFeedEntry("http://www.somedomain.com/123");
        assertEquals(0, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries());
    }

    @Test
    public void test_RemovingANewsFeedEntry_WhenANewsFeedEntryDoesntExist_DoesntRemoveAnything() {
        Date today = DateUtils.getToday();
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        newsFeedEntries.add(new NewsFeedEntry("http://www.somedomain.com/123", "Title", "Body", today, getContentSourceComponent().findByShortName("simonbrown")));
        getNewsFeedEntryComponent().storeNewsFeedEntries(newsFeedEntries);

        assertEquals(1, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries());
        getNewsFeedEntryComponent().removeNewsFeedEntry("http://www.otherdomain.com/123");
        assertEquals(1, getNewsFeedEntryComponent().getNumberOfNewsFeedEntries());
    }

    private NewsFeedEntry createNewsFeedEntry(String contentSourceShortName, long id, String title, String body, Date date) {
        return new NewsFeedEntry("http://www.somedomain.com/" + id, title + " " + id, body, date, getContentSourceComponent().findByShortName(contentSourceShortName));
    }

    @After
    public void tearDown() {
        Mongo mongo = (Mongo)applicationContext.getBean("mongo");
        mongo.getDB("techtribesje_test").getCollection("newsFeedEntries").drop();
    }

}
