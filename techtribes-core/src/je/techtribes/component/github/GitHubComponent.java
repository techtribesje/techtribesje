package je.techtribes.component.github;

import com.structurizr.element.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;

import java.util.List;

@Component(responsibility = "Provides access to the list of GitHub repos associated with content sources.")
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
