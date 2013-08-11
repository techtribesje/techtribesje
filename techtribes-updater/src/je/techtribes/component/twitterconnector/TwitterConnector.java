package je.techtribes.component.twitterconnector;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.Tweet;

import java.util.List;

@Component
public interface TwitterConnector {

    public List<TwitterProfile> getTwitterProfiles();

    public List<Tweet> getRecentTweets();

    public void startStreaming(StreamingTweetListener tweetListener);

    public void stopStreaming();

}
