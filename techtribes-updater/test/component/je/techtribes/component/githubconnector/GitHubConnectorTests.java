package je.techtribes.component.githubconnector;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import je.techtribes.domain.Person;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GitHubConnectorTests {

    protected static ApplicationContext applicationContext;

    @Before
    public void setUp() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void test_getRepositories_ReturnsAListOfRepositories_WhenThereAreSome() {
        GitHubConnector gitHubConnector = (GitHubConnector)applicationContext.getBean("gitHubConnector");
        ContentSource contentSource = new Person();
        contentSource.setGitHubId("techtribesje");
        List<GitHubRepository> repos = gitHubConnector.getRepositories(contentSource);
        Collections.sort(repos, new Comparator<GitHubRepository>() {
            @Override
            public int compare(GitHubRepository r1, GitHubRepository r2) {
                return r1.getName().compareTo(r2.getName());
            }
        });
        assertEquals(4, repos.size());

        int index = 0;
        assertEquals("introduction-to-coding", repos.get(index).getName());

        index = 1;
        assertEquals("software-guidebook", repos.get(index).getName());

        index = 2;
        assertEquals("techtribesje", repos.get(index).getName());
        assertEquals("Source code for the techtribes.je website", repos.get(index).getDescription());
        assertEquals("https://github.com/techtribesje/techtribesje", repos.get(index).getUrl());
        assertEquals(contentSource, repos.get(index).getContentSource());

        index = 3;
        assertEquals("techtribesje-bin", repos.get(index).getName());
        assertEquals("Scripts for the techtribes.je live environment", repos.get(index).getDescription());
        assertEquals("https://github.com/techtribesje/techtribesje-bin", repos.get(index).getUrl());
        assertEquals(contentSource, repos.get(index).getContentSource());
    }

}