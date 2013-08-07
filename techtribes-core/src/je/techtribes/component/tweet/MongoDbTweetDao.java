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

class MongoDbTweetDao implements TweetDao {

    private Mongo mongo;
    private String database;

    private static final String TWITTER_COLLECTION_NAME = "tweets";

    private static final String TWEET_ID_KEY = "id";
    private static final String BODY_KEY = "body";
    private static final String CONTENT_SOURCE_ID_KEY = "contentSourceId";
    private static final String TWITTER_ID_KEY = "twitterId";
    private static final String TIMESTAMP_KEY = "timestamp";

    MongoDbTweetDao(MongoDatabaseConfiguration mongoDatabaseConfiguration) {
        this.mongo = mongoDatabaseConfiguration.getMongo();
        this.database = mongoDatabaseConfiguration.getDatabase();
    }

    @Override
    public List<Tweet> getRecentTweets(int page, int pageSize) {
        List<DBObject> dbObjects = getDBCollection().find().sort(new BasicDBObject(TWEET_ID_KEY, -1)).skip((page - 1) * pageSize).limit(pageSize).toArray();
        return toTweets(dbObjects);
    }

    @Override
    public List<Tweet> getRecentTweets(ContentSource contentSource, int pageSize) {
        if (contentSource != null) {
            List<DBObject> dbObjects = getDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, contentSource.getId())).sort(new BasicDBObject(TWEET_ID_KEY, -1)).limit(pageSize).toArray();
            return toTweets(dbObjects);
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    public List<Tweet> getRecentTweets(Collection<ContentSource> contentSources, int page, int pageSize) {
        if (contentSources != null && contentSources.size() > 0) {
            Collection<Integer> contentSourceIds = new ContentSourceToIdConverter().getIds(contentSources);
            List<DBObject> dbObjects = getDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, new BasicDBObject("$in", contentSourceIds))).sort(new BasicDBObject(TWEET_ID_KEY, -1)).skip((page - 1) * pageSize).limit(pageSize).toArray();
            return toTweets(dbObjects);
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    public long getNumberOfTweets() {
        return getDBCollection().count();
    }

    @Override
    public long getNumberOfTweets(ContentSource contentSource, Date start, Date end) {
        if (contentSource != null) {
            BasicDBObject query = new BasicDBObject();
            query.put(CONTENT_SOURCE_ID_KEY, contentSource.getId());
            query.put(TIMESTAMP_KEY, BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get());
            return getDBCollection().find(query).count();
        } else {
            return 0;
        }
    }

    @Override
    public long getNumberOfTweets(Collection<ContentSource> contentSources) {
        if (contentSources != null && contentSources.size() > 0) {
            Collection<Integer> contentSourceIds = new ContentSourceToIdConverter().getIds(contentSources);
            return getDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, new BasicDBObject("$in", contentSourceIds))).count();
        } else {
            return 0;
        }
    }

    @Override
    public void removeTweet(long tweetId) {
        getDBCollection().findAndRemove(new BasicDBObject(TWEET_ID_KEY, tweetId));
    }

    @Override
    public void storeTweets(Collection<Tweet> tweets) {
        if (tweets != null && tweets.size() > 0) {
            DBCollection coll = getDBCollection();

            for (Tweet tweet : tweets) {
                BasicDBObject doc = toBasicDBObject(tweet);
                BasicDBObject query = new BasicDBObject();
                query.put(TWEET_ID_KEY, tweet.getId());

                coll.findAndModify(query, null, null, false, doc, false, true);
            }
        }
    }

    private BasicDBObject toBasicDBObject(Tweet tweet) {
        BasicDBObject doc = new BasicDBObject();
        doc.put(TWEET_ID_KEY, tweet.getId());
        doc.put(CONTENT_SOURCE_ID_KEY, tweet.getContentSource().getId());
        doc.put(TWITTER_ID_KEY, tweet.getTwitterId());
        doc.put(BODY_KEY, tweet.getBody());
        doc.put(TIMESTAMP_KEY, tweet.getTimestamp());
        return doc;
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

    private DBCollection getDBCollection() {
        DB db = mongo.getDB(database);
        return db.getCollection(TWITTER_COLLECTION_NAME);
    }

}
