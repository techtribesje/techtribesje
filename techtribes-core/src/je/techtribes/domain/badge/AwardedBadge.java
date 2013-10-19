package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;

import java.util.Date;

public class AwardedBadge {

    private Badge badge;
    private Date date;

    private int contentSourceId;
    private ContentSource contentSource;

    private boolean awarded = true;

    public AwardedBadge(Badge badge, ContentSource contentSource) {
        this.badge = badge;
        this.contentSourceId = contentSource.getId();
        this.contentSource = contentSource;
        this.date = new Date();
    }

    public AwardedBadge(int badgeId, int contentSourceId, Date date) {
        this.badge = Badges.find(badgeId);
        this.contentSourceId = contentSourceId;
        this.date = date;
    }

    public Badge getBadge() {
        return badge;
    }

    public int getContentSourceId() {
        return contentSourceId;
    }

    public ContentSource getContentSource() {
        return contentSource;
    }

    public void setContentSource(ContentSource contentSource) {
        this.contentSource = contentSource;
    }

    public Date getDate() {
        return date;
    }

    public boolean isAwarded() {
        return awarded;
    }

    public void setAwarded(boolean awarded) {
        this.awarded = awarded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwardedBadge that = (AwardedBadge) o;

        if (contentSourceId != that.contentSourceId) return false;
        if (badge != null ? !badge.equals(that.badge) : that.badge != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = badge != null ? badge.hashCode() : 0;
        result = 31 * result + contentSourceId;
        return result;
    }

}
