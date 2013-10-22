package je.techtribes.component.activity;

import je.techtribes.component.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.event.EventComponent;
import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.domain.Tweet;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.domain.*;
import je.techtribes.util.DateUtils;
import je.techtribes.util.JdbcDatabaseConfiguration;
import je.techtribes.util.comparator.ActivityByScoreComparator;

import java.util.*;

class ActivityComponentImpl extends AbstractComponent implements ActivityComponent {

    private ContentSourceComponent contentSourceComponent;
    private NewsFeedEntryComponent newsFeedEntryComponent;
    private TweetComponent tweetComponent;
    private TalkComponent talkComponent;
    private EventComponent eventComponent;

    private JdbcActivityDao activityDao;

    private Map<ContentSource, Activity> activitiesByContentSource = new HashMap<>();
    private List<Activity> activityListForPeople = new LinkedList<>();
    private List<Activity> activityListForBusinessTribes = new LinkedList<>();
    private List<Activity> activityListForCommunityTribes = new LinkedList<>();

    ActivityComponentImpl(ContentSourceComponent contentSourceComponent, NewsFeedEntryComponent newsFeedEntryComponent, TweetComponent tweetComponent, TalkComponent talkComponent, EventComponent eventComponent, JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.contentSourceComponent = contentSourceComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.tweetComponent = tweetComponent;
        this.talkComponent = talkComponent;
        this.eventComponent = eventComponent;
        this.activityDao = new JdbcActivityDao(jdbcDatabaseConfiguration);
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
            activityDao.storeActivity(activityCollection);
            refreshRecentActivity();
        } catch (Exception e) {
            ActivityException ae = new ActivityException("Error storing recent activity", e);
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

    public List<Activity> getActivityListForPeople() {
        return activityListForPeople;
    }

    public List<Activity> getActivityListForBusinessTribes() {
        return activityListForBusinessTribes;
    }

    public List<Activity> getActivityListForCommunityTribes() {
        return activityListForCommunityTribes;
    }

    @Override
    public Activity getActivity(ContentSource contentSource) {
        if (activitiesByContentSource.containsKey(contentSource)) {
            return activitiesByContentSource.get(contentSource);
        } else {
            return null;
        }
    }

    @Override
    public synchronized void refreshRecentActivity() {
        try {
            List<Activity> activityListForPeople = new LinkedList<>();
            List<Activity> activityListForBusinessTribes = new LinkedList<>();
            List<Activity> activityListForMediaTribes = new LinkedList<>();
            List<Activity> activityListForCommunityTribes = new LinkedList<>();

            Collection<Activity> activityCollection = activityDao.getRecentActivity();
            filterAndEnrich(activityCollection);

            Map<ContentSource,Activity> activityMap = toMap(activityCollection);

            // mark content sources as active or not
            for (Activity activity : activityCollection) {
                if (activity.getScore() > 0) {
                    activity.getContentSource().setActive(true);
                }

                switch (activity.getContentSource().getType()) {
                    case Person:
                        activityListForPeople.add(activity);
                        break;
                    case Business:
                        activityListForBusinessTribes.add(activity);
                        break;
                    case Media:
                        activityListForMediaTribes.add(activity);
                        break;
                    case Community:
                        activityListForCommunityTribes.add(activity);
                        break;
                }
            }

            Collections.sort(activityListForPeople, new ActivityByScoreComparator());
            Collections.sort(activityListForBusinessTribes, new ActivityByScoreComparator());
            Collections.sort(activityListForCommunityTribes, new ActivityByScoreComparator());
            Collections.sort(activityListForMediaTribes, new ActivityByScoreComparator());

            this.activitiesByContentSource = activityMap;
            this.activityListForPeople = activityListForPeople;
            this.activityListForBusinessTribes = activityListForBusinessTribes;
            this.activityListForCommunityTribes = activityListForCommunityTribes;
        } catch (Exception e) {
            ActivityException ae = new ActivityException("Error refreshing recent activity", e);
            logError(ae);
            throw ae;
        }
    }

    private void filterAndEnrich(Collection<Activity> activityCollection) {
        Iterator<Activity> it = activityCollection.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            ContentSource contentSource = contentSourceComponent.findById(activity.getContentSourceId());
            if (contentSource != null && contentSource.isContentAggregated()) {
                activity.setContentSource(contentSource);
            } else {
                LoggingComponentFactory.create().debug(this, "Filtering activity associated with content source ID " + activity.getContentSourceId());
                it.remove();
            }
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