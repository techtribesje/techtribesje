package je.techtribes.component.scheduledcontentupdater;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.newsfeedconnector.NewsFeedConnector;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.NewsFeed;
import je.techtribes.domain.NewsFeedEntry;

import java.util.List;

class NewsFeedUpdater {

    private final ScheduledContentUpdaterImpl scheduledContentUpdater;

    private final ContentSourceComponent contentSourceComponent;
    private final NewsFeedEntryComponent newsFeedEntryComponent;
    private final SearchComponent searchComponent;

    private final NewsFeedConnector newsFeedConnector;

    NewsFeedUpdater(ScheduledContentUpdaterImpl scheduledContentUpdater, ContentSourceComponent contentSourceComponent, NewsFeedConnector newsFeedConnector, NewsFeedEntryComponent newsFeedEntryComponent, SearchComponent searchComponent) {
        this.scheduledContentUpdater = scheduledContentUpdater;
        this.contentSourceComponent = contentSourceComponent;
        this.newsFeedConnector = newsFeedConnector;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.searchComponent = searchComponent;
    }

    void refreshNews() {
        try {
            List<ContentSource> contentSources = contentSourceComponent.getPeopleAndTribes();
            for (ContentSource contentSource : contentSources) {
                for (NewsFeed newsFeed : contentSource.getNewsFeeds()) {
                    try {
                        List<NewsFeedEntry> newsFeedEntries = newsFeedConnector.loadNewsFeedEntries(newsFeed);
                        newsFeedEntryComponent.storeNewsFeedEntries(newsFeedEntries);

                        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
                            searchComponent.add(newsFeedEntry);
                        }
                    } catch (Exception e) {
                        ScheduledContentUpdaterException scue = new ScheduledContentUpdaterException("Error updating news feed for " + newsFeed.getUrl(), e);
                        scheduledContentUpdater.logWarn(scue);
                    }
                }
            }
        } catch (Exception e) {
            ScheduledContentUpdaterException scue = new ScheduledContentUpdaterException("Error updating news feeds", e);
            scheduledContentUpdater.logError(scue);
        }
    }
}