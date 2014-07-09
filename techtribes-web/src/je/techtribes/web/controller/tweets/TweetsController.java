package je.techtribes.web.controller.tweets;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.ComponentDependency;
import com.structurizr.annotation.UsedBy;
import je.techtribes.domain.Tweet;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.util.PageSize;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@Component(description = "Allows users to view tweets.")
@UsedBy( person = "Anonymous User", description = "uses" )
public class TweetsController extends AbstractController {

    @ComponentDependency(description = "Gets tweets from")
    private TweetComponent tweetComponent;

    @Autowired
    public TweetsController(TweetComponent tweetComponent) {
        this.tweetComponent = tweetComponent;
    }

    @RequestMapping(value="/tweets", method = RequestMethod.GET)
	public String viewRecentTweetsByPage(ModelMap model) {
        return viewRecentTweetsByPage(1, model);
	}

    @RequestMapping(value="/tweets/{page:[\\d]+}", method = RequestMethod.GET)
	public String viewRecentTweetsByPage(@PathVariable("page")int page, ModelMap model) {
        int maxPage = PageSize.calculateNumberOfPages(tweetComponent.getNumberOfTweets(), PageSize.RECENT_TWEETS);
        page = PageSize.validatePage(page, maxPage);
        List<Tweet> tweets = tweetComponent.getRecentTweets(page, PageSize.RECENT_TWEETS);

        model.addAttribute("tweets", tweets);
        model.addAttribute("currentPage", page);
        model.addAttribute("maxPage", maxPage);
        addCommonAttributes(model);
        setPageTitle(model, "Tweets", "Page " + page);

		return "tweets";
	}

}
