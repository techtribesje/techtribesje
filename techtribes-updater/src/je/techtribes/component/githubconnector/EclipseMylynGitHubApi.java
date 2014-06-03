package je.techtribes.component.githubconnector;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.util.LinkedList;
import java.util.List;

public class EclipseMylynGitHubApi extends GitHubApi {

    private String oAuth2;

    protected EclipseMylynGitHubApi(String oAuth2) {
        this.oAuth2 = oAuth2;
    }

    public List<GitHubRepository> getRepositories(ContentSource contentSource) throws Exception {
        RepositoryService service = new RepositoryService();
        service.getClient().setOAuth2Token(oAuth2);

        List<GitHubRepository> repos = new LinkedList<>();

        for (Repository repo : service.getRepositories(contentSource.getGitHubId())) {
            if (!repo.isFork() && !repo.isPrivate()) {
                GitHubRepository gitHubRepository = new GitHubRepository(repo.getName(), repo.getDescription(), repo.getHtmlUrl(), contentSource);
                repos.add(gitHubRepository);
            }
        }

        return repos;
    }

}
