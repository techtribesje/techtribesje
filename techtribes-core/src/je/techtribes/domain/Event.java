package je.techtribes.domain;

import je.techtribes.util.StringUtils;

import java.net.URL;
import java.util.Date;

public class Event extends ContentItem {

    private int id;
    private String description;
    private String location;
    private Island island;
    private URL url;
    private Date startDate;
    private Date endDate;

    public Event(int id) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
