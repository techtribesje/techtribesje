package je.techtribes.component.newsfeedentry;

import com.structurizr.annotation.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.NewsFeedEntry;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component(description = "Provides access to blog entries and news.")
public interface NewsFeedEntryComponent {

    List<NewsFeedEntry> getRecentNewsFeedEntries(int page, int pageSize);

    List<NewsFeedEntry> getNewsFeedEntries(Date start, Date end);

    List<NewsFeedEntry> getRecentNewsFeedEntries(ContentSource contentSource, int pageSize);

    List<NewsFeedEntry> getRecentNewsFeedEntries(Collection<ContentSource> contentSources, int page, int pageSize);

    long getNumberOfNewsFeedEntries(ContentSource contentSource, Date start, Date end);

    long getNumberOfNewsFeedEntries();

    long getNumberOfNewsFeedEntries(Collection<ContentSource> contentSources);

    void storeNewsFeedEntries(Collection<NewsFeedEntry> newsFeedEntries);

    void removeNewsFeedEntry(String url);

}
