package je.techtribes.component.tweet;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Tweet;

import java.util.Collection;
import java.util.Date;
import java.util.List;

interface TweetDao {

    List<Tweet> getRecentTweets(int page, int pageSize);

    List<Tweet> getRecentTweets(ContentSource contentSource, int pageSize);

    List<Tweet> getRecentTweets(Collection<ContentSource> contentSources, int page, int pageSize);

    long getNumberOfTweets(ContentSource contentSource, Date start, Date end);

    long getNumberOfTweets();

    long getNumberOfTweets(Collection<ContentSource> contentSources);

    void removeTweet(long tweetId);

    void storeTweets(Collection<Tweet> tweets);

}
