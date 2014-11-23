package je.techtribes.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a tweet.
 */
public class Tweet extends ContentItem {

    private String twitterId;
    private long id;
    private String profileImageUrl;

    public Tweet(String twitterId, long id, String body, Date timestamp, String profileImageUrl) {
        this.twitterId = twitterId;
        this.id = id;
        setBody(body);
        setTimestamp(timestamp);
        this.profileImageUrl = profileImageUrl;
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
        if (getContentSource() != null) {
            return "http://twitter.com/" + getContentSource().getTwitterId() + "/status/" + getId();
        } else {
            return "http://twitter.com/" + getTwitterId() + "/status/" + getId();
        }
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Set<String> getHashtags() {
        return getTokens("#");
    }

    public Set<String> getUsers() {
        return getTokens("@");
    }

    private Set<String> getTokens(String prefix) {
        Set<String> tokenSet = new HashSet<>();

        if (getBody() != null) {
            String tokens[] = getBody().split("\\s");
            StringBuilder twitterHashtags = new StringBuilder();
            for (String token : tokens) {
                if (token.startsWith(prefix)) {
                    tokenSet.add(token.toLowerCase());
                }
            }
        }

        return tokenSet;
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
