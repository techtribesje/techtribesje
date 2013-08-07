package je.techtribes.component.github;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GitHubComponentTests extends AbstractComponentTestsBase {

    @Before
    public void setUp() {
        JdbcTemplate template = getJdbcTemplate();
        for (int i = 1; i <= 5; i++) {
            template.update("insert into github_repo (content_source_id, name, description, url) values (?, ?, ?, ?)",
                    1,
                    "Repo " + i,
                    "Here is a description for repo " + i,
                    "https://github.com/repo" + i);
        }
        for (int i = 6; i <= 10; i++) {
            template.update("insert into github_repo (content_source_id, name, description, url) values (?, ?, ?, ?)",
                    2,
                    "Repo " + i,
                    "Here is a description for repo " + i,
                    "https://github.com/repo" + i);
        }
    }

    @After
    public void tearDown() {
        JdbcTemplate template = getJdbcTemplate();
        template.execute("delete from github_repo");
    }

    @Test
    public void test_getRepositories_ReturnsAllRepositories() {
        List<GitHubRepository> repos = getGitHubComponent().getRepositories();
        assertEquals(10, repos.size());
    }

    @Test
    public void test_getRepositoriesForContentSource_ReturnsAllRepositories_WhenThereAreSome() {
        List<GitHubRepository> repos = getGitHubComponent().getRepositories(getContentSourceComponent().findById(1));
        assertEquals(5, repos.size());

        int i = 1;
        for (GitHubRepository repo : repos) {
            assertEquals("Repo " + i, repo.getName());
            i++;
        }
    }

    @Test
    public void test_getRepositoriesForContentSource_ReturnsAnEmptyList_WhenThereAreNone() {
        List<GitHubRepository> repos = getGitHubComponent().getRepositories(getContentSourceComponent().findById(4));
        assertEquals(0, repos.size());
    }

    @Test
    public void test_GitHubRepositoryDetailsAreCorrectlyRetrievedFromTheDatabase() {
        List<GitHubRepository> repos = getGitHubComponent().getRepositories();
        GitHubRepository repo = repos.get(0);

        assertEquals("Repo 1", repo.getName());
        assertEquals("Here is a description for repo 1", repo.getDescription());
        assertEquals("Simon Brown", repo.getContentSource().getName());
        assertEquals("https://github.com/repo1", repo.getUrl());
    }

    @Test
    public void test_setRepositories_RemovesAllExistingRepos_WhenThereAreNone() {
        ContentSource contentSource = getContentSourceComponent().findById(1);
        List<GitHubRepository> repos = getGitHubComponent().getRepositories(contentSource);
        assertEquals(5, repos.size());

        getGitHubComponent().setRepositories(new LinkedList<GitHubRepository>(), contentSource);

        repos = getGitHubComponent().getRepositories(contentSource);
        assertEquals(0, repos.size());
    }

    @Test
    public void test_setRepositories_ReplacesExistingRepos_WhenThereAreSome() {
        ContentSource contentSource = getContentSourceComponent().findById(1);
        List<GitHubRepository> repos = getGitHubComponent().getRepositories(contentSource);
        assertEquals(5, repos.size());

        List<GitHubRepository> newRepos = new LinkedList<>();
        newRepos.add(new GitHubRepository("New repo", "Here is a description of the new repo", "https://github.com/newrepo"));
        newRepos.get(0).setContentSource(contentSource);

        getGitHubComponent().setRepositories(newRepos, contentSource);

        repos = getGitHubComponent().getRepositories(contentSource);
        assertEquals(1, repos.size());
        assertEquals("New repo", repos.get(0).getName());
    }

}
