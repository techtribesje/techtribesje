package je.techtribes.web.controller.content;

import com.structurizr.annotation.UsedBy;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.domain.ContentItem;
import je.techtribes.util.PageSize;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Show recent content (news and blog posts).
 */
@Controller
@UsedBy( person = "Anonymous User", description = "uses" )
public class ContentController extends AbstractController {

    private NewsFeedEntryComponent newsFeedEntryComponent;

    @Autowired
    public ContentController(NewsFeedEntryComponent newsFeedEntryComponent) {
        this.newsFeedEntryComponent = newsFeedEntryComponent;
    }

    @RequestMapping(value = "/content", method = RequestMethod.GET)
	public String viewRecentContent(ModelMap model) {
        return viewRecentContent(1, model);
	}

    @RequestMapping(value="/content/{page:[\\d]+}", method = RequestMethod.GET)
	public String viewRecentContent(@PathVariable("page")int page, ModelMap model) {
        int maxPage = PageSize.calculateNumberOfPages(newsFeedEntryComponent.getNumberOfNewsFeedEntries(), PageSize.RECENT_NEWS_FEED_ENTRIES);
        page = PageSize.validatePage(page, maxPage);
        List<? extends ContentItem> newsFeedEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(page, PageSize.RECENT_NEWS_FEED_ENTRIES);

        model.addAttribute("newsFeedEntries", newsFeedEntries);
        model.addAttribute("currentPage", page);
        model.addAttribute("maxPage", maxPage);
        setPageTitle(model, "Content", "Page " + page);
        addCommonAttributes(model);

		return "content";
	}

}
