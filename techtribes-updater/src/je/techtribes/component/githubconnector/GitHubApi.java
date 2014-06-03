package je.techtribes.component.githubconnector;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;

import java.util.List;

abstract class GitHubApi {

    abstract List<GitHubRepository> getRepositories(ContentSource contentSource) throws Exception;

}
