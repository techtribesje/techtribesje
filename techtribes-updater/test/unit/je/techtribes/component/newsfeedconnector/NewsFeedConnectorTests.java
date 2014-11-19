package je.techtribes.component.newsfeedconnector;

import je.techtribes.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NewsFeedConnectorTests {

    private NewsFeedConnector newsFeedConnector;
    private MockNewsFeedInterface newsFeedInterface;

    @Before
    public void setUp() {
        this.newsFeedInterface = new MockNewsFeedInterface();
        this.newsFeedConnector = new NewsFeedConnectorImpl(newsFeedInterface);
    }

    @Test
    public void test_loadNewsFeedEntries_DoesntIncludeNewsFeedEntries_WhenTheContentSourceIsDQMagazineAndJerseyIsNotMentionedInTheBody() throws Exception {
        ContentSource contentSource = ContentSourceFactory.create(ContentSourceType.Media);
        contentSource.setName("Digital Quadrant Magazine");

        setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Some content from Jersey", new Date(), contentSource));
        assertEquals(1, newsFeedConnector.loadNewsFeedEntries(null).size());

        setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Some content from JERSEY", new Date(), contentSource));
        assertEquals(1, newsFeedConnector.loadNewsFeedEntries(null).size());
    }

    @Test
    public void test_loadNewsFeedEntries_IncludesNewsFeedEntries_WhenTheContentSourceIsDQMagazineAndGuernseyIsMentionedInTheBody() throws Exception {
        ContentSource contentSource = ContentSourceFactory.create(ContentSourceType.Media);
        contentSource.setName("Digital Quadrant Magazine");

        setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Some content from Guernsey", new Date(), contentSource));
        assertEquals(1, newsFeedConnector.loadNewsFeedEntries(null).size());

        setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Some content from GUERNSEY", new Date(), contentSource));
        assertEquals(1, newsFeedConnector.loadNewsFeedEntries(null).size());
    }

    @Test
    public void test_loadNewsFeedEntries_IncludesNewsFeedEntries_WhenTheContentSourceIsDQMagazineAndChannelIslandsIsMentionedInTheBody() throws Exception {
        ContentSource contentSource = ContentSourceFactory.create(ContentSourceType.Media);
        contentSource.setName("Digital Quadrant Magazine");

        setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Some content from the Channel Islands", new Date(), contentSource));
        assertEquals(1, newsFeedConnector.loadNewsFeedEntries(null).size());

        setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Some content from CHANNEL ISLANDS", new Date(), contentSource));
        assertEquals(1, newsFeedConnector.loadNewsFeedEntries(null).size());
    }

    @Test
    public void test_loadNewsFeedEntries_ExcludesNewsFeedEntries_WhenTheContentSourceIsDQMagazineAndJerseyGuernseyOrChannelIslandsIsntMentionedInTheBody() throws Exception {
        ContentSource contentSource = ContentSourceFactory.create(ContentSourceType.Media);
        contentSource.setName("Digital Quadrant Magazine");

        setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Some content from Singapore", new Date(), contentSource));
        assertEquals(0, newsFeedConnector.loadNewsFeedEntries(null).size());
    }

    @Test
    public void test_loadNewsFeedEntries_ExcludesNewsFeedEntries_WhenTheContentSourceIsAMediaTribeAndNoneOfTheKeywordTriggersAreMentionedInTheTitleOrBody() throws Exception {
        ContentSource contentSource = ContentSourceFactory.create(ContentSourceType.Media);
        contentSource.setName("Jersey Evening Post");

        setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Jersey cow runs down the road in St Lawrence", new Date(), contentSource));
        assertEquals(0, newsFeedConnector.loadNewsFeedEntries(null).size());
    }

    @Test
    public void test_loadNewsFeedEntries_IncludesNewsFeedEntries_WhenTheContentSourceIsAMediaTribeAndTheKeywordTriggersAreMentionedInTheTitle() throws Exception {
        ContentSource contentSource = ContentSourceFactory.create(ContentSourceType.Media);
        contentSource.setName("Jersey Evening Post");

        for (String keyword : Tribe.MEDIA_TRIBE_KEYWORD_TRIGGERS) {
            setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title about " + keyword, "Some news story about something" + keyword, new Date(), contentSource));
            assertEquals(1, newsFeedConnector.loadNewsFeedEntries(null).size());
        }
    }

    @Test
    public void test_loadNewsFeedEntries_IncludesNewsFeedEntries_WhenTheContentSourceIsAMediaTribeAndTheKeywordTriggersAreMentionedInTheBody() throws Exception {
        ContentSource contentSource = ContentSourceFactory.create(ContentSourceType.Media);
        contentSource.setName("Jersey Evening Post");

        for (String keyword : Tribe.MEDIA_TRIBE_KEYWORD_TRIGGERS) {
            setNewsFeedEntries(new NewsFeedEntry("http://www.domain.com/permalink", "Title", "Some news story about " + keyword, new Date(), contentSource));
            assertEquals(1, newsFeedConnector.loadNewsFeedEntries(null).size());
        }
    }

    private void setNewsFeedEntries(NewsFeedEntry... newsFeedEntriesArray) {
        newsFeedInterface.setNewsFeedEntries(Arrays.asList(newsFeedEntriesArray));
    }

}
