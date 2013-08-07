package je.techtribes.connector.github;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;

import java.util.List;

@Component
public interface GitHubConnector {

    List<GitHubRepository> getRepositories(ContentSource contentSource);

}
