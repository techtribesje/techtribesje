package je.techtribes.updater;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.log.LoggingComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.scheduledcontentupdater.ScheduledContentUpdater;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Tweet;
import je.techtribes.util.DateUtils;
import je.techtribes.util.PageSize;
import je.techtribes.util.Version;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.TimeZone;

/**
 * Standalone application that updates tweets, news, badges, etc.
 */
public class Main {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LoggingComponent loggingComponent = (LoggingComponent)applicationContext.getBean("loggingComponent");
        loggingComponent.info(Main.class, "techtribes.je updater, version " + Version.getBuildNumber() + " built on " + Version.getBuildTimestamp());

        if (args.length > 0 && "rebuildsearch".equals(args[0])) {
            ContentSourceComponent contentSourceComponent = (ContentSourceComponent)applicationContext.getBean("contentSourceComponent");
            contentSourceComponent.refreshContentSources();

            rebuildSearchIndexes(applicationContext);
            System.exit(0);
        } else if (args.length > 0 && "migrate".equals(args[0])) {
            NewsFeedEntryComponent newsFeedEntryComponent = (NewsFeedEntryComponent)applicationContext.getBean("newsFeedEntryComponent");
            TweetComponent tweetComponent = (TweetComponent)applicationContext.getBean("tweetComponent");
            ContentSourceComponent contentSourceComponent = (ContentSourceComponent)applicationContext.getBean("contentSourceComponent");
            contentSourceComponent.refreshContentSources();

            long numberOfNewsFeedEntries = newsFeedEntryComponent.getNumberOfNewsFeedEntries();
            loggingComponent.info(Main.class, "Number of NFEs: " + numberOfNewsFeedEntries);
            int numberOfPages = PageSize.calculateNumberOfPages(numberOfNewsFeedEntries, PageSize.RECENT_NEWS_FEED_ENTRIES);
            for (int page = 1; page <= numberOfPages; page++) {
                loggingComponent.info(Main.class, "Migrating NFEs; page " + page + " of " + numberOfPages);
                List<NewsFeedEntry> nfes = newsFeedEntryComponent.getRecentNewsFeedEntries(page, PageSize.RECENT_NEWS_FEED_ENTRIES);
                newsFeedEntryComponent.storeNewsFeedEntries(nfes);
            }

            long numberOfTweets = tweetComponent.getNumberOfTweets();
            loggingComponent.info(Main.class, "Number of tweets: " + numberOfTweets);
            numberOfPages = PageSize.calculateNumberOfPages(numberOfTweets, PageSize.RECENT_TWEETS);
            for (int page = 1; page <= numberOfPages; page++) {
                loggingComponent.info(Main.class, "Migrating tweets; page " + page + " of " + numberOfPages);
                List<Tweet> tweets = tweetComponent.getRecentTweets(page, PageSize.RECENT_TWEETS);
                tweetComponent.storeTweets(tweets);
            }

            System.exit(0);
        }

        ScheduledContentUpdater scu = (ScheduledContentUpdater)applicationContext.getBean("contentUpdater");
        scu.start();
    }

    private static void rebuildSearchIndexes(ApplicationContext applicationContext) {
        LoggingComponent loggingComponent = (LoggingComponent)applicationContext.getBean("loggingComponent");
        loggingComponent.info(Main.class, "Starting to rebuild search indexes");
        SearchComponent searchComponent = (SearchComponent)applicationContext.getBean("searchComponent");
        NewsFeedEntryComponent newsFeedEntryComponent = (NewsFeedEntryComponent)applicationContext.getBean("newsFeedEntryComponent");
        TweetComponent tweetComponent = (TweetComponent)applicationContext.getBean("tweetComponent");

        searchComponent.clearSearchIndex();

        // add add everything that the news feed service knows about
        long numberOfNewsFeedEntries = newsFeedEntryComponent.getNumberOfNewsFeedEntries();
        loggingComponent.info(Main.class, "Number of news feed entries: " + numberOfNewsFeedEntries);
        int numberOfPages = PageSize.calculateNumberOfPages(numberOfNewsFeedEntries, PageSize.RECENT_NEWS_FEED_ENTRIES);
        for (int page = 1; page <= numberOfPages; page++) {
            loggingComponent.info(Main.class, "Indexing news feed entries; page " + page + " of " + numberOfPages);
            List<NewsFeedEntry> newsFeedEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(page, PageSize.RECENT_NEWS_FEED_ENTRIES);
            for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
                searchComponent.add(newsFeedEntry);
            }
        }

        // add add everything that the tweet service knows about
        long numberOfTweets = tweetComponent.getNumberOfTweets();
        loggingComponent.info(Main.class, "Number of tweets: " + numberOfTweets);
        numberOfPages = PageSize.calculateNumberOfPages(numberOfTweets, PageSize.RECENT_TWEETS);
        for (int page = 1; page <= numberOfPages; page++) {
            loggingComponent.info(Main.class, "Indexing tweets; page " + page + " of " + numberOfPages);
            List<Tweet> tweets = tweetComponent.getRecentTweets(page, PageSize.RECENT_TWEETS);
            for (Tweet tweet : tweets) {
                searchComponent.add(tweet);
            }
        }
        loggingComponent.info(Main.class, "Finished rebuilding search indexes");
    }

}
