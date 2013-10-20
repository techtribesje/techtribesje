package je.techtribes.component.githubconnector;

import je.techtribes.component.AbstractComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

class EclipseMylynGitHubConnector extends AbstractComponent implements GitHubConnector {

    private String oAuth2;

    EclipseMylynGitHubConnector(String oAuth2) {
        this.oAuth2 = oAuth2;
    }

    @Override
    public List<GitHubRepository> getRepositories(ContentSource contentSource) {
        try {
            List<GitHubRepository> repos = new LinkedList<>();

            RepositoryService service = new RepositoryService();
            service.getClient().setOAuth2Token(oAuth2);

            for (Repository repo : service.getRepositories(contentSource.getGitHubId())) {
                if (!repo.isFork() && !repo.isPrivate()) {
                    GitHubRepository gitHubRepository = new GitHubRepository(repo.getName(), repo.getDescription(), repo.getHtmlUrl(), contentSource);
                    repos.add(gitHubRepository);
                }
            }

            return repos;
        } catch (IOException ioe) {
            GitHubConnectorException ghe = new GitHubConnectorException("Couldn't get list of GitHub repos for " + contentSource.getGitHubId(), ioe);
            logError(ghe);
            throw ghe;
        }
    }

}
