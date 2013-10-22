package je.techtribes.domain;

import java.util.Date;

public class Activity {

    public static final int LOCAL_TALK_SCORE = 50;
    public static final int INTERNATIONAL_TALK_SCORE = 100;
    public static final int CONTENT_SCORE = 25;
    public static final int TWEET_SCORE = 1;
    public static final int EVENT_SCORE = 100;

    public static final int MAXIMUM_TWEET_SCORE = 25;

    private int contentSourceId;
    private ContentSource contentSource;

    private long numberOfLocalTalks;
    private long numberOfInternationalTalks;
    private long numberOfNewsFeedEntries;
    private long numberOfTweets;
    private long numberOfEvents;

    private Date lastActivityDate = new Date(0);

    public Activity(int contentSourceId, long numberOfInternationalTalks, long numberOfLocalTalks, long numberOfNewsFeedEntries, long numberOfTweets, long numberOfEvents, Date lastActivityDate) {
        this.contentSourceId = contentSourceId;
        this.numberOfLocalTalks = numberOfLocalTalks;
        this.numberOfInternationalTalks = numberOfInternationalTalks;
        this.numberOfNewsFeedEntries = numberOfNewsFeedEntries;
        this.numberOfTweets = numberOfTweets;
        this.numberOfEvents = numberOfEvents;
        this.lastActivityDate = lastActivityDate;
    }

    public Activity(ContentSource contentSource, long numberOfInternationalTalks, long numberOfLocalTalks, long numberOfNewsFeedEntries, long numberOfTweets, long numberOfEvents) {
        this.contentSource = contentSource;
        this.numberOfLocalTalks = numberOfLocalTalks;
        this.numberOfInternationalTalks = numberOfInternationalTalks;
        this.numberOfNewsFeedEntries = numberOfNewsFeedEntries;
        this.numberOfTweets = numberOfTweets;
        this.numberOfEvents = numberOfEvents;
    }

    public int getContentSourceId() {
        return contentSourceId;
    }

    public void setContentSource(ContentSource contentSource) {
        this.contentSource = contentSource;
    }

    public ContentSource getContentSource() {
        return contentSource;
    }

    public long getNumberOfLocalTalks() {
        return numberOfLocalTalks;
    }

    public long getNumberOfInternationalTalks() {
        return numberOfInternationalTalks;
    }

    public long getNumberOfNewsFeedEntries() {
        return numberOfNewsFeedEntries;
    }

    public long getNumberOfTweets() {
        return numberOfTweets;
    }

    public long getNumberOfEvents() {
        return numberOfEvents;
    }

    public long getScore() {
        return getInternationalTalkScore() + getLocalTalkScore() + getNewsFeedEntryScore() + getTwitterScore() + getEventScore();
    }

    public long getLocalTalkScore() {
        if (contentSource.isPerson()) {
            return this.numberOfLocalTalks * LOCAL_TALK_SCORE;
        } else {
            return 0;
        }
    }

    public long getInternationalTalkScore() {
        if (contentSource.isPerson()) {
            return this.numberOfInternationalTalks * INTERNATIONAL_TALK_SCORE;
        } else {
            return 0;
        }
    }

    public long getNewsFeedEntryScore() {
        return this.numberOfNewsFeedEntries * CONTENT_SCORE;
    }

    public long getTwitterScore() {
        return Math.min(MAXIMUM_TWEET_SCORE, this.numberOfTweets * TWEET_SCORE);
    }

    public long getEventScore() {
        if (contentSource.isTribe() && contentSource.getType() == ContentSourceType.Community) {
            return this.numberOfEvents * EVENT_SCORE;
        } else {
            return 0;
        }
    }

    public Date getLastActivityDate() {
        return this.lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

}
