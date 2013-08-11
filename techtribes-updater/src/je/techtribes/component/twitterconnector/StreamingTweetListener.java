package je.techtribes.component.twitterconnector;

import je.techtribes.domain.Tweet;

public interface StreamingTweetListener {

    public void onTweet(Tweet tweet);

    public void onDelete(long tweetId);

}
