package je.techtribes.component.tweet;

import com.structurizr.annotation.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Tweet;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component(description = "Provides access to tweets.")
public interface TweetComponent {

    /**
     * Gets the most recent tweets by page number.
     */
    List<Tweet> getRecentTweets(int page, int pageSize);

    /**
     * Gets the most recent tweets for a given person or tribe.
     */
    List<Tweet> getRecentTweets(ContentSource contentSource, int pageSize);

    /**
     * Gets the most recent tweets for a given collection of people and/or tribes.
     */
    List<Tweet> getRecentTweets(Collection<ContentSource> contentSources, int page, int pageSize);

    /**
     * Calculates how many tweets a given person or tribe has created between a date period.
     */
    long getNumberOfTweets(ContentSource contentSource, Date start, Date end);

    /**
     * Gets the number of tweets.
     */
    long getNumberOfTweets();

    /**
     * Gets the number of tweets for a given collection of people and/or tribes.
     */
    long getNumberOfTweets(Collection<ContentSource> contentSources);

    /**
     * Removes a tweet with the given ID.
     */
    void removeTweet(long tweetId);

    /**
     * Stores a collection of tweets.
     */
    void storeTweets(Collection<Tweet> tweets);

    /**
     * Gets the most recent hashtagged tweets by page number.
     */
    List<Tweet> getRecentHashtaggedTweets(String hashtag, int page, int pageSize);

    /**
     * Stores a collection of tweets.
     */
    void storeHashtaggedTweets(Collection<Tweet> tweets);

    /**
     * Removes a tweet with the given ID.
     */
    void removeHashtaggedTweet(long tweetId);

}
