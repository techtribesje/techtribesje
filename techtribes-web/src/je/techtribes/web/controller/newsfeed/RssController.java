package je.techtribes.web.controller.newsfeed;

import com.structurizr.annotation.UsedBy;
import je.techtribes.domain.ContentItem;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.util.PageSize;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.List;

@Controller
@UsedBy( person = "Anonymous User", description = "uses" )
public class RssController extends AbstractController {

    private NewsFeedEntryComponent newsFeedEntryComponent;

    @Autowired
    public RssController(NewsFeedEntryComponent newsFeedEntryComponent) {
        this.newsFeedEntryComponent = newsFeedEntryComponent;
    }

    @RequestMapping(value = "/rss.xml", method = RequestMethod.GET)
	public View generateRssFeed(ModelMap model) {
        List<? extends ContentItem> newsFeedEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(1, PageSize.RECENT_NEWS_FEED_ENTRIES);

        model.addAttribute("newsFeedEntries", newsFeedEntries);

        return new InternalResourceView("/WEB-INF/views-other/rss.jsp");
	}

}
