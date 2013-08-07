package je.techtribes.util.comparator;

import je.techtribes.domain.GitHubRepository;

import java.util.Comparator;

/**
 * Compares GitHubRepository instances by name (ignoring case).
 */
public class GitHubRepositoryByNameComparator implements Comparator<GitHubRepository> {

	public int compare(GitHubRepository repo1, GitHubRepository repo2) {
		return repo1.getName().toLowerCase().compareTo(repo2.getName().toLowerCase());
	}

}