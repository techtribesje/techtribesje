package je.techtribes.domain;

import je.techtribes.domain.ContentItem;
import je.techtribes.domain.Island;
import je.techtribes.util.StringUtils;

import java.net.URL;
import java.util.Date;

public class Job extends ContentItem {

    public static final int JOB_LIFESPAN_IN_DAYS = 28;

    private int id;
    private String description;
    private Island island;
    private URL url;
    private Date datePosted;

    public Job(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getBody() {
        return getDescription();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTruncatedDescription() {
        return StringUtils.filterHtmlAndTruncate(getDescription());
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Island getIsland() {
        return island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String getPermalink() {
        return getUrl() != null ? getUrl().toString() : "";
    }

}
