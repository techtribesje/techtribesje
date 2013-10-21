package je.techtribes.domain;

import je.techtribes.util.StringUtils;

import java.util.Date;

/**
 * Represents the basic contract for a piece of content (i.e. blog entry, tweet, search result, etc).
 */
public abstract class ContentItem {

    private String title;
    private String body;

    private int contentSourceId;
    private ContentSource contentSource;
    private Date timestamp;

    public int getContentSourceId() {
        return contentSourceId;
    }

    public void setContentSourceId(int contentSourceId) {
        this.contentSourceId = contentSourceId;
    }

    public ContentSource getContentSource() {
        return this.contentSource;
    }

    public void setContentSource(ContentSource contentSource) {
        this.contentSource = contentSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public String getBodyAsHtml() {
        if (getBody() == null) {
            return null;
        }

        String s = getBody().replaceAll("http://\\S*", "<a href=\"$0\" target=\"_blank\">$0</a>");
        s = s.replaceAll("https://\\S*", "<a href=\"$0\" target=\"_blank\">$0</a>");
        s = s.replaceAll("(#)([a-zA-Z0-9_]+)", "<a href=\"/search?q=%23$2\">$0</a>");
        s = s.replaceAll("(@)([a-zA-Z0-9_]+)", "<a href=\"/twitter/$2\">$0</a>");

        return s;
    }

    public String getTruncatedBody() {
        return StringUtils.filterHtmlAndTruncate(getBody());
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public abstract String getPermalink();

}