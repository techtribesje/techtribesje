package je.techtribes.connector.twitter;

import java.net.URL;

/**
 * Profile information for a Twitter user.
 */
public class TwitterProfile {

    private final String twitterId;
    private String description;
    private URL imageUrl;
    private URL url;
    private int followersCount;

    public TwitterProfile(String twitterId, String description, URL imageUrl, URL url, int followersCount) {
        this.twitterId = twitterId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.url = url;
        this.followersCount = followersCount;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public String getDescription() {
        return description;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public URL getUrl() {
        return url;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwitterProfile that = (TwitterProfile) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        if (twitterId != null ? !twitterId.equals(that.twitterId) : that.twitterId != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = twitterId != null ? twitterId.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

}