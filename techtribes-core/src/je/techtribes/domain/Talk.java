package je.techtribes.domain;

import je.techtribes.util.StringUtils;

import java.net.URL;
import java.util.Date;

public class Talk extends ContentItem {

    private int id;
    private String description;
    private TalkType type;
    private String eventName;
    private String city;
    private String country;
    private URL url;
    private Date date;
    private URL slidesUrl;
    private URL videoUrl;

    public Talk(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTruncatedDescription() {
        return StringUtils.filterHtmlAndTruncate(getBody(), 512);
    }

    public TalkType getType() {
        return type;
    }

    public void setType(TalkType type) {
        this.type = type;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public URL getSlidesUrl() {
        return slidesUrl;
    }

    public void setSlidesUrl(URL slidesUrl) {
        this.slidesUrl = slidesUrl;
    }

    public URL getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(URL videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String getBody() {
        return getDescription();
    }

    @Override
    public String getPermalink() {
        return "/talks/" + getId();
    }

    public boolean isLocal() {
        return country.equalsIgnoreCase("Jersey") || country.equalsIgnoreCase("Guernsey");
    }

    public boolean isInPast() {
        return this.date.before(new Date());
    }

}
