package je.techtribes.component.scheduledcontentupdater;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.github.GitHubComponent;
import je.techtribes.component.githubconnector.GitHubConnector;
import je.techtribes.domain.ContentSource;

class GitHubUpdater {

    private final ScheduledContentUpdater scheduledContentUpdater;

    private final ContentSourceComponent contentSourceComponent;
    private final GitHubComponent gitHubComponent;

    private final GitHubConnector gitHubConnector;

    GitHubUpdater(ScheduledContentUpdater scheduledContentUpdater, ContentSourceComponent contentSourceComponent, GitHubComponent gitHubComponent, GitHubConnector gitHubConnector) {
        this.scheduledContentUpdater = scheduledContentUpdater;
        this.contentSourceComponent = contentSourceComponent;
        this.gitHubComponent = gitHubComponent;
        this.gitHubConnector = gitHubConnector;
    }

    void refreshGitHubRepositories() {
        try {
            for (ContentSource contentSource : contentSourceComponent.getPeopleAndTribes()) {
                if (contentSource.hasGitHubId()) {
                    gitHubComponent.setRepositories(gitHubConnector.getRepositories(contentSource), contentSource);
                }
            }
        } catch (Exception e) {
            ScheduledContentUpdaterException scue = new ScheduledContentUpdaterException("Error retrieving GitHub repos", e);
            scheduledContentUpdater.logError(scue);
        }
    }
}