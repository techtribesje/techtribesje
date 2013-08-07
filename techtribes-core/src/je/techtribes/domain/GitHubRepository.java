package je.techtribes.domain;

public class GitHubRepository {

    private int contentSourceId;
    private ContentSource contentSource;

    private String name;
    private String description;
    private String url;

    public GitHubRepository(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public GitHubRepository(String name, String description, String url, ContentSource contentSource) {
        this(name, description, url);
        this.contentSource = contentSource;
    }

    public int getContentSourceId() {
        return contentSourceId;
    }

    public void setContentSourceId(int contentSourceId) {
        this.contentSourceId = contentSourceId;
    }

    public ContentSource getContentSource() {
        return contentSource;
    }

    public void setContentSource(ContentSource contentSource) {
        this.contentSource = contentSource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GitHubRepository that = (GitHubRepository) o;

        if (contentSource != null ? !contentSource.equals(that.contentSource) : that.contentSource != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = contentSource != null ? contentSource.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
