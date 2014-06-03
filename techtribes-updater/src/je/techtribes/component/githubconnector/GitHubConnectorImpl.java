package je.techtribes.component.githubconnector;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import je.techtribes.util.AbstractComponent;

import java.util.List;

class GitHubConnectorImpl extends AbstractComponent implements GitHubConnector {

    private GitHubApi gitHubApi;

    GitHubConnectorImpl(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    @Override
    public List<GitHubRepository> getRepositories(ContentSource contentSource) {
        try {
            return gitHubApi.getRepositories(contentSource);
        } catch (Exception ioe) {
            GitHubConnectorException ghe = new GitHubConnectorException("Couldn't get list of GitHub repos for " + contentSource.getGitHubId(), ioe);
            logError(ghe);
            throw ghe;
        }
    }

}
