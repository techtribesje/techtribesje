package je.techtribes.web.controller;

import je.techtribes.domain.NewsFeedEntry;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RssControllerTests extends AbstractControllerTestsBase {

    @Test
    public void test_generateRssFeed() {
        RssController controller = new RssController(new NewsFeedEntryComponentStub());

        ModelMap model = new ModelMap();
        View view = controller.generateRssFeed(model);

        List<NewsFeedEntry> newsFeedEntries = (List<NewsFeedEntry>)model.get("newsFeedEntries");
        assertEquals(12, newsFeedEntries.size());

        assertTrue(view instanceof InternalResourceView);
        assertEquals("/WEB-INF/views-other/rss.jsp", ((InternalResourceView)view).getUrl());
    }

}
