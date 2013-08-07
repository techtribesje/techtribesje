package je.techtribes.web.controller;

import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.badge.BadgeComponent;
import je.techtribes.component.github.GitHubComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryException;
import je.techtribes.domain.Talk;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.domain.Tweet;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.component.tweet.TweetException;
import je.techtribes.domain.*;
import je.techtribes.domain.badge.AwardedBadge;
import je.techtribes.util.PageSize;
import je.techtribes.util.comparator.AwardedBadgeComparator;
import je.techtribes.util.comparator.ContentSourceByTwitterFollowersCountDescendingComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class PeopleController extends AbstractController {

    private BadgeComponent badgeComponent;
    private NewsFeedEntryComponent newsFeedEntryComponent;
    private TweetComponent tweetComponent;
    private TalkComponent talksService;
    private ActivityComponent activityComponent;
    private GitHubComponent gitHubComponent;

    @Autowired
    public PeopleController(BadgeComponent badgeComponent, NewsFeedEntryComponent newsFeedEntryComponent, TweetComponent tweetComponent, TalkComponent talksService, ActivityComponent activityComponent, GitHubComponent gitHubComponent) {
        this.badgeComponent = badgeComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.tweetComponent = tweetComponent;
        this.talksService = talksService;
        this.activityComponent = activityComponent;
        this.gitHubComponent = gitHubComponent;
    }

    @RequestMapping(value = "/people", method = RequestMethod.GET)
	public String viewPeople(ModelMap model, HttpServletRequest request) {
        List<ContentSource> people = contentSourceComponent.getContentSources(ContentSourceType.Person);

        String sort = request.getParameter("sort");
        if (sort != null) {
            if (sort.equalsIgnoreCase("followers")) {
                Collections.sort(people, new ContentSourceByTwitterFollowersCountDescendingComparator());
                model.addAttribute("sort", "followers");
            } else if (sort.equalsIgnoreCase("activity")) {
                List<ContentSource> contentSources = new LinkedList<>();
                for (Activity activity : activityComponent.getActivityListForPeople()) {
                    contentSources.add(activity.getContentSource());
                }
                people = contentSources;
                model.addAttribute("sort", "activity");
            } else {
                model.addAttribute("sort", "name");
            }
        }

        model.addAttribute("people", people);
        model.addAttribute("numberOfPeople", people.size());
        addCommonAttributes(model);
        setPageTitle(model, "People");

        return "people";
	}

    @RequestMapping(value="/people/{name:^[a-z-0-9]*$}", method = RequestMethod.GET)
	public String viewPerson(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        List<AwardedBadge> badges = badgeComponent.getAwardedBadges(contentSource);
        Collections.sort(badges, new AwardedBadgeComparator());
        Activity activity = activityComponent.getActivity(contentSource);

        model.addAttribute("person", contentSource);
        model.addAttribute("badges", badges);
        model.addAttribute("activeNav", "summary");
        model.addAttribute("activity", activity);
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName());

        return "person";
	}

    @RequestMapping(value="/people/{name:^[a-z-0-9]*$}/talks", method = RequestMethod.GET)
	public String viewTalksByPerson(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);

        List<Talk> talks = talksService.getTalks(contentSource);
        Set<String> countries = new TreeSet<String>();

        for (Talk talk : talks) {
            countries.add(talk.getCountry());
        }

        model.addAttribute("person", contentSource);
        model.addAttribute("talks", talks);
        model.addAttribute("countries", countries);
        model.addAttribute("numberOfCountries", countries.size());
        model.addAttribute("activeNav", "talks");
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName(), "Talks");

        return "person-talks";
	}

    @RequestMapping(value="/people/{name:^[a-z-0-9]*$}/code", method = RequestMethod.GET)
	public String viewCodeByPerson(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);

        model.addAttribute("person", contentSource);
        model.addAttribute("activeNav", "code");
        if (contentSource.hasGitHubId()) {
            model.addAttribute("gitHubRepositories", gitHubComponent.getRepositories(contentSource));
        }
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName(), "Code");

        return "person-code";
	}

    @RequestMapping(value="/people/{name:^[a-z-0-9]*$}/tweets", method = RequestMethod.GET)
	public String viewTweets(@PathVariable("name")String shortName, ModelMap model) {
        return viewTweets(shortName, 1, model);
    }

    @RequestMapping(value="/people/{name:^[a-z-0-9]*$}/tweets/{page:[\\d]+}", method = RequestMethod.GET)
	public String viewTweets(@PathVariable("name")String shortName, @PathVariable("page")int page, ModelMap model) {
        List<ContentSource> contentSources = new LinkedList<>();
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        contentSources.add(contentSource);

        List<Tweet> tweets = new LinkedList<>();
        long numberOfTweets = tweetComponent.getNumberOfTweets(contentSources);

        if (numberOfTweets > 0) {
            int maxPage = PageSize.calculateNumberOfPages(numberOfTweets, PageSize.RECENT_TWEETS);
            page = PageSize.validatePage(page, maxPage);

            try {
                tweets = tweetComponent.getRecentTweets(contentSources, page, PageSize.RECENT_TWEETS);
            } catch (TweetException tse) {
                loggingComponent.warn(this, "Couldn't retrieve tweets for " + shortName, tse);
            }

            model.addAttribute("tweets", tweets);
            model.addAttribute("currentPage", page);
            model.addAttribute("maxPage", maxPage);
            setPageTitle(model, contentSource.getName(), "Tweets", "Page " + page);
        } else {
            setPageTitle(model, contentSource.getName(), "Tweets");
        }

        model.addAttribute("person", contentSource);
        model.addAttribute("activeNav", "tweets");
        addCommonAttributes(model);

        return "person-tweets";
	}

    @RequestMapping(value="/people/{name:^[a-z-0-9]*$}/content", method = RequestMethod.GET)
	public String viewContent(@PathVariable("name")String shortName, ModelMap model) {
        return viewContent(shortName, 1, model);
    }

    @RequestMapping(value="/people/{name:^[a-z-0-9]*$}/content/{page:[\\d]+}", method = RequestMethod.GET)
	public String viewContent(@PathVariable("name")String shortName, @PathVariable("page")int page, ModelMap model) {
        List<ContentSource> contentSources = new LinkedList<>();
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        contentSources.add(contentSource);

        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        long numberOfNewsFeedEntries = newsFeedEntryComponent.getNumberOfNewsFeedEntries(contentSources);

        if (numberOfNewsFeedEntries > 0) {
            int maxPage = PageSize.calculateNumberOfPages(numberOfNewsFeedEntries, PageSize.RECENT_NEWS_FEED_ENTRIES);
            page = PageSize.validatePage(page, maxPage);

            try {
                newsFeedEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(contentSources, page, PageSize.RECENT_NEWS_FEED_ENTRIES);
            } catch (NewsFeedEntryException nfse) {
                loggingComponent.warn(this, "Couldn't retrieve content for " + shortName, nfse);
            }

            model.addAttribute("newsFeedEntries", newsFeedEntries);
            model.addAttribute("currentPage", page);
            model.addAttribute("maxPage", maxPage);
            setPageTitle(model, contentSource.getName(), "Content", "Page " + page);
        } else {
            setPageTitle(model, contentSource.getName(), "Content");
        }

        model.addAttribute("person", contentSource);
        model.addAttribute("activeNav", "content");
        addCommonAttributes(model);

        return "person-content";
	}

}
