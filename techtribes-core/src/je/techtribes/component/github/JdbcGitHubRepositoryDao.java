package je.techtribes.component.github;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import je.techtribes.util.JdbcDatabaseConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

class JdbcGitHubRepositoryDao {

    private DataSource dataSource;

    JdbcGitHubRepositoryDao(JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.dataSource = jdbcDatabaseConfiguration.getDataSource();
    }

    List<GitHubRepository> getRepositories() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.query("select github_repo.content_source_id, github_repo.name, github_repo.description, github_repo.url, github_repo.fork from github_repo, content_source where github_repo.content_source_id = content_source.id order by name",
                new GitHubRepositoryRowMapper());
    }

    List<GitHubRepository> getRepositories(ContentSource contentSource) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.query("select content_source_id, name, description, url, fork from github_repo where content_source_id = ? order by name",
                new Object[] { contentSource.getId() },
                new GitHubRepositoryRowMapper());
    }

    void setRepositories(List<GitHubRepository> repositories, ContentSource contentSource) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("delete from github_repo where content_source_id = ?", contentSource.getId());

        for (GitHubRepository repository : repositories) {
            template.update("insert into github_repo (content_source_id, name, description, url, fork) values (?, ?, ?, ?, ?)",
                repository.getContentSource().getId(),
                repository.getName(),
                repository.getDescription(),
                repository.getUrl(),
                repository.isFork());
        }
    }

}