package je.techtribes.component.github;

import je.techtribes.domain.GitHubRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class GitHubRepositoryRowMapper implements RowMapper<GitHubRepository> {

    @Override
    public GitHubRepository mapRow(ResultSet rs, int line) throws SQLException {
        GitHubRepositoryResultExtractor extractor = new GitHubRepositoryResultExtractor();
        return extractor.extractData(rs);
    }

}