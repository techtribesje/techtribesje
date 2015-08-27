package je.techtribes.component.twitterconnector;

import com.structurizr.annotation.Component;
import je.techtribes.domain.Tweet;

import java.util.List;

/**
 * Pulls profile information and tweets from Twitter.
 */
@Component
public interface TwitterConnector {

    public List<TwitterProfile> getTwitterProfiles();

    public List<Tweet> getRecentTweets();

    public List<Tweet> getRecentHashtaggedTweets();

    public void startUserStream(StreamingTweetListener tweetListener);

    public void stopUserStream();

    public void startFilteredStream(StreamingTweetListener tweetListener);

    public void stopFilteredStream();

}
