package je.techtribes.util.comparator;

import je.techtribes.domain.GitHubRepository;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GitHubRepositoryByNameComparatorTests {

    @Test
    public void test() {
        GitHubRepository r1 = new GitHubRepository("aaa", "aaa", "http://github.com/user/aaa");
        GitHubRepository r2 = new GitHubRepository("BBB", "BBB", "http://github.com/user/BBB");

        GitHubRepositoryByNameComparator comparator = new GitHubRepositoryByNameComparator();
        assertTrue(comparator.compare(r1, r2) < 0);
        assertTrue(comparator.compare(r1, r1) == 0);
        assertTrue(comparator.compare(r2, r1) > 0);
    }

}
