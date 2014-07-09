package je.techtribes.component.activityupdater;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.event.EventComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.domain.*;
import je.techtribes.util.AbstractComponent;
import je.techtribes.util.DateUtils;
import je.techtribes.util.JdbcDatabaseConfiguration;

import java.util.*;

class ActivityUpdaterImpl extends AbstractComponent implements ActivityUpdater {

    private ContentSourceComponent contentSourceComponent;
    private NewsFeedEntryComponent newsFeedEntryComponent;
    private TweetComponent tweetComponent;
    private TalkComponent talkComponent;
    private EventComponent eventComponent;

    private JdbcActivityUpdaterDao activityUpdaterDao;

    ActivityUpdaterImpl(ContentSourceComponent contentSourceComponent, NewsFeedEntryComponent newsFeedEntryComponent, TweetComponent tweetComponent, TalkComponent talkComponent, EventComponent eventComponent, JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.contentSourceComponent = contentSourceComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.tweetComponent = tweetComponent;
        this.talkComponent = talkComponent;
        this.eventComponent = eventComponent;
        this.activityUpdaterDao = new JdbcActivityUpdaterDao(jdbcDatabaseConfiguration);
    }

    @Override
    public synchronized void calculateActivityForLastSevenDays() {
        Collection<Activity> activityCollection = new LinkedList<>();

        Date start = DateUtils.getXDaysAgo(7);
        Date end = DateUtils.getEndOfToday();

        List<ContentSource> contentSources = contentSourceComponent.getPeopleAndTribes();
        for (ContentSource contentSource : contentSources) {
            long localTalkCount = talkComponent.getNumberOfLocalTalks(contentSource.getId(), start, end);
            long internationalTalkCount = talkComponent.getNumberOfInternationalTalks(contentSource.getId(), start, end);
            long newsFeedEntryCount = newsFeedEntryComponent.getNumberOfNewsFeedEntries(contentSource, start, end);
            long tweetCount = tweetComponent.getNumberOfTweets(contentSource, start, end);
            long eventCount = eventComponent.getNumberOfEvents(contentSource, start, end);

            Activity activity = new Activity(contentSource, internationalTalkCount, localTalkCount, newsFeedEntryCount, tweetCount, eventCount);
            activityCollection.add(activity);
        }

        // now find the most recent activity for all content sources (tweets and content)
        Map<ContentSource,Activity> activityMap = toMap(activityCollection);
        Map<ContentSource, Date> activityDates = new HashMap<>();
        List<Tweet> tweets = tweetComponent.getRecentTweets(1, 1000);
        calculateMostRecentActivityDate(activityDates, tweets);
        List<NewsFeedEntry> newsFeedEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(1, 1000);
        calculateMostRecentActivityDate(activityDates, newsFeedEntries);
        for (ContentSource contentSource : activityDates.keySet()) {
            if (activityMap.containsKey(contentSource)) {
                Activity activity = activityMap.get(contentSource);
                activity.setLastActivityDate(activityDates.get(contentSource));
            }
        }

        try {
            activityUpdaterDao.storeActivity(activityCollection);
        } catch (Exception e) {
            ActivityUpdaterException ae = new ActivityUpdaterException("Error storing recent activity", e);
            logError(ae);
            throw ae;
        }
    }

    private void calculateMostRecentActivityDate(Map<ContentSource, Date> activityDates, List<? extends ContentItem> contentItems) {
        for (ContentItem contentItem : contentItems) {
            Date date = new Date(0);
            if (activityDates.containsKey(contentItem.getContentSource())) {
                date = activityDates.get(contentItem.getContentSource());
            }

            if (contentItem.getTimestamp().after(date)) {
                date = contentItem.getTimestamp();
            }

            activityDates.put(contentItem.getContentSource(), date);
        }
    }

    private Map<ContentSource,Activity> toMap(Collection<Activity> activityCollection) {
        Map<ContentSource, Activity> activityMapByContentSource = new HashMap<>();

        for (Activity activity : activityCollection) {
            activityMapByContentSource.put(activity.getContentSource(), activity);
        }

        return activityMapByContentSource;
    }

}