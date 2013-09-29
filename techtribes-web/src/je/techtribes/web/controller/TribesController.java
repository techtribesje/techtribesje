package je.techtribes.web.controller;

import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.badge.BadgeComponent;
import je.techtribes.domain.Event;
import je.techtribes.component.event.EventComponent;
import je.techtribes.component.github.GitHubComponent;
import je.techtribes.domain.Job;
import je.techtribes.component.job.JobComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryException;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.search.SearchResult;
import je.techtribes.domain.Talk;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.domain.Tweet;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.component.tweet.TweetException;
import je.techtribes.domain.*;
import je.techtribes.domain.badge.AwardedBadge;
import je.techtribes.util.PageSize;
import je.techtribes.util.comparator.AwardedBadgeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class TribesController extends AbstractController {

    private BadgeComponent badgeComponent;
    private NewsFeedEntryComponent newsFeedEntryComponent;
    private TweetComponent tweetComponent;
    private TalkComponent talkComponent;
    private ActivityComponent activityComponent;
    private JobComponent jobService;
    private EventComponent eventService;
    private GitHubComponent gitHubComponent;
    private SearchComponent searchComponent;

    @Autowired
    public TribesController(BadgeComponent badgeComponent, NewsFeedEntryComponent newsFeedEntryComponent, TweetComponent tweetComponent, TalkComponent talkComponent, ActivityComponent activityComponent, JobComponent jobService, EventComponent eventService, GitHubComponent gitHubComponent, SearchComponent searchComponent) {
        this.badgeComponent = badgeComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.tweetComponent = tweetComponent;
        this.talkComponent = talkComponent;
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
        List<Tribe> tribes = contentSourceComponent.getTribes();

        model.addAttribute("tribes", tribes);
        addCommonAttributes(model);
        setPageTitle(model, "Tribes");

        return "tribes";
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
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        if (contentSource.isPerson()) {
            loggingComponent.error(this, shortName + " is not a tribe");
            return "404";
        }
        Tribe tribe = (Tribe)contentSource;

        // redirect to the jobs page for tribes where we're not aggregating content
        if (!tribe.isContentAggregated()) {
            return "redirect:/tribes/" + tribe.getShortName() + "/jobs";
        } else if (tribe.getType() == ContentSourceType.Tech) {
            return "redirect:/tribes/" + tribe.getShortName() + "/content";
        }

        Activity activity = activityComponent.getActivity(contentSource);
        List<AwardedBadge> badges = badgeComponent.getAwardedBadges(contentSource);
        Collections.sort(badges, new AwardedBadgeComparator());

        model.addAttribute("tribe", contentSource);
        model.addAttribute("badges", badges);
        model.addAttribute("activeNav", "summary");
        model.addAttribute("activity", activity);
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName());

        return "tribe";
	}

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/tweets", method = RequestMethod.GET)
	public String viewTribeTweets(@PathVariable("name")String shortName, ModelMap model) {
        return viewTribeTweets(shortName, 1, model);
    }

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/tweets/{page:[\\d]+}", method = RequestMethod.GET)
	public String viewTribeTweets(@PathVariable("name")String shortName, @PathVariable("page")int page, ModelMap model) {
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        if (contentSource.isPerson()) {
            loggingComponent.error(this, shortName + " is not a tribe");
            return "404";
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
            Collection<ContentSource> contentSources = createListOfShortNamesForTribeAndMembers(tribe);

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
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        if (contentSource.isPerson()) {
            loggingComponent.error(this, shortName + " is not a tribe");
            return "404";
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
            Collection<ContentSource> contentSources = createListOfShortNamesForTribeAndMembers(tribe);

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

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/talks", method = RequestMethod.GET)
	public String viewTribeTalks(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        if (contentSource.isPerson()) {
            loggingComponent.error(this, shortName + " is not a tribe");
            return "404";
        }
        Tribe tribe = (Tribe)contentSource;

        List<Talk> talks = talkComponent.getTalks(createListOfMembers(tribe));

        Set<String> countries = new TreeSet<>();
        for (Talk talk : talks) {
            countries.add(talk.getCountry());
        }

        model.addAttribute("tribe", contentSource);
        model.addAttribute("talks", talks);
        model.addAttribute("countries", countries);
        model.addAttribute("numberOfCountries", countries.size());
        model.addAttribute("activeNav", "talks");
        model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(talks).getStatistics());
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName(), "Talks");

        return "tribe-talks";
	}

    @RequestMapping(value="/tribes/{name:^[a-z-0-9]*$}/jobs", method = RequestMethod.GET)
	public String viewTribeJobs(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);

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
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);

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
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);

        model.addAttribute("tribe", contentSource);
        model.addAttribute("activeNav", "code");
        if (contentSource.hasGitHubId()) {
            model.addAttribute("gitHubRepositories", gitHubComponent.getRepositories(contentSource));
        }
        addCommonAttributes(model);
        setPageTitle(model, contentSource.getName(), "Code");

        return "tribe-code";
	}

    private Collection<ContentSource> createListOfShortNamesForTribeAndMembers(Tribe tribe) {
        List<ContentSource> shortNames = new LinkedList<>();
        shortNames.add(tribe);
        shortNames.addAll(createListOfShortNamesForTribeMembers(tribe));

        return shortNames;
    }

    private Collection<ContentSource> createListOfShortNamesForTribeMembers(Tribe tribe) {
        if (tribe.getType() != ContentSourceType.Community) {
            List<ContentSource> list = new LinkedList<>();
            list.addAll(tribe.getMembers());
            return list;
        }

        return new LinkedList<>();
    }

    private Collection<ContentSource> createListOfMembers(Tribe tribe) {
        Collection<ContentSource> members = new LinkedList<>();
        if (tribe.getType() != ContentSourceType.Community) {
            for (Person person : tribe.getMembers()) {
                members.add(person);
            }
        }

        return members;
    }

}
