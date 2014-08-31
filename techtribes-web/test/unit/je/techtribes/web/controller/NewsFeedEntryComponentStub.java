package je.techtribes.web.controller;

import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.NewsFeedEntry;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NewsFeedEntryComponentStub implements NewsFeedEntryComponent {

    @Override
    public List<NewsFeedEntry> getRecentNewsFeedEntries(int page, int pageSize) {
        List<NewsFeedEntry> newsFeedEntries = new LinkedList<>();
        for (int i = 1; i <= pageSize; i++) {
            newsFeedEntries.add(new NewsFeedEntry("http://somedomain.com/" + (i + (pageSize * (page-1))), "Title", "Body", new Date(), 1));
        }
        return newsFeedEntries;
    }

    @Override
    public List<NewsFeedEntry> getRecentNewsFeedEntries(ContentSource contentSource, int pageSize) {
        return null;
    }

    @Override
    public List<NewsFeedEntry> getNewsFeedEntries(Date start, Date end) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<NewsFeedEntry> getRecentNewsFeedEntries(Collection<ContentSource> contentSources, int page, int pageSize) {
        return null;
    }

    @Override
    public long getNumberOfNewsFeedEntries(ContentSource contentSource, Date start, Date end) {
        return 120;
    }

    @Override
    public long getNumberOfNewsFeedEntries() {
        return 1200;
    }

    @Override
    public long getNumberOfNewsFeedEntries(Collection<ContentSource> contentSources) {
        return 120;
    }

    @Override
    public void storeNewsFeedEntries(Collection<NewsFeedEntry> newsFeedEntries) {
    }

    @Override
    public void removeNewsFeedEntry(String url) {
    }

}
