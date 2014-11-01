package je.techtribes.domain;

import je.techtribes.util.StringUtils;

import java.net.URL;

public class Book extends ContentItem {

    public enum Role {
        Author("Author"),
        Coauthor("Co-author"),
        Contributor("Contributor");

        private String description;

        Role(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private int id;
    private String description;
    private String publisher;
    private String publishedDate;
    private URL url;

    private Role role = Role.Author;

    public Book(int id) {
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getRole() {
        return role.toString();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getPermalink() {
        return getUrl() != null ? getUrl().toString() : "";
    }

    public boolean isPublished() {
        return this.publishedDate != null;
    }

}
