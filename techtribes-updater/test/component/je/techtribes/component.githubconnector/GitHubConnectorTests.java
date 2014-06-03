package je.techtribes.component.githubconnector;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.GitHubRepository;
import je.techtribes.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        assertEquals(2, repos.size());

        assertEquals("techtribesje", repos.get(0).getName());
        assertEquals("Source code for the techtribes.je website", repos.get(0).getDescription());
        assertEquals("https://github.com/techtribesje/techtribesje", repos.get(0).getUrl());
        assertEquals(contentSource, repos.get(0).getContentSource());

        assertEquals("techtribesje-bin", repos.get(1).getName());
        assertEquals("Scripts for the techtribes.je live environment", repos.get(1).getDescription());
        assertEquals("https://github.com/techtribesje/techtribesje-bin", repos.get(1).getUrl());
        assertEquals(contentSource, repos.get(1).getContentSource());
    }

}
