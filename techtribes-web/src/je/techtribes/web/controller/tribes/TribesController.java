package je.techtribes.web.controller.tribes;

import com.structurizr.annotation.UsedByPerson;
import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.badge.BadgeComponent;
import je.techtribes.component.event.EventComponent;
import je.techtribes.component.github.GitHubComponent;
import je.techtribes.component.job.JobComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryException;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.search.SearchResult;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.component.tweet.TweetException;
import je.techtribes.domain.*;
import je.techtribes.domain.badge.AwardedBadge;
import je.techtribes.domain.badge.Badge;
import je.techtribes.domain.badge.BadgeType;
import je.techtribes.domain.badge.Badges;
import je.techtribes.util.PageSize;
import je.techtribes.util.comparator.AwardedBadgeComparator;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Shows a summary of a tribe; including contents, tweets, talks, etc.
 */
@Controller
@UsedByPerson( name = "Anonymous User", description = "uses" )
public class TribesController extends AbstractController {

    private BadgeComponent badgeComponent;
    private NewsFeedEntryComponent newsFeedEntryComponent;
    private TweetComponent tweetComponent;
    private ActivityComponent activityComponent;
    private JobComponent jobService;
    private EventComponent eventService;
    private GitHubComponent gitHubComponent;
    private SearchComponent searchComponent;

    @Autowired
    public TribesController(BadgeComponent badgeComponent, NewsFeedEntryComponent newsFeedEntryComponent, TweetComponent tweetComponent, ActivityComponent activityComponent, JobComponent jobService, EventComponent eventService, GitHubComponent gitHubComponent, SearchComponent searchComponent) {
        this.badgeComponent = badgeComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.tweetComponent = tweetComponent;
        this.activityComponent = activityComponent;
        this.jobService = jobService;
        this.eventService = eventService;
        this.gitHubComponent = gitHubComponent;
        this.searchComponent = searchComponent;
    }

    @RequestMapping(value = "/tech", method = RequestMethod.GET)
	public String viewTechTribes(ModelMap model) {
        return viewTribesByType(model, ContentSourceType.Tech, "tribes-tech", "Tech tribes");
	}

    @RequestMapping(value = "/community", method = RequestMethod.GET)
	public String viewSocialTribes(ModelMap model) {
        return viewTribesByType(model, ContentSourceType.Community, "tribes-community", "Community tribes");
	}

    @RequestMapping(value = "/business", method = RequestMethod.GET)
	public String viewBusinessTribes(ModelMap model) {
        return viewTribesByType(model, ContentSourceType.Business, "tribes-business", "Business tribes");
	}

    @RequestMapping(value = "/media", method = RequestMethod.GET)
	public String viewMediaTribes(ModelMap model) {
        return viewTribesByType(model, ContentSourceType.Media, "tribes-media", "Media tribes");
	}

    @RequestMapping(value = "/tribes", method = RequestMethod.GET)
	public String viewTribes(ModelMap model) {
        return viewBusinessTribes(model);
	}

	public String viewTribesByType(ModelMap model, ContentSourceType type, String view, String title) {
        List<ContentSource> tribes = contentSourceComponent.getContentSources(type);

        model.addAttribute("tribes", tribes);
        addCommonAttributes(model);
        setPageTitle(model, title);

        return view;
	}

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}", method = RequestMethod.GET)
	public String viewTribe(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = findTribeByShortName(shortName);
        if (contentSource == null) {
            return "forward:/404";
        }

        Tribe tribe = (Tribe)contentSource;

        // redirect to the jobs page for tribes where we're not aggregating content
        if (tribe.getType() == ContentSourceType.Tech) {
            return "redirect:/tribes/" + tribe.getShortName() + "/content";
        }

        Activity activity = activityComponent.getActivity(contentSource);
        List<AwardedBadge> badges = badgeComponent.getAwardedBadges(contentSource);
        addUnawardedBadges(badges, Badges.getBadges(), contentSource);
        Collections.sort(badges, new AwardedBadgeComparator());

        model.addAttribute("tribe", contentSource);
        model.addAttribute("badges", badges);
        model.addAttribute("activeNav", "summary");
        model.addAttribute("activity", activity);
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName());

        return "tribe";
	}

    private void addUnawardedBadges(List<AwardedBadge> awardedBadges, List<Badge> badges, ContentSource contentSource) {
        for (Badge badge : badges) {
            if (badge.getType() == BadgeType.Tribe || badge.getType() == BadgeType.PersonAndTribe) {
                AwardedBadge awardedBadge = new AwardedBadge(badge, contentSource);

                if (!awardedBadges.contains(awardedBadge)) {
                    awardedBadge.setAwarded(false);
                    awardedBadges.add(awardedBadge);
                }
            }
        }
    }

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/tweets", method = RequestMethod.GET)
	public String viewTribeTweets(@PathVariable("name")String shortName, ModelMap model) {
        return viewTribeTweets(shortName, 1, model);
    }

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/tweets/{page:[\\d]+}", method = RequestMethod.GET)
	public String viewTribeTweets(@PathVariable("name")String shortName, @PathVariable("page")int page, ModelMap model) {
        ContentSource contentSource = findTribeByShortName(shortName);
        if (contentSource == null) {
            return "forward:/404";
        }

        Tribe tribe = (Tribe)contentSource;
        model.addAttribute("tribe", tribe);
        model.addAttribute("activeNav", "tweets");
        addCommonAttributes(model);

        if (tribe.getType() == ContentSourceType.Tech) {
            String query = tribe.getSearchTerms();
            int maxPage = Integer.MAX_VALUE;
            page = PageSize.validatePage(page, maxPage);

            List<SearchResult> searchResults = searchComponent.searchForTweets(query, PageSize.RECENT_TWEETS, page);
            model.addAttribute("tweets", searchResults);
            model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(searchResults).getStatistics());
            setPageTitle(model, contentSource.getName(), "Tweets", "Page " + page);

            model.addAttribute("currentPage", page);
            if (searchResults.size() < PageSize.RECENT_TWEETS) {
                model.addAttribute("maxPage", page);
            } else {
                model.addAttribute("maxPage", maxPage);
            }

            return "tribe-tweets-search";
        } else {
            List<ContentSource> contentSources = new LinkedList<>();
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
                model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(tweets).getStatistics());
                setPageTitle(model, tribe.getName(), "Tweets", "Page " + page);

            } else {
                setPageTitle(model, tribe.getName(), "Tweets");
            }

            return "tribe-tweets";
        }
	}

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/content", method = RequestMethod.GET)
	public String viewTribeContent(@PathVariable("name")String shortName, ModelMap model) {
        return viewTribeContent(shortName, 1, model);
    }

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/content/{page:[\\d]+}", method = RequestMethod.GET)
	public String viewTribeContent(@PathVariable("name")String shortName, @PathVariable("page")int page, ModelMap model) {
        ContentSource contentSource = findTribeByShortName(shortName);
        if (contentSource == null) {
            return "forward:/404";
        }

        Tribe tribe = (Tribe)contentSource;
        model.addAttribute("tribe", contentSource);
        model.addAttribute("activeNav", "content");
        addCommonAttributes(model);

        if (tribe.getType() == ContentSourceType.Tech) {
            String query = tribe.getSearchTerms();
            int maxPage = Integer.MAX_VALUE;
            page = PageSize.validatePage(page, maxPage);

            List<SearchResult> searchResults = searchComponent.searchForNewsFeedEntries(query, PageSize.RECENT_NEWS_FEED_ENTRIES, page);
            model.addAttribute("newsFeedEntries", searchResults);
            model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(searchResults).getStatistics());
            setPageTitle(model, contentSource.getName(), "Content", "Page " + page);

            model.addAttribute("currentPage", page);
            if (searchResults.size() < PageSize.RECENT_NEWS_FEED_ENTRIES) {
                model.addAttribute("maxPage", page);
            } else {
                model.addAttribute("maxPage", maxPage);
            }

            return "tribe-content-search";
        } else {
            List<ContentSource> contentSources = new LinkedList<>();
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
                setPageTitle(model, contentSource.getName(), "Content", "Page " + page);
                model.addAttribute("newsFeedEntries", newsFeedEntries);
                model.addAttribute("currentPage", page);
                model.addAttribute("maxPage", maxPage);
                model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(newsFeedEntries).getStatistics());

            } else {
                setPageTitle(model, contentSource.getName(), "Content");
            }

            return "tribe-content";
        }
	}

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/jobs", method = RequestMethod.GET)
	public String viewTribeJobs(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = findTribeByShortName(shortName);
        if (contentSource == null) {
            return "forward:/404";
        }

        List<Job> jobs = jobService.getRecentJobs(contentSource, 12, false);

        model.addAttribute("tribe", contentSource);
        model.addAttribute("jobs", jobs);
        model.addAttribute("activeNav", "jobs");
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName(), "Jobs");

        return "tribe-jobs";
	}

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/events", method = RequestMethod.GET)
	public String viewTribeEvents(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = findTribeByShortName(shortName);
        if (contentSource == null) {
            return "forward:/404";
        }

        List<Event> events = eventService.getEvents(contentSource, 12);

        model.addAttribute("tribe", contentSource);
        model.addAttribute("events", events);
        model.addAttribute("activeNav", "events");
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName(), "Events");

        return "tribe-events";
	}

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/code", method = RequestMethod.GET)
	public String viewCodeByTribe(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = findTribeByShortName(shortName);
        if (contentSource == null) {
            return "forward:/404";
        }

        model.addAttribute("tribe", contentSource);
        model.addAttribute("activeNav", "code");
        if (contentSource.hasGitHubId()) {
            model.addAttribute("gitHubRepositories", gitHubComponent.getRepositories(contentSource));
        }
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName(), "Code");

        return "tribe-code";
	}

}
