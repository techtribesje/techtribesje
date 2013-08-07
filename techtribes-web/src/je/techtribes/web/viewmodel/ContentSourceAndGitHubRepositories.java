package je.techtribes.web.viewmodel;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import je.techtribes.util.comparator.GitHubRepositoryByNameComparator;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class ContentSourceAndGitHubRepositories {

    private ContentSource contentSource;
    private Set<GitHubRepository> gitHubRepositories;

    public ContentSourceAndGitHubRepositories(ContentSource contentSource) {
        this.contentSource = contentSource;
        this.gitHubRepositories = new TreeSet<>(new GitHubRepositoryByNameComparator());
    }

    public ContentSource getContentSource() {
        return contentSource;
    }

    public void add(GitHubRepository repo) {
        this.gitHubRepositories.add(repo);
    }

    public Collection<GitHubRepository> getGitHubRepositories() {
        return gitHubRepositories;
    }

}
