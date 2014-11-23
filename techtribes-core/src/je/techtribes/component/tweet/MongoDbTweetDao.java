package je.techtribes.component.tweet;

import com.mongodb.*;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Tweet;
import je.techtribes.util.ContentSourceToIdConverter;
import je.techtribes.util.MongoDatabaseConfiguration;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

class MongoDbTweetDao {

    private Mongo mongo;
    private String database;

    private static final String TWEETS_COLLECTION_NAME = "tweets";
    private static final String HASHTAGGED_TWEETS_COLLECTION_NAME = "hashtaggedTweets";

    private static final String TWEET_ID_KEY = "id";
    private static final String BODY_KEY = "body";
    private static final String CONTENT_SOURCE_ID_KEY = "contentSourceId";
    private static final String TWITTER_ID_KEY = "twitterId";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String USER_PROFILE_IMAGE_URL_KEY = "profileImageUrl";
    private static final String HASHTAGS_KEY = "hashtags";

    MongoDbTweetDao(MongoDatabaseConfiguration mongoDatabaseConfiguration) {
        this.mongo = mongoDatabaseConfiguration.getMongo();
        this.database = mongoDatabaseConfiguration.getDatabase();
    }

    List<Tweet> getRecentTweets(int page, int pageSize) {
        List<DBObject> dbObjects = getTweetsDBCollection().find().sort(new BasicDBObject(TWEET_ID_KEY, -1)).skip((page - 1) * pageSize).limit(pageSize).toArray();
        return toTweets(dbObjects);
    }

    List<Tweet> getRecentTweets(ContentSource contentSource, int pageSize) {
        if (contentSource != null) {
            List<DBObject> dbObjects = getTweetsDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, contentSource.getId())).sort(new BasicDBObject(TWEET_ID_KEY, -1)).limit(pageSize).toArray();
            return toTweets(dbObjects);
        } else {
            return new LinkedList<>();
        }
    }

    List<Tweet> getRecentTweets(Collection<ContentSource> contentSources, int page, int pageSize) {
        if (contentSources != null && contentSources.size() > 0) {
            Collection<Integer> contentSourceIds = new ContentSourceToIdConverter().getIds(contentSources);
            List<DBObject> dbObjects = getTweetsDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, new BasicDBObject("$in", contentSourceIds))).sort(new BasicDBObject(TWEET_ID_KEY, -1)).skip((page - 1) * pageSize).limit(pageSize).toArray();
            return toTweets(dbObjects);
        } else {
            return new LinkedList<>();
        }
    }

    long getNumberOfTweets() {
        return getTweetsDBCollection().count();
    }

    long getNumberOfTweets(ContentSource contentSource, Date start, Date end) {
        if (contentSource != null) {
            BasicDBObject query = new BasicDBObject();
            query.put(CONTENT_SOURCE_ID_KEY, contentSource.getId());
            query.put(TIMESTAMP_KEY, BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get());
            return getTweetsDBCollection().find(query).count();
        } else {
            return 0;
        }
    }

    long getNumberOfTweets(Collection<ContentSource> contentSources) {
        if (contentSources != null && contentSources.size() > 0) {
            Collection<Integer> contentSourceIds = new ContentSourceToIdConverter().getIds(contentSources);
            return getTweetsDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, new BasicDBObject("$in", contentSourceIds))).count();
        } else {
            return 0;
        }
    }

    void removeTweet(long tweetId) {
        getTweetsDBCollection().findAndRemove(new BasicDBObject(TWEET_ID_KEY, tweetId));
    }

    void storeTweets(Collection<Tweet> tweets) {
        if (tweets != null && tweets.size() > 0) {
            DBCollection coll = getTweetsDBCollection();

            for (Tweet tweet : tweets) {
                BasicDBObject doc = new BasicDBObject();
                doc.put(TWEET_ID_KEY, tweet.getId());
                doc.put(CONTENT_SOURCE_ID_KEY, tweet.getContentSource().getId());
                doc.put(TWITTER_ID_KEY, tweet.getTwitterId());
                doc.put(BODY_KEY, tweet.getBody());
                doc.put(TIMESTAMP_KEY, tweet.getTimestamp());

                BasicDBObject query = new BasicDBObject();
                query.put(TWEET_ID_KEY, tweet.getId());

                coll.findAndModify(query, null, null, false, doc, false, true);
            }
        }
    }

    List<Tweet> getRecentHashtaggedTweets(String hashtag, int page, int pageSize) {
        List<DBObject> dbObjects = getHashtaggedTweetsDBCollection().find(new BasicDBObject(HASHTAGS_KEY, Pattern.compile(hashtag + " "))).sort(new BasicDBObject(TWEET_ID_KEY, -1)).skip((page - 1) * pageSize).limit(pageSize).toArray();
        return toHashtaggedTweets(dbObjects);
    }

    void removeHashtaggedTweet(long tweetId) {
        getHashtaggedTweetsDBCollection().findAndRemove(new BasicDBObject(TWEET_ID_KEY, tweetId));
    }

    void storeHashtaggedTweets(Collection<Tweet> tweets) {
        if (tweets != null && tweets.size() > 0) {
            DBCollection coll = getHashtaggedTweetsDBCollection();

            for (Tweet tweet : tweets) {
                BasicDBObject doc = new BasicDBObject();
                doc.put(TWEET_ID_KEY, tweet.getId());
                doc.put(TWITTER_ID_KEY, tweet.getTwitterId());
                doc.put(USER_PROFILE_IMAGE_URL_KEY, tweet.getProfileImageUrl());

                StringBuilder hashtags = new StringBuilder();
                for (String hashtag : tweet.getHashtags()) {
                    hashtags.append(hashtag);
                    hashtags.append(" ");
                }

                doc.put(HASHTAGS_KEY, hashtags.toString());
                doc.put(BODY_KEY, tweet.getBody());
                doc.put(TIMESTAMP_KEY, tweet.getTimestamp());

                BasicDBObject query = new BasicDBObject();
                query.put(TWEET_ID_KEY, tweet.getId());

                coll.findAndModify(query, null, null, false, doc, false, true);
            }
        }
    }

    private List<Tweet> toTweets(List<DBObject> dbObjects) {
        List<Tweet> tweets = new LinkedList<>();

        for (DBObject dbObject : dbObjects) {
            tweets.add(toTweet(dbObject));
        }

        return tweets;
    }

    private Tweet toTweet(DBObject dbo) {
        return new Tweet(
                (Integer)dbo.get(CONTENT_SOURCE_ID_KEY),
                (Long)dbo.get(TWEET_ID_KEY),
                dbo.get(BODY_KEY).toString(),
                (Date)dbo.get(TIMESTAMP_KEY)
        );
    }

    private List<Tweet> toHashtaggedTweets(List<DBObject> dbObjects) {
        List<Tweet> tweets = new LinkedList<>();

        for (DBObject dbObject : dbObjects) {
            tweets.add(toHashtaggedTweets(dbObject));
        }

        return tweets;
    }

    private Tweet toHashtaggedTweets(DBObject dbo) {
        return new Tweet(
                dbo.get(TWITTER_ID_KEY).toString(),
                (Long)dbo.get(TWEET_ID_KEY),
                dbo.get(BODY_KEY).toString(),
                (Date)dbo.get(TIMESTAMP_KEY),
                dbo.get(USER_PROFILE_IMAGE_URL_KEY).toString()
        );
    }

    private DBCollection getTweetsDBCollection() {
        DB db = mongo.getDB(database);
        return db.getCollection(TWEETS_COLLECTION_NAME);
    }

    private DBCollection getHashtaggedTweetsDBCollection() {
        DB db = mongo.getDB(database);
        return db.getCollection(HASHTAGGED_TWEETS_COLLECTION_NAME);
    }

}