package je.techtribes.web.controller.home;

import com.structurizr.annotation.UsedBy;
import je.techtribes.component.event.EventComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.domain.*;
import je.techtribes.domain.badge.Badge;
import je.techtribes.domain.badge.Badges;
import je.techtribes.util.DateUtils;
import je.techtribes.util.PageSize;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Shows the home page.
 */
@Controller
@RequestMapping("/")
@UsedBy( person = "Anonymous User", description = "uses" )
public class HomePageController extends AbstractController {

    private NewsFeedEntryComponent newsFeedEntryComponent;
    private TweetComponent tweetComponent;
    private TalkComponent talkComponent;
    private EventComponent eventComponent;

    @Autowired
    public HomePageController(NewsFeedEntryComponent newsFeedEntryComponent, TweetComponent tweetComponent, TalkComponent talkComponent, EventComponent eventComponent) {
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.tweetComponent = tweetComponent;
        this.talkComponent = talkComponent;
        this.eventComponent = eventComponent;
    }

	@RequestMapping(method = RequestMethod.GET)
	public String showHomePage(ModelMap model) {
        List<? extends ContentItem> newsFeedEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(1, 6);
        List<ContentSource> mediaTribes = contentSourceComponent.getContentSources(ContentSourceType.Media);
        List<? extends ContentItem> newsEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(mediaTribes, 1, 3);
        List<Tweet> tweets = tweetComponent.getRecentTweets(1, 12);
        List<Talk> talks = talkComponent.getUpcomingTalks(PageSize.UPCOMING_TALKS);

        List<Event> events = eventComponent.getFutureEvents(PageSize.RECENT_EVENTS);
        Collections.reverse(events);

        model.addAttribute("newsFeedEntries", newsFeedEntries);
        model.addAttribute("newsEntries", newsEntries);
        model.addAttribute("tweets", tweets);
        model.addAttribute("talks", talks);
        model.addAttribute("events", events);

        List<String> years = new LinkedList<>();
        int currentYear = DateUtils.getYear();
        for (int year = currentYear; year >= 2012; year--) {
            years.add("" + year);
        }
        model.addAttribute("years", years);

//        model.addAttribute("activityListForPeople", activityComponent.getActivityListForPeople());
//        model.addAttribute("activityListForBusinessTribes", activityComponent.getActivityListForBusinessTribes());
//        model.addAttribute("activityListForCommunityTribes", activityComponent.getActivityListForCommunityTribes());

        List<Badge> badges = Badges.getBadges();
        model.addAttribute("badges", badges);

        model.addAttribute("currentPage", 1);
        model.addAttribute("maxPage", 2);

        addCommonAttributes(model);
        setPageTitle(model);

        model.addAttribute("includeVideos", false);

		return "home";
	}

}
