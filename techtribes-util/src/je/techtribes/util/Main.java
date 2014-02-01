package je.techtribes.util;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.log.LoggingComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Tweet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.TimeZone;

/**
 * Standalone application for command line utilities.
 */
public class Main {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LoggingComponent loggingComponent = (LoggingComponent)applicationContext.getBean("loggingComponent");
        loggingComponent.info(new Main(), "techtribes.je utils, version " + Version.getBuildNumber() + " built on " + Version.getBuildTimestamp());

        if (args.length > 0) {
            if ("rebuildsearch".equals(args[0])) {
                ContentSourceComponent contentSourceComponent = (ContentSourceComponent)applicationContext.getBean("contentSourceComponent");
                contentSourceComponent.refreshContentSources();

                rebuildSearchIndexes(applicationContext);
            } else if ("remove".equals(args[0]) && args.length == 2) {
                removeNewsFeedEntry(args[1], applicationContext);
            }
        }

        System.exit(0);
    }

    private static void removeNewsFeedEntry(String url, ApplicationContext applicationContext) {
        LoggingComponent loggingComponent = (LoggingComponent)applicationContext.getBean("loggingComponent");
        loggingComponent.info(new Main(), "Removing news feed entry with URL " + url);

        NewsFeedEntryComponent newsFeedEntryComponent = (NewsFeedEntryComponent)applicationContext.getBean("newsFeedEntryComponent");
        SearchComponent searchComponent = (SearchComponent)applicationContext.getBean("searchComponent");

        newsFeedEntryComponent.removeNewsFeedEntry(url);
        searchComponent.removeNewsFeedEntry(url);
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
            searchComponent.addNewsFeedEntries(newsFeedEntries);
        }

        // add add everything that the tweet service knows about
        int tweetPageSize = 200;
        long numberOfTweets = tweetComponent.getNumberOfTweets();
        loggingComponent.info(Main.class, "Number of tweets: " + numberOfTweets);
        numberOfPages = PageSize.calculateNumberOfPages(numberOfTweets, tweetPageSize);
        for (int page = 1; page <= numberOfPages; page++) {
            loggingComponent.info(Main.class, "Indexing tweets; page " + page + " of " + numberOfPages);
            List<Tweet> tweets = tweetComponent.getRecentTweets(page, tweetPageSize);
            searchComponent.addTweets(tweets);
        }
        loggingComponent.info(Main.class, "Finished rebuilding search indexes");
    }

}