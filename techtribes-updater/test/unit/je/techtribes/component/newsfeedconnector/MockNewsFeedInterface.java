package je.techtribes.component.newsfeedconnector;

import je.techtribes.domain.NewsFeed;
import je.techtribes.domain.NewsFeedEntry;

import java.util.List;

class MockNewsFeedInterface extends NewsFeedInterface {

    private List<NewsFeedEntry> newsFeedEntries;

    void setNewsFeedEntries(List<NewsFeedEntry> newsFeedEntries) {
        this.newsFeedEntries = newsFeedEntries;
    }

    @Override
    List<NewsFeedEntry> loadNewsFeedEntries(NewsFeed feed) {
        return this.newsFeedEntries;
    }

}
