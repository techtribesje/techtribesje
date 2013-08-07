package je.techtribes.component.github;

import je.techtribes.component.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import je.techtribes.util.JdbcDatabaseConfiguration;

import java.util.List;

class GitHubComponentImpl extends AbstractComponent implements GitHubComponent {

    private ContentSourceComponent contentSourceComponent;
    private GitHubRepositoryDao gitHubRepositoryDao;

    GitHubComponentImpl(ContentSourceComponent contentSourceComponent, JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.contentSourceComponent = contentSourceComponent;
        this.gitHubRepositoryDao = new JdbcGitHubRepositoryDao(jdbcDatabaseConfiguration);
    }

    @Override
    public List<GitHubRepository> getRepositories() {
        try {
            List<GitHubRepository> repos = gitHubRepositoryDao.getRepositories();
            enrichRepos(repos);

            return repos;
        } catch (Exception e) {
            GitHubException ghe = new GitHubException("Error getting repos", e);
            logError(ghe);
            throw ghe;
        }
    }

    @Override
    public List<GitHubRepository> getRepositories(ContentSource contentSource) {
        try {
            List<GitHubRepository> repos = gitHubRepositoryDao.getRepositories(contentSource);
            enrichRepos(repos);

            return repos;
        } catch (Exception e) {
            GitHubException ghe = new GitHubException("Error getting repos for content source with ID " + contentSource.getId(), e);
            logError(ghe);
            throw ghe;
        }
    }

    @Override
    public void setRepositories(List<GitHubRepository> repositories, ContentSource contentSource) {
        try {
            gitHubRepositoryDao.setRepositories(repositories, contentSource);
        } catch (Exception e) {
            GitHubException ghe = new GitHubException("Error setting repos for content source with ID " + contentSource.getId(), e);
            logError(ghe);
            throw ghe;
        }
    }

    private void enrichRepos(List<GitHubRepository> repos) {
        for (GitHubRepository repo : repos) {
            if (repo.getContentSourceId() > 0) {
                repo.setContentSource(contentSourceComponent.findById(repo.getContentSourceId()));
            }
        }
    }

}
