package je.techtribes.component.github;

import je.techtribes.domain.GitHubRepository;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

class GitHubRepositoryResultExtractor implements ResultSetExtractor<GitHubRepository> {

    @Override
    public GitHubRepository extractData(ResultSet rs) throws SQLException {
        int contentSourceId = rs.getInt("content_source_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String url = rs.getString("url");

        GitHubRepository gitHubRepository = new GitHubRepository(name, description, url);
        gitHubRepository.setContentSourceId(contentSourceId);

        return gitHubRepository;
    }

}
