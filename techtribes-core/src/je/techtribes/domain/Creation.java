package je.techtribes.domain;

import je.techtribes.util.StringUtils;

import java.net.URL;

public class Creation extends ContentItem {

    private int id;
    private String description;
    private URL url;
    private URL codeUrl;

    public Creation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return getTitle();
    }

    public void setName(String name) {
        setTitle(name);
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

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(URL codeUrl) {
        this.codeUrl = codeUrl;
    }

    @Override
    public String getPermalink() {
        return getUrl() != null ? getUrl().toString() : "";
    }

}
