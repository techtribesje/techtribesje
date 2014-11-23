package je.techtribes.domain;

import je.techtribes.util.StringUtils;

import java.util.Date;

/**
 * Represents an entry in a news feed.
 */
public class NewsFeedEntry extends ContentItem {

    private String permalink;

    public NewsFeedEntry(String permalink, String title, String body, Date timestamp, ContentSource contentSource) {
        this.permalink = permalink;
        setTitle(title);
        setBody(body);
        setTimestamp(timestamp);
        setContentSource(contentSource);
    }

    public NewsFeedEntry(String permalink, String title, String body, Date timestamp, int contentSourceId) {
        this.permalink = permalink;
        setTitle(title);
        setBody(body);
        setTimestamp(timestamp);
        setContentSourceId(contentSourceId);
    }

    public String getPermalink() {
        return permalink;
    }

    @Override
    public String getTruncatedBody() {
        return StringUtils.filterHtmlAndTruncate(getBody(), 256);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        NewsFeedEntry feedEntry = (NewsFeedEntry) o;

        return permalink.equals(feedEntry.permalink);
    }

    public int hashCode() {
        return permalink.hashCode();
    }

}