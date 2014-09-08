package je.techtribes.domain;

import je.techtribes.component.log.LoggingComponentFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Superclass for a person or tribe.
 */
public abstract class ContentSource implements Comparable<ContentSource> {

    private int id;
    private ContentSourceType type = ContentSourceType.Person;
    private String name = "";
    private Island island = Island.None;
    private String profile = "";
    private URL profileImageUrl;
    private URL url;
    private String twitterId;
    private String gitHubId;
    private int twitterFollowersCount;
    private boolean contentAggregated = true;
    private boolean active = false;
    private boolean signedIn = false;

    private String searchTerms;

    private Set<NewsFeed> newsFeeds = new HashSet<>();

    public ContentSource(ContentSourceType type) {
        this(type, 0);
    }

    public ContentSource(ContentSourceType type, int id) {
        this.type = type;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isTribe() {
        return this.type != ContentSourceType.Person;
    }

    public boolean isPerson() {
        return this.type == ContentSourceType.Person;
    }

    public ContentSourceType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return name.replace("&", "").replace(" ", "").replace(".", "").replace("'", "").toLowerCase();
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }
    }

    public Island getIsland() {
        return island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    public String getTwitterId() {
        return this.twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public boolean hasTwitterId() {
        return this.twitterId != null && !twitterId.isEmpty();
    }

    public String getGitHubId() {
        return gitHubId;
    }

    public void setGitHubId(String gitHubId) {
        this.gitHubId = gitHubId;
    }

    public boolean hasGitHubId() {
        return this.gitHubId != null && !gitHubId.isEmpty();
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public URL getProfileImageUrl() {
        if (this.profileImageUrl != null) {
            return profileImageUrl;
        } else {
            try {
                return new URL("https://techtribes.je/static/img/default-profile-image.png");
            } catch (MalformedURLException e) {
                LoggingComponentFactory.create().warn(this, "Could not set profile image URL");
                return null;
            }
        }
    }

    public void setProfileImageUrl(URL profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getTwitterFollowersCount() {
        return twitterFollowersCount;
    }

    public void setTwitterFollowersCount(int twitterFollowersCount) {
        this.twitterFollowersCount = twitterFollowersCount;
    }

    public int compareTo(ContentSource cs) {
        return name.compareTo(cs.name);
    }

    public void addNewsFeed(String url) {
        if (url != null) {
            try {
                NewsFeed newsFeed = new NewsFeed(url, this);
                newsFeeds.add(newsFeed);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public Set<NewsFeed> getNewsFeeds() {
        return new HashSet<>(newsFeeds);
    }

    public boolean isContentAggregated() {
        return contentAggregated;
    }

    /**
     * Sets whether content (i.e. tweets and blog entries) for this content source
     * are aggregated into the site. This is set to false for tribes that need
     * to be registered in order list jobs, but we don't want to see their
     * content (e.g. recruitment agents, the States of Jersey, etc).
     */
    public void setContentAggregated(boolean contentAggregated) {
        this.contentAggregated = contentAggregated;
    }

    public boolean hasSignedInBefore() {
        return signedIn;
    }

    public void setSignedInBefore(boolean signedIn) {
        this.signedIn = signedIn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentSource that = (ContentSource)o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}