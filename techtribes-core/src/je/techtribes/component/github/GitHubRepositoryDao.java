package je.techtribes.component.github;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;

import java.util.List;

interface GitHubRepositoryDao {

    List<GitHubRepository> getRepositories();

    List<GitHubRepository> getRepositories(ContentSource contentSource);

    void setRepositories(List<GitHubRepository> repositories, ContentSource contentSource);

}
