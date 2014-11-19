package je.techtribes.component.newsfeedconnector;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.*;
import je.techtribes.util.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NewsFeedConnectorTests extends AbstractComponentTestsBase {

    private NewsFeedConnector newsFeedConnector;
    private MockNewsFeedInterface newsFeedInterface;

    @Before
    public void setUp() {
        this.newsFeedConnector = getNewsFeedConnector();
    }

    @Test
    public void test_loadNewsFeedEntries_Works() throws Exception {
        ContentSource contentSource = ContentSourceFactory.create(ContentSourceType.Person);
        contentSource.setName("Simon Brown");

        NewsFeed newsFeed = new NewsFeed("https://raw.githubusercontent.com/techtribesje/techtribesje/master/techtribes-updater/test/component/je/techtribes/component/newsfeedconnector/codingthearchitecture.xml", contentSource);
        List<NewsFeedEntry> newsFeedEntries = newsFeedConnector.loadNewsFeedEntries(newsFeed);
        assertEquals(10, newsFeedEntries.size());

        assertNewsFeedEntry(newsFeedEntries.get(0), "Speaking in Australia - YOW! 2014", "<p>\n" +
                        "For my final trip of the year, I'm heading to Australia at the end of this month for the <a href=\"http://2014.yowconference.com.au\">YOW! 2014</a> series of conferences. I'll be presenting <a href=\"https://a.confui.com/-A4d5Y9tJ\">Agility and the essence of software architecture</a> in Melbourne, Brisbane and Sydney.\n" +
                        "Plus I'll be running my <a href=\"https://a.confui.com/-RsaB7oCm\">Simple sketches for diagramming your software architecture</a> workshop in Melbourne and Sydney. I can't wait; see you there!\n" +
                        "</p>",
                "http://www.codingthearchitecture.com/2014/11/13/speaking_in_australia_yow_2014.html",
                DateUtils.getDate(2014, 11, 13, 9, 37, 0), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(1), "Simple sketches for diagramming your software architecture",
                "https://www.voxxed.com/blog/2014/10/simple-sketches-for-diagramming-your-software-architecture/",
                DateUtils.getDate(2014, 11, 12, 22, 2, 0), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(2), "【翻訳】マイクロサービス – 分散された大きな泥だんご",
                "http://postd.cc/distributed_big_balls_of_mud/",
                DateUtils.getDate(2014, 11, 12, 20, 6, 0), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(3), "Online training now available via Parleys",
                "http://www.codingthearchitecture.com/2014/11/12/online_training_now_available_via_parleys.html",
                DateUtils.getDate(2014, 11, 12, 12, 43, 0), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(4), "程序员必读之软件架构",
                "http://www.codingthearchitecture.com/2014/11/10/1415634000000.html",
                DateUtils.getDate(2014, 11, 10, 15, 40, 0), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(5), "Software architecture vs code (DevDay 2014)",
                "http://www.codingthearchitecture.com/2014/10/27/software_architecture_vs_code_devday_2014.html",
                DateUtils.getDate(2014, 10, 27, 18, 10, 32), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(6), "Software architecture sketching in Iceland",
                "http://www.codingthearchitecture.com/2014/10/23/software_architecture_sketching_in_iceland.html",
                DateUtils.getDate(2014, 10, 23, 10, 46, 0), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(7), "Comparing solutions",
                "http://www.codingthearchitecture.com/2014/10/22/comparing_solutions.html",
                DateUtils.getDate(2014, 10, 22, 14, 38, 0), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(8), "Modularity and testability",
                "http://www.codingthearchitecture.com/2014/10/01/modularity_and_testability.html",
                DateUtils.getDate(2014, 10, 1, 22, 21, 26), contentSource);

        assertNewsFeedEntry(newsFeedEntries.get(9), "Firm foundations",
                "http://www.asas2014.nl/posts/VAhgjyYAACMAYla5/firm-foundations",
                DateUtils.getDate(2014, 9, 9, 12, 32, 0), contentSource);

    }

    private void assertNewsFeedEntry(NewsFeedEntry newsFeedEntry, String title, String body, String permalink, Date date, ContentSource contentSource) {
        assertNewsFeedEntry(newsFeedEntry, title, permalink, date, contentSource);
        assertEquals(body, newsFeedEntry.getBody().trim());
    }

    private void assertNewsFeedEntry(NewsFeedEntry newsFeedEntry, String title, String permalink, Date date, ContentSource contentSource) {
        assertEquals(title, newsFeedEntry.getTitle());
        assertEquals(permalink, newsFeedEntry.getPermalink());
        assertEquals(date, newsFeedEntry.getTimestamp());
        assertEquals(contentSource, newsFeedEntry.getContentSource());
    }

}
