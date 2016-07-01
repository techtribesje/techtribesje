package je.techtribes.component.twitterconnector;

import com.structurizr.annotation.UsesSoftwareSystem;
import je.techtribes.domain.Tweet;
import je.techtribes.util.AbstractComponent;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@UsesSoftwareSystem(name = "Twitter", description= "Gets tweets and profile information from")
class Twitter4jTwitterConnector extends AbstractComponent implements TwitterConnector {

    private static final int LOOKUP_USERS_PAGE_SIZE = 100;
    private static final int HOME_TIMELINE_PAGE_SIZE = 100;

    private String twitterId;
    private String oAuthConsumerKey;
    private String oAuthConsumerSecret;
    private String oAuthAccessToken;
    private String oAuthAccessTokenSecret;
    private String debug;
    private String[] filters;

    private TwitterStream userStream;
    private TwitterStream filteredStream;

    Twitter4jTwitterConnector(String twitterId, String oAuthConsumerKey, String oAuthConsumerSecret, String oAuthAccessToken, String oAuthAccessTokenSecret, String debug, String filters) {
        this.twitterId = twitterId;
        this.oAuthConsumerKey = oAuthConsumerKey;
        this.oAuthConsumerSecret = oAuthConsumerSecret;
        this.oAuthAccessToken = oAuthAccessToken;
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
        this.debug = debug;
        this.filters = filters.split(",");
    }

    private Twitter getTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled("true".equals(debug))
            .setOAuthConsumerKey(oAuthConsumerKey)
            .setOAuthConsumerSecret(oAuthConsumerSecret)
            .setOAuthAccessToken(oAuthAccessToken)
            .setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        return new TwitterFactory(cb.build()).getInstance();
    }

    private TwitterStream getUserStream() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled("true".equals(debug))
                .setOAuthConsumerKey(oAuthConsumerKey)
                .setOAuthConsumerSecret(oAuthConsumerSecret)
                .setOAuthAccessToken(oAuthAccessToken)
                .setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        return new TwitterStreamFactory(cb.build()).getInstance();
    }

    private TwitterStream getFilteredStream() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled("true".equals(debug))
                .setOAuthConsumerKey(oAuthConsumerKey)
                .setOAuthConsumerSecret(oAuthConsumerSecret)
                .setOAuthAccessToken(oAuthAccessToken)
                .setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        return new TwitterStreamFactory(cb.build()).getInstance();
    }

    /**
     * Gets the profiles of all users being followed by the configured Twitter account.
     *
     * @return      a List of TwitterProfile objects, one for every Twitter ID being followed
     */
    public List<TwitterProfile> getTwitterProfiles() throws je.techtribes.component.twitterconnector.TwitterException {
        List<TwitterProfile> profiles = new LinkedList<>();

        try {
            Twitter twitter = getTwitter();

            // find the list of Twitter IDs that I'm following
            IDs followingIds = twitter.getFriendsIDs(-1);

            // and break this list up into pages of 100 (this is the maximum number of Twitter IDs for which
            // profile information can be retrieved in one go)
            int pages = (int)Math.ceil(followingIds.getIDs().length / (double)LOOKUP_USERS_PAGE_SIZE);

            int startIndex;
            int endIndex;

            for (int page = 1; page <= pages; page++) {

                startIndex = (LOOKUP_USERS_PAGE_SIZE * (page-1));
                endIndex = (LOOKUP_USERS_PAGE_SIZE * page)-1;

                if (endIndex > (followingIds.getIDs().length-1)) {
                    endIndex = followingIds.getIDs().length-1;
                }

                long[] ids = new long[(endIndex-startIndex)+1];
                int j = 0;
                for (int i = startIndex; i <= endIndex; i++) {
                    ids[j] = followingIds.getIDs()[i];
                    j++;
                }

                ResponseList<User> users = twitter.lookupUsers(ids);
                for (User user : users) {
                    profiles.add(toTwitterProfile(user));
                }
            }

            // and enrich "my" profile
            ResponseList<User> users = twitter.lookupUsers(new String[] { twitterId });
            profiles.add(toTwitterProfile(users.get(0)));
        } catch (Exception e) {
            je.techtribes.component.twitterconnector.TwitterException te = new je.techtribes.component.twitterconnector.TwitterException("Could not get profile information from Twitter", e);
            logError(te);
            throw te;
        }

        return profiles;
    }

    public List<Tweet> getRecentTweets() throws je.techtribes.component.twitterconnector.TwitterException {
        List<Tweet> tweets = new LinkedList<>();

        try {
            Twitter twitter = getTwitter();
            List<Status> statuses = twitter.getHomeTimeline(new Paging(1, HOME_TIMELINE_PAGE_SIZE));

            for (Status status : statuses) {
                Tweet tweet = toTweet(status);
                tweets.add(tweet);
            }
        } catch (Exception e) {
            je.techtribes.component.twitterconnector.TwitterException te = new je.techtribes.component.twitterconnector.TwitterException("Could not get home timeline from Twitter", e);
            logError(te);
            throw te;
        }

        return tweets;
    }

    public List<Tweet> getRecentHashtaggedTweets() throws je.techtribes.component.twitterconnector.TwitterException {
        List<Tweet> tweets = new LinkedList<>();

        try {
            Twitter twitter = getTwitter();
            StringBuilder queryString = new StringBuilder();
            for (int i = 0; i < filters.length; i ++) {
                String encodedFilter = filters[i].replace("#", "%23");
                queryString.append(encodedFilter);
                if (i < (filters.length -1)) {
                    queryString.append(" OR ");
                }
            }

            Query query = new Query(queryString.toString());
            query.setResultType(Query.MIXED);
            query.setCount(100);
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                Tweet tweet = toTweet(status);
                tweets.add(tweet);
            }
        } catch (Exception e) {
            je.techtribes.component.twitterconnector.TwitterException te = new je.techtribes.component.twitterconnector.TwitterException("Could not get home timeline from Twitter", e);
            logError(te);
            throw te;
        }

        return tweets;
    }

    public void startUserStream(final StreamingTweetListener tweetListener) {
        userStream = getUserStream();
        UserStreamListener listener = new UserStreamListener() {
            @Override
            public void onDeletionNotice(long l, long l1) {
            }

            @Override
            public void onFriendList(long[] longs) {
            }

            @Override
            public void onFavorite(User user, User user1, Status status) {
            }

            @Override
            public void onUnfavorite(User user, User user1, Status status) {
            }

            @Override
            public void onFollow(User user, User user1) {
            }

            @Override
            public void onDirectMessage(DirectMessage directMessage) {
            }

            @Override
            public void onUserListMemberAddition(User user, User user1, UserList userList) {
            }

            @Override
            public void onUserListMemberDeletion(User user, User user1, UserList userList) {
            }

            @Override
            public void onUserListSubscription(User user, User user1, UserList userList) {
            }

            @Override
            public void onUserListUnsubscription(User user, User user1, UserList userList) {
            }

            @Override
            public void onUserListCreation(User user, UserList userList) {
            }

            @Override
            public void onUserListUpdate(User user, UserList userList) {
            }

            @Override
            public void onUserListDeletion(User user, UserList userList) {
            }

            @Override
            public void onUserProfileUpdate(User user) {
            }

            @Override
            public void onBlock(User user, User user1) {
            }

            @Override
            public void onUnblock(User user, User user1) {
            }

            @Override
            public void onStatus(Status status) {
                logInfo(toTweet(status).toString());
                tweetListener.onTweet(toTweet(status));
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                tweetListener.onDelete(statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int i) {
            }

            @Override
            public void onScrubGeo(long l, long l1) {
            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {
                logWarn(stallWarning.toString());
            }

            @Override
            public void onException(Exception e) {
                je.techtribes.component.twitterconnector.TwitterException te = new je.techtribes.component.twitterconnector.TwitterException("Received exception from Twitter4j", e);
                logError(te);
            }
        };

        userStream.addListener(listener);
        userStream.user();
    }

    public void stopUserStream() {
        userStream.shutdown();
    }

    public void startFilteredStream(final StreamingTweetListener tweetListener) {
        filteredStream = getFilteredStream();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                logInfo(toTweet(status).toString());
                tweetListener.onTweet(toTweet(status));
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                tweetListener.onDelete(statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int i) {

            }

            @Override
            public void onScrubGeo(long l, long l2) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            @Override
            public void onException(Exception e) {

            }
        };

        filteredStream.addListener(listener);

        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(filters);
        filteredStream.filter(filterQuery);
    }

    public void stopFilteredStream() {
        filteredStream.shutdown();
    }

    private Tweet toTweet(Status status) {
        return new Tweet(status.getUser().getScreenName(), status.getId(), status.getText(), status.getCreatedAt(), status.getUser().getProfileImageURL());
    }

    private TwitterProfile toTwitterProfile(User user) {
        URL imageURL = null;
        URL url = null;

        try {
            imageURL = user.getProfileImageURL() != null ? new URL(user.getProfileImageURL()) : null;
        } catch (MalformedURLException e) {
            je.techtribes.component.twitterconnector.TwitterException te = new je.techtribes.component.twitterconnector.TwitterException("Error with image URL", e);
            logWarn(te);
        }
        try {
            url = user.getURLEntity() != null && user.getURLEntity().getExpandedURL() != null && user.getURLEntity().getExpandedURL().length() > 0 ? new URL(user.getURLEntity().getExpandedURL()) : null;
        } catch (MalformedURLException e) {
            je.techtribes.component.twitterconnector.TwitterException te = new je.techtribes.component.twitterconnector.TwitterException("Error with profile URL", e);
            logWarn(te);
        }

        return new TwitterProfile(
            user.getScreenName(),
            user.getDescription(),
            imageURL,
            url,
            user.getFollowersCount());
    }

}