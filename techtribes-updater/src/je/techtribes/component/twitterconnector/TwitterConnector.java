package je.techtribes.component.twitterconnector;

import com.structurizr.element.Component;
import com.structurizr.element.Integration;
import com.structurizr.element.IntegrationType;
import je.techtribes.domain.Tweet;

import java.util.List;

@Component(responsibility = "Pulls profile information and tweets from Twitter.")
@Integration(type = IntegrationType.External, target = "Twitter", responsibility = "Gets tweets and profile information from")
public interface TwitterConnector {

    public List<TwitterProfile> getTwitterProfiles();

    public List<Tweet> getRecentTweets();

    public void startStreaming(StreamingTweetListener tweetListener);

    public void stopStreaming();

}
