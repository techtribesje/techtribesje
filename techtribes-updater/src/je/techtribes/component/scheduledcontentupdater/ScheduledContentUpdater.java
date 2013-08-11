package je.techtribes.component.scheduledcontentupdater;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.component.AbstractComponent;
import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.badge.BadgeComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.github.GitHubComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.component.githubconnector.GitHubConnector;
import je.techtribes.component.newsfeedconnector.NewsFeedConnector;
import je.techtribes.component.twitterconnector.TwitterConnector;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class ScheduledContentUpdater extends AbstractComponent {

    private ContentSourceComponent contentSourceComponent;

    private GitHubComponent gitHubComponent;
    private TweetComponent tweetComponent;
    private NewsFeedEntryComponent newsFeedEntryComponent;
    private ActivityComponent activityComponent;
    private TalkComponent talkComponent;
    private BadgeComponent badgeComponent;
    private SearchComponent searchComponent;

    private GitHubConnector gitHubConnector;
    private NewsFeedConnector newsFeedConnector;
    private TwitterConnector twitterConnector;

    private NewsFeedUpdater newsFeedUpdater;
    private TwitterUpdater twitterUpdater;
    private GitHubUpdater gitHubUpdater;
    private BadgeAwarder badgeAwarder;

    public ScheduledContentUpdater(ContentSourceComponent contentSourceComponent, GitHubComponent gitHubComponent, TweetComponent tweetComponent, NewsFeedEntryComponent newsFeedEntryComponent, ActivityComponent activityComponent, TalkComponent talkComponent, BadgeComponent badgeComponent, SearchComponent searchComponent, GitHubConnector gitHubConnector, NewsFeedConnector newsFeedConnector, TwitterConnector twitterConnector) {
        this.contentSourceComponent = contentSourceComponent;
        this.gitHubComponent = gitHubComponent;
        this.tweetComponent = tweetComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.activityComponent = activityComponent;
        this.talkComponent = talkComponent;
        this.badgeComponent = badgeComponent;
        this.searchComponent = searchComponent;
        this.gitHubConnector = gitHubConnector;
        this.newsFeedConnector = newsFeedConnector;
        this.twitterConnector = twitterConnector;
    }

    public void start() {
        newsFeedUpdater = new NewsFeedUpdater(this, contentSourceComponent, newsFeedConnector, newsFeedEntryComponent, searchComponent);
        twitterUpdater = new TwitterUpdater(this, contentSourceComponent, tweetComponent, searchComponent, twitterConnector);
        gitHubUpdater = new GitHubUpdater(this, contentSourceComponent, gitHubComponent, gitHubConnector);
        badgeAwarder = new BadgeAwarder(this, badgeComponent);

        // this is the initial content update following startup of this component
        updateContentSourcesAndNews();

        twitterUpdater.refreshRecentTweets();
        twitterUpdater.startStreaming();
    }

    public void stop() {
        twitterUpdater.stopStreaming();
    }

    @Scheduled(cron="0 15,30,45 * * * ?")
    public void updateContentSourcesAndNews() {
        logInfo("Updating content sources, profiles and news");
        contentSourceComponent.refreshContentSources();
        twitterUpdater.refreshProfiles();
        gitHubUpdater.refreshGitHubRepositories();
        newsFeedUpdater.refreshNews();
    }

    @Scheduled(cron="0 0 * * * ?")
    public void updateAndAwardBadges() {
        logInfo("Updating activity rankings and awarding badges");
        updateContentSourcesAndNews();

        activityComponent.calculateActivityForLastSevenDays();

        badgeAwarder.awardBadgesForActivity(activityComponent.getActivityListForPeople());
        badgeAwarder.awardBadgesForActivity(activityComponent.getActivityListForBusinessTribes());
        badgeAwarder.awardBadgesForActivity(activityComponent.getActivityListForCommunityTribes());
        badgeAwarder.awardBadgesForTalks(talkComponent.getRecentTalks());
        badgeAwarder.awardBadgesForContent(newsFeedEntryComponent.getRecentNewsFeedEntries(1, 100));
        badgeAwarder.awardBadgesForTweets(tweetComponent.getRecentTweets(1, 100));
        badgeAwarder.awardBadgesForContentSource(contentSourceComponent.getPeopleAndTribes());
    }

}