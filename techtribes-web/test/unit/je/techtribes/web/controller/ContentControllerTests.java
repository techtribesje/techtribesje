package je.techtribes.web.controller;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.NewsFeedEntry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContentControllerTests extends AbstractControllerTestsBase {

    private ContentController controller;

    @Before
    public void setUp() {
        controller = new ContentController(new NewsFeedEntryComponentStub());
        controller.setContentSourceComponent(new ContentSourceComponentStub());
    }

    @Test
    public void test_viewRecentContent_ReturnsPageOneOfContent_WhenPageOneIsRequested() {
        ModelMap model = new ModelMap();
        String view = controller.viewRecentContent(model);
        assertModelAndView(model, view, 1);
    }

    @Test
    public void test_viewRecentContent_ReturnsPageOneOfContent_WhenPageZeroIsRequested() {
        ModelMap model = new ModelMap();
        String view = controller.viewRecentContent(0, model);
        assertModelAndView(model, view, 1);
    }

    @Test
    public void test_viewRecentContent_ReturnsPageTwoOfContent_WhenPageTwoIsRequested() {
        ModelMap model = new ModelMap();
        String view = controller.viewRecentContent(2, model);
        assertModelAndView(model, view, 2);
    }

    @Test
    public void test_viewRecentContent_ReturnsTheLastPageOfContent_WhenAPageThatIsTooHighIsRequested() {
        ModelMap model = new ModelMap();
        String view = controller.viewRecentContent(99999999, model);
        assertModelAndView(model, view, 100);
    }

    private void assertModelAndView(ModelMap model, String view, int page) {
        List<NewsFeedEntry> newsFeedEntries = (List<NewsFeedEntry>)model.get("newsFeedEntries");
        assertEquals(12, newsFeedEntries.size());
        int i = 1 + ((page-1) * 12);
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            assertEquals("http://somedomain.com/" + i, newsFeedEntry.getPermalink());
            i++;
        }

        assertEquals(page, model.get("currentPage"));
        assertEquals(100, model.get("maxPage"));
        assertEquals("techtribes.je - Content - Page " + page, model.get("pageTitle"));
        assertEquals(100, ((Collection<ContentSource>) model.get("peopleAndTribes")).size());
        assertEquals("content", view);
    }

}
