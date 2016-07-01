package je.techtribes.web.controller.news;

import com.structurizr.annotation.UsedByPerson;
import je.techtribes.domain.*;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
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
 * Shows recent news.
 */
@Controller
@UsedByPerson( name = "Anonymous User", description = "uses" )
public class NewsController extends AbstractController {

    private NewsFeedEntryComponent newsFeedEntryComponent;

    @Autowired
    public NewsController(NewsFeedEntryComponent newsFeedEntryComponent) {
        this.newsFeedEntryComponent = newsFeedEntryComponent;
    }

    @RequestMapping(value = "/news", method = RequestMethod.GET)
	public String viewRecentNews(ModelMap model) {
        return viewRecentNewsByPage(1, model);
    }

    @RequestMapping(value="/news/{page:[\\d]+}", method = RequestMethod.GET)
	public String viewRecentNewsByPage(@PathVariable("page")int page, ModelMap model) {
        List<ContentSource> mediaTribes = contentSourceComponent.getContentSources(ContentSourceType.Media);
        int maxPage = PageSize.calculateNumberOfPages(newsFeedEntryComponent.getNumberOfNewsFeedEntries(mediaTribes), PageSize.RECENT_NEWS);
        page = PageSize.validatePage(page, maxPage);
        List<? extends ContentItem> newsEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(mediaTribes, page, PageSize.RECENT_NEWS);

        model.addAttribute("newsEntries", newsEntries);
        model.addAttribute("currentPage", page);
        model.addAttribute("maxPage", maxPage);
        addCommonAttributes(model);
        setPageTitle(model, "News", "Page " + page);

        return "news";
	}

}
