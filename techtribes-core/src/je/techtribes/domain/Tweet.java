package je.techtribes.domain;

import java.util.Date;

/**
 * Represents a tweet.
 */
public class Tweet extends ContentItem {

    private String twitterId;
    private long id;

    public Tweet(String twitterId, long id, String body, Date timestamp) {
        this.twitterId = twitterId;
        this.id = id;
        setBody(body);
        setTimestamp(timestamp);
    }

    public Tweet(int contentSourceId, long id, String body, Date timestamp) {
        setContentSourceId(contentSourceId);
        this.id = id;
        setBody(body);
        setTimestamp(timestamp);
    }

    public Tweet(ContentSource contentSource, long id, String body, Date timestamp) {
        setContentSource(contentSource);
        this.id = id;
        setBody(body);
        setTimestamp(timestamp);
    }

    public String getTwitterId() {
        return twitterId;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public String getTruncatedBody() {
        return getBody();
    }

    @Override
    public String getPermalink() {
        return "http://twitter.com/" + getTwitterId() + "/status/" + getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet tweet = (Tweet) o;

        return (id == tweet.id);
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "[@" + getTwitterId() + "] " + getBody();
    }
}
