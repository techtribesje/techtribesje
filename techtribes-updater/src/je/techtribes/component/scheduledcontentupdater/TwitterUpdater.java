package je.techtribes.component.scheduledcontentupdater;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.component.tweet.TweetException;
import je.techtribes.component.twitterconnector.StreamingTweetListener;
import je.techtribes.component.twitterconnector.TwitterConnector;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.Tribe;
import je.techtribes.domain.Tweet;
import je.techtribes.component.twitterconnector.TwitterProfile;

import java.util.LinkedList;
import java.util.List;

class TwitterUpdater {

    private final ScheduledContentUpdaterImpl scheduledContentUpdater;

    private final ContentSourceComponent contentSourceComponent;
    private final TweetComponent tweetComponent;
    private final SearchComponent searchComponent;

    private TwitterConnector twitterConnector;

    TwitterUpdater(ScheduledContentUpdaterImpl scheduledContentUpdater, ContentSourceComponent contentSourceComponent, TweetComponent tweetComponent, SearchComponent searchComponent, TwitterConnector twitterConnector) {
        this.scheduledContentUpdater = scheduledContentUpdater;
        this.contentSourceComponent = contentSourceComponent;
        this.tweetComponent = tweetComponent;
        this.searchComponent = searchComponent;
        this.twitterConnector = twitterConnector;
    }

    void refreshProfiles() {
        try {
            List<TwitterProfile> profiles = twitterConnector.getTwitterProfiles();
            for (TwitterProfile profile : profiles) {
                ContentSource contentSource = contentSourceComponent.findByTwitterId(profile.getTwitterId());
                if (contentSource != null) {
                    contentSource.setProfile(profile.getDescription());
                    contentSource.setProfileImageUrl(profile.getImageUrl());
                    contentSource.setUrl(profile.getUrl());
                    contentSource.setTwitterFollowersCount(profile.getFollowersCount());

                    contentSourceComponent.update(contentSource);
                }
            }
        } catch (Exception e) {
            ScheduledContentUpdaterException scue = new ScheduledContentUpdaterException("Error updating people and profiles", e);
            scheduledContentUpdater.logError(scue);
        }
    }

    public void refreshRecentTweets() {
        try {
            List<Tweet> tweets = twitterConnector.getRecentTweets();
            List<Tweet> filteredTweets = filterTweets(tweets);
            tweetComponent.storeTweets(filteredTweets);
            searchComponent.addTweets(filteredTweets);
        } catch (TweetException e) {
            ScheduledContentUpdaterException scue = new ScheduledContentUpdaterException("Error refreshing tweets", e);
            scheduledContentUpdater.logError(scue);
        }
    }

    public void startStreaming() {
        try {
            twitterConnector.startUserStream(new StreamingTweetListener() {
                @Override
                public void onTweet(final Tweet tweet) {
                    List<Tweet> tweets = new LinkedList<>();
                    tweets.add(tweet);
                    List<Tweet> filteredTweets = filterTweets(tweets);
                    tweetComponent.storeTweets(filteredTweets);
                    searchComponent.addTweets(filteredTweets);
                }

                @Override
                public void onDelete(long tweetId) {
                    tweetComponent.removeTweet(tweetId);
                    searchComponent.deleteTweet(tweetId);
                }
            });
        } catch (Exception e) {
            ScheduledContentUpdaterException scue = new ScheduledContentUpdaterException("Error starting streaming", e);
            scheduledContentUpdater.logError(scue);
        }
    }

    public void stopStreaming() {
        try {
            twitterConnector.stopUserStream();
            twitterConnector.stopFilteredStream();
        } catch (Exception e) {
            ScheduledContentUpdaterException scue = new ScheduledContentUpdaterException("Error stopping streaming", e);
            scheduledContentUpdater.logError(scue);
        }
    }

    private List<Tweet> filterTweets(List<Tweet> tweets) {
        List<Tweet> filteredTweets = new LinkedList<>();

        for (Tweet tweet : tweets) {
            ContentSource contentSource = contentSourceComponent.findByTwitterId(tweet.getTwitterId());
            if (contentSource != null && contentSource.isContentAggregated()) {
                tweet.setContentSource(contentSource);

                if (contentSource.getType() == ContentSourceType.Media) {
                    if (contentSource.getShortName().equals("digitalquadrantmagazine")) {
                        filteredTweets.add(tweet);
                    } else {
                        // this tries to ensure that only digital/IT/technology tweets are aggregated
                        String body = tweet.getBody().toLowerCase();
                        for (String keyword : Tribe.MEDIA_TRIBE_KEYWORD_TRIGGERS) {
                            if (body.contains(keyword) && !filteredTweets.contains(tweet)) {
                                filteredTweets.add(tweet);
                            }
                        }
                    }
                } else {
                    filteredTweets.add(tweet);
                }
            }
        }

        return filteredTweets;
    }

}
