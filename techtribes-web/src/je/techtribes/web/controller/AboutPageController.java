package je.techtribes.web.controller;

import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.Talk;
import je.techtribes.domain.Tribe;
import je.techtribes.util.comparator.ContentSourceByNameComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping("/about")
public class AboutPageController extends AbstractController {

    private NewsFeedEntryComponent newsFeedEntryComponent;
    private TweetComponent tweetComponent;
    private TalkComponent talkComponent;

    @Autowired
    public AboutPageController(NewsFeedEntryComponent newsFeedEntryComponent, TweetComponent tweetComponent, TalkComponent talkComponent) {
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.tweetComponent = tweetComponent;
        this.talkComponent = talkComponent;
    }

	@RequestMapping(method = RequestMethod.GET)
	public String showPage(ModelMap model) {
        addContentSourceStatistics(model);
        addTalkStatistics(model);
        addNewsFeedEntryStatistics(model);
        addTwitterStatistics(model);
        addCommonAttributes(model);
        model.addAttribute("team", ((Tribe)contentSourceComponent.findByShortName("techtribesje")).getMembers());

		return "about";
	}

    private void addContentSourceStatistics(ModelMap model) {
        List<ContentSource> people = contentSourceComponent.getContentSources(ContentSourceType.Person);
        List<ContentSource> businessTribes = contentSourceComponent.getContentSources(ContentSourceType.Business);
        List<ContentSource> communityTribes = contentSourceComponent.getContentSources(ContentSourceType.Community);
        List<ContentSource> mediaTribes = contentSourceComponent.getContentSources(ContentSourceType.Media);
        List<ContentSource> techTribes = contentSourceComponent.getContentSources(ContentSourceType.Tech);

        model.addAttribute("people", people);
        model.addAttribute("businessTribes", businessTribes);
        model.addAttribute("communityTribes", communityTribes);
        model.addAttribute("mediaTribes", mediaTribes);
        model.addAttribute("techTribes", techTribes);
    }

    private void addTalkStatistics(ModelMap model) {
        List<Talk> talks = talkComponent.getRecentTalks();
        Set<ContentSource> people = new TreeSet<>(new ContentSourceByNameComparator());
        Set<String> countries = new TreeSet<>();

        for (Talk talk : talks) {
            if (talk.getContentSource() != null) {
                people.add(talk.getContentSource());
            }

            countries.add(talk.getCountry());
        }

        model.addAttribute("talks", talks);
        model.addAttribute("speakers", people);
        model.addAttribute("countries", countries);
    }

    private void addNewsFeedEntryStatistics(ModelMap model) {
        model.addAttribute("numberOfNewsFeedEntries", newsFeedEntryComponent.getNumberOfNewsFeedEntries());
    }

    private void addTwitterStatistics(ModelMap model) {
        model.addAttribute("numberOfTweets", tweetComponent.getNumberOfTweets());
    }

}
