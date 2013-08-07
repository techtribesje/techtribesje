package je.techtribes.component.github;

import je.techtribes.domain.GitHubRepository;
import je.techtribes.domain.Person;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GitHubRepositoryTests {

    private GitHubRepository repo;

    @Before
    public void setUp() {
        repo = new GitHubRepository("repoName", "repoDescription", "https://github.com/user/repoName");
    }

    @Test
    public void testGetName() {
        assertEquals("repoName", repo.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("repoDescription", repo.getDescription());
    }

    @Test
    public void testGetUrl() {
        assertEquals("https://github.com/user/repoName", repo.getUrl());
    }

    @Test
    public void testGetContentSourceId() {
        repo.setContentSourceId(100);
        assertEquals(100, repo.getContentSourceId());
    }

    @Test
    public void testGetContentSource() {
        Person person = new Person();
        repo.setContentSource(person);
        assertEquals(person, repo.getContentSource());
    }

}
