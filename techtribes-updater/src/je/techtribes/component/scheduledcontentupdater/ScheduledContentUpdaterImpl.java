package je.techtribes.component.scheduledcontentupdater;

import com.structurizr.annotation.ComponentDependency;
import je.techtribes.component.activityupdater.ActivityUpdater;
import je.techtribes.component.badgeawarder.BadgeAwarder;
import je.techtribes.util.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.github.GitHubComponent;
import je.techtribes.component.githubconnector.GitHubConnector;
import je.techtribes.component.newsfeedconnector.NewsFeedConnector;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.component.twitterconnector.TwitterConnector;
import org.springframework.scheduling.annotation.Scheduled;

class ScheduledContentUpdaterImpl extends AbstractComponent implements ScheduledContentUpdater {

    @ComponentDependency(description = "Gets people and tribes from")
    private ContentSourceComponent contentSourceComponent;

    @ComponentDependency(description = "Updates the list of repositories using")
    private GitHubComponent gitHubComponent;

    @ComponentDependency(description = "Stores new tweets using")
    private TweetComponent tweetComponent;

    @ComponentDependency(description = "Stores new and updated news feed entries using")
    private NewsFeedEntryComponent newsFeedEntryComponent;

    @ComponentDependency(description = "Recalculates the people/tribe activity rankings using")
    private ActivityUpdater activityUpdater;

    @ComponentDependency(description = "Updates the search indexes for new tweets/news feed entries using")
    private SearchComponent searchComponent;

    @ComponentDependency(description = "Gets public repositories using the GitHub API")
    private GitHubConnector gitHubConnector;

    @ComponentDependency(description = "Downloads RSS/Atom feeds for blogs")
    private NewsFeedConnector newsFeedConnector;

    @ComponentDependency(description = "Refreshes profile information and gets new tweets using the REST and streaming APIs")
    private TwitterConnector twitterConnector;

    @ComponentDependency(description = "Awards badges using")
    private BadgeAwarder badgeAwarder;

    private NewsFeedUpdater newsFeedUpdater;
    private TwitterUpdater twitterUpdater;
    private GitHubUpdater gitHubUpdater;

    public ScheduledContentUpdaterImpl(ContentSourceComponent contentSourceComponent, GitHubComponent gitHubComponent, TweetComponent tweetComponent, NewsFeedEntryComponent newsFeedEntryComponent, ActivityUpdater activityUpdater, SearchComponent searchComponent, GitHubConnector gitHubConnector, NewsFeedConnector newsFeedConnector, TwitterConnector twitterConnector, BadgeAwarder badgeAwarder) {
        this.contentSourceComponent = contentSourceComponent;
        this.gitHubComponent = gitHubComponent;
        this.tweetComponent = tweetComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.activityUpdater = activityUpdater;
        this.searchComponent = searchComponent;
        this.gitHubConnector = gitHubConnector;
        this.newsFeedConnector = newsFeedConnector;
        this.twitterConnector = twitterConnector;
        this.badgeAwarder = badgeAwarder;
    }

    @Override
    public void start() {
        newsFeedUpdater = new NewsFeedUpdater(this, contentSourceComponent, newsFeedConnector, newsFeedEntryComponent, searchComponent);
        twitterUpdater = new TwitterUpdater(this, contentSourceComponent, tweetComponent, searchComponent, twitterConnector);
        gitHubUpdater = new GitHubUpdater(this, contentSourceComponent, gitHubComponent, gitHubConnector);

        // this is the initial content update following startup of this component
        logInfo("Updating content sources from database");
        contentSourceComponent.refreshContentSources();

        logInfo("Refreshing previous tweets");
        twitterUpdater.refreshRecentTweets();

        logInfo("Starting tweet streaming");
        twitterUpdater.startStreaming();
    }

    @Override
    public void stop() {
        twitterUpdater.stopStreaming();
    }

    @Scheduled(cron="0 15,30,45 * * * ?")
    void updateContentSourcesAndNews() {
        logInfo("Updating content sources from database");
        contentSourceComponent.refreshContentSources();

        logInfo("Refreshing profiles from Twitter");
        twitterUpdater.refreshProfiles();

        logInfo("Refreshing repos from GitHub");
        gitHubUpdater.refreshGitHubRepositories();

        logInfo("Refreshing news feed entries");
        newsFeedUpdater.refreshNews();
    }

    @Scheduled(cron="0 0 * * * ?")
    void updateAndAwardBadges() {
        updateContentSourcesAndNews();

        logInfo("Calculating activity rankings");
        activityUpdater.calculateActivityForLastSevenDays();

        logInfo("Awarding badges");
        badgeAwarder.awardBadgesForActivity();
        badgeAwarder.awardBadgesForTalks();
        badgeAwarder.awardBadgesForContent();
        badgeAwarder.awardBadgesForTweets();
        badgeAwarder.awardBadgesForContentSource();
    }

}