package je.techtribes.component.github;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;

import java.util.List;

/**
 * Provides access to the list of GitHub repos associated with content sources.
 */
@Component
public interface GitHubComponent {

    /**
     * Gets the list of all GitHub repos.
     */
    List<GitHubRepository> getRepositories();

    /**
     * Gets the list of all GitHub repos for a given ContentSource.
     */
    List<GitHubRepository> getRepositories(ContentSource contentSource);

    /**
     * Sets the list of GitHub repos for a given ContentSource.
     */
    void setRepositories(List<GitHubRepository> repositories, ContentSource contentSource);

}
