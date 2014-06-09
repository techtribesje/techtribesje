package je.techtribes.component.githubconnector;

import com.structurizr.element.Component;
import com.structurizr.element.Integration;
import com.structurizr.element.IntegrationType;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;

import java.util.List;

@Component(responsibility = "Gets information about repositories from GitHub.")
@Integration(type = IntegrationType.External, target = "GitHub", responsibility = "Gets the list of public repositories from")
public interface GitHubConnector {

    List<GitHubRepository> getRepositories(ContentSource contentSource);

}
