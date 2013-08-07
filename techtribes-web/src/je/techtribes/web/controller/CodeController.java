package je.techtribes.web.controller;

import je.techtribes.component.github.GitHubComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import je.techtribes.util.comparator.ContentSourceByNameComparator;
import je.techtribes.web.viewmodel.ContentSourceAndGitHubRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class CodeController extends AbstractController {

    private GitHubComponent gitHubComponent;

    @Autowired
    public CodeController(GitHubComponent gitHubComponent) {
        this.gitHubComponent = gitHubComponent;
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET)
	public String viewRecentNews(ModelMap model) {
        List<GitHubRepository> repos = gitHubComponent.getRepositories();

        Map<ContentSource, ContentSourceAndGitHubRepositories> reposByCoder = new TreeMap<>(new ContentSourceByNameComparator());
        for (GitHubRepository repo : repos) {
            ContentSourceAndGitHubRepositories contentSourceAndGitHubRepositories = new ContentSourceAndGitHubRepositories(repo.getContentSource());

            if (!reposByCoder.containsKey(repo.getContentSource())) {
                reposByCoder.put(repo.getContentSource(), contentSourceAndGitHubRepositories);
            } else {
                contentSourceAndGitHubRepositories = reposByCoder.get(repo.getContentSource());
            }
            contentSourceAndGitHubRepositories.add(repo);
        }

        model.addAttribute("reposByCoder", reposByCoder.values());
        addCommonAttributes(model);
        setPageTitle(model, "Code");

        return "code";
	}

}
