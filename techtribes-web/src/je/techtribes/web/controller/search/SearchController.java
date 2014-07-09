package je.techtribes.web.controller.search;

import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.search.SearchResult;
import je.techtribes.component.search.SearchResultType;
import je.techtribes.domain.ContentSourceStatistics;
import je.techtribes.util.PageSize;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class SearchController extends AbstractController {

    private SearchComponent searchService;

    @Autowired
    public SearchController(SearchComponent searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(value="/search", method = RequestMethod.GET)
	public String searchByQuery(@RequestParam("q")String query, ModelMap model) {
        return searchByQuery(query, 1, model);
    }


    @RequestMapping(value="/search/{page:[\\d]+}", method = RequestMethod.GET)
	public String searchByQuery(@RequestParam("q")String query, @PathVariable("page")int page, ModelMap model) {
        int maxPage = Integer.MAX_VALUE;
        page = PageSize.validatePage(page, maxPage);

        addCommonAttributes(model);
        setPageTitle(model, "Search");

        if (query != null && !query.isEmpty()) {
            List<SearchResult> searchResults;
            int pageSize = 0;
            if (query.startsWith("#") || query.startsWith("@")) {
                pageSize = PageSize.RECENT_TWEETS;
                searchResults = searchService.searchForTweets(query, pageSize, page);
                model.addAttribute("searchType", SearchResultType.Tweet);
                model.addAttribute("tweets", searchResults);
            } else {
                pageSize = PageSize.RECENT_NEWS_FEED_ENTRIES;
                searchResults = searchService.searchForNewsFeedEntries(query, pageSize, page);
                model.addAttribute("searchType", SearchResultType.NewsFeedEntry);
                model.addAttribute("newsFeedEntries", searchResults);
            }

            model.addAttribute("currentPage", page);
            if (searchResults.size() < pageSize) {
                model.addAttribute("maxPage", page);
            } else {
                model.addAttribute("maxPage", maxPage);
            }
            setPageTitle(model, "Search", "Page " + page);

            model.addAttribute("query", query);
            try {
                model.addAttribute("encodedQuery", URLEncoder.encode(query, "UTF-8"));
            } catch (UnsupportedEncodingException uee) {
                loggingComponent.warn(this, uee.getMessage());
            }
            model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(searchResults).getStatistics());

            return "search";
        } else {
            return "redirect:/";
        }
	}

}
