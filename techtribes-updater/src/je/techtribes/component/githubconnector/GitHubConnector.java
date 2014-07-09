package je.techtribes.component.githubconnector;

import com.structurizr.annotation.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;

import java.util.List;

@Component(description = "Gets information about repositories from GitHub.")
public interface GitHubConnector {

    List<GitHubRepository> getRepositories(ContentSource contentSource);

}
