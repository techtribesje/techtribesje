package je.techtribes.component.newsfeedentry;

import com.mongodb.*;
import je.techtribes.component.log.LoggingComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.util.MongoDatabaseConfiguration;

import java.util.*;

class MongoDbNewsFeedEntryDao implements NewsFeedEntryDao {

    private Mongo mongo;
    private String database;

    private LoggingComponent loggingComponent;

    private static final String NEWS_FEED_ENTRY_COLLECTION = "newsFeedEntries";

    private static final String PERMALINK_KEY = "permalink";
    private static final String TITLE_KEY = "title";
    private static final String BODY_KEY = "body";
    private static final String CONTENT_SOURCE_ID_KEY = "contentSourceId";
    private static final String TIMESTAMP_KEY = "timestamp";

    MongoDbNewsFeedEntryDao(MongoDatabaseConfiguration mongoDatabaseConfiguration, LoggingComponent loggingComponent) {
        this.mongo = mongoDatabaseConfiguration.getMongo();
        this.database = mongoDatabaseConfiguration.getDatabase();
        this.loggingComponent = loggingComponent;
    }

    public void storeNewsFeedEntries(Collection<NewsFeedEntry> newsFeedEntries) {
        if (newsFeedEntries == null) {
            return;
        }

        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            if (!newsFeedEntryAlreadyExistsWithADifferentPermalink(newsFeedEntry) &&
                !newsFeedEntryAlreadyExistsWithADifferentDate(newsFeedEntry)) {

                BasicDBObject doc = new BasicDBObject();
                doc.put(PERMALINK_KEY, newsFeedEntry.getPermalink());
                doc.put(TITLE_KEY, newsFeedEntry.getTitle());
                doc.put(BODY_KEY, newsFeedEntry.getBody());
                doc.put(CONTENT_SOURCE_ID_KEY, newsFeedEntry.getContentSource().getId());
                doc.put(TIMESTAMP_KEY, newsFeedEntry.getTimestamp());

                BasicDBObject query = new BasicDBObject();
                query.put(PERMALINK_KEY, newsFeedEntry.getPermalink());

                getDBCollection().findAndModify(query, null, null, false, doc, false, true);

            }
        }
    }

    private boolean newsFeedEntryAlreadyExistsWithADifferentPermalink(NewsFeedEntry newsFeedEntry) {
        // see if an entry with the same author, title and timestamp exists
        // (this means that author has changed the permalink, perhaps by republishing or by adding a click tracker)
        BasicDBObject duplicateQuery = new BasicDBObject();
        duplicateQuery.put(CONTENT_SOURCE_ID_KEY, newsFeedEntry.getContentSource().getId());
        duplicateQuery.put(TITLE_KEY, newsFeedEntry.getTitle());
        duplicateQuery.put(TIMESTAMP_KEY, newsFeedEntry.getTimestamp());

        List<DBObject> possibleDuplicates = getDBCollection().find(duplicateQuery).toArray();
        if (possibleDuplicates.size() > 0 && !possibleDuplicates.get(0).get(PERMALINK_KEY).toString().equals(newsFeedEntry.getPermalink())) {
            loggingComponent.warn(this, "Possible duplicated detected for \"" + newsFeedEntry.getTitle() + "\" by " + newsFeedEntry.getContentSource().getName());
            loggingComponent.warn(this, " - old permalink: " + possibleDuplicates.get(0).get(PERMALINK_KEY).toString());
            loggingComponent.warn(this, " - new permalink: " + newsFeedEntry.getPermalink());

            return true;
        }

        return false;
    }

    private boolean newsFeedEntryAlreadyExistsWithADifferentDate(NewsFeedEntry newsFeedEntry) {
        // see if an entry with the same author, title and permalink exists
        // (this means the author has changed the date, probably by republishing)
        BasicDBObject duplicateQuery = new BasicDBObject();
        duplicateQuery.put(CONTENT_SOURCE_ID_KEY, newsFeedEntry.getContentSource().getId());
        duplicateQuery.put(TITLE_KEY, newsFeedEntry.getTitle());
        duplicateQuery.put(PERMALINK_KEY, newsFeedEntry.getPermalink());

        List<DBObject> possibleDuplicates = getDBCollection().find(duplicateQuery).toArray();
        if (possibleDuplicates.size() > 0 && !possibleDuplicates.get(0).get(TIMESTAMP_KEY).equals(newsFeedEntry.getTimestamp())) {
            loggingComponent.warn(this, "Possible duplicated detected for \"" + newsFeedEntry.getTitle() + "\" by " + newsFeedEntry.getContentSource().getName());
            loggingComponent.warn(this, " - old date: " + possibleDuplicates.get(0).get(TIMESTAMP_KEY).toString());
            loggingComponent.warn(this, " - new date: " + newsFeedEntry.getTimestamp());

            return true;
        }

        return false;
    }

    public List<NewsFeedEntry> getRecentNewsFeedEntries(int page, int pageSize) {
        List<DBObject> dbos = getDBCollection().find().sort(new BasicDBObject(TIMESTAMP_KEY, -1)).skip((page - 1) * pageSize).limit(pageSize).toArray();

        return toListOfNewsFeedEntry(dbos);
    }

    public List<NewsFeedEntry> getRecentNewsFeedEntries(ContentSource contentSource, int pageSize) {
        if (contentSource != null) {
            List<DBObject> dbos = getDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, contentSource.getId())).sort(new BasicDBObject(TIMESTAMP_KEY, -1)).limit(pageSize).toArray();

            return toListOfNewsFeedEntry(dbos);
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    public List<NewsFeedEntry> getRecentNewsFeedEntries(Collection<ContentSource> contentSources, int page, int pageSize) {
        List<DBObject> dbos = getDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, new BasicDBObject("$in", getIds(contentSources)))).sort(new BasicDBObject(TIMESTAMP_KEY, -1)).skip((page - 1) * pageSize).limit(pageSize).toArray();

        return toListOfNewsFeedEntry(dbos);
    }

    @Override
    public long getNumberOfNewsFeedEntries(ContentSource contentSource, Date start, Date end) {
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
    public long getNumberOfNewsFeedEntries() {
        return getDBCollection().count();
    }

    @Override
    public long getNumberOfNewsFeedEntries(Collection<ContentSource> contentSources) {
        if (contentSources != null && contentSources.size() > 0) {
            return getDBCollection().find(new BasicDBObject(CONTENT_SOURCE_ID_KEY, new BasicDBObject("$in", getIds(contentSources)))).count();
        } else {
            return 0;
        }
    }

    private List<NewsFeedEntry> toListOfNewsFeedEntry(List<DBObject> dbos) {
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();

        for (DBObject dbo : dbos) {
            NewsFeedEntry newsFeedEntry = new NewsFeedEntry(
                    (String)dbo.get(PERMALINK_KEY),
                    (String)dbo.get(TITLE_KEY),
                    (String)dbo.get(BODY_KEY),
                    (Date)dbo.get(TIMESTAMP_KEY),
                    (Integer)dbo.get(CONTENT_SOURCE_ID_KEY)
            );
            newsFeedEntries.add(newsFeedEntry);
        }

        return newsFeedEntries;
    }

    private Collection<Integer> getIds(Collection<ContentSource> contentSources) {
        Set<Integer> contentSourceIds = new HashSet<>();
        for (ContentSource contentSource : contentSources) {
            contentSourceIds.add(contentSource.getId());
        }

        return contentSourceIds;
    }

    private DBCollection getDBCollection() {
        DB db = mongo.getDB(database);
        return db.getCollection(NEWS_FEED_ENTRY_COLLECTION);
    }

}