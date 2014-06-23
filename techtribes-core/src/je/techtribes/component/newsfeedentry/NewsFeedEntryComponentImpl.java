package je.techtribes.component.newsfeedentry;

import com.structurizr.annotation.ContainerDependency;
import je.techtribes.util.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.component.contentsource.ContentItemFilter;
import je.techtribes.util.ContentSourceCollectionFormatter;
import je.techtribes.util.MongoDatabaseConfiguration;
import je.techtribes.util.PageSize;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@ContainerDependency(target="NoSQL Data Store", description = "Reads from and writes to")
class NewsFeedEntryComponentImpl extends AbstractComponent implements NewsFeedEntryComponent {

    private ContentSourceComponent contentSourceComponent;
    private MongoDbNewsFeedEntryDao newsFeedEntryDao;

    NewsFeedEntryComponentImpl(MongoDatabaseConfiguration mongoDatabaseConfiguration, ContentSourceComponent contentSourceComponent) {
        this.contentSourceComponent = contentSourceComponent;
        this.newsFeedEntryDao = new MongoDbNewsFeedEntryDao(mongoDatabaseConfiguration, LoggingComponentFactory.create());
    }

    public List<NewsFeedEntry> getRecentNewsFeedEntries(int page, int pageSize) {
        try {
            page = PageSize.validatePage(page);
            pageSize = PageSize.validatePageSize(pageSize);
            List<NewsFeedEntry> newsFeedEntries = newsFeedEntryDao.getRecentNewsFeedEntries(page, pageSize);
            filterAndEnrich(newsFeedEntries);

            return newsFeedEntries;
        } catch (Exception e) {
            NewsFeedEntryException nfee = new NewsFeedEntryException("Error getting recent news feed entries", e);
            logError(nfee);
            throw nfee;
        }
    }

    public List<NewsFeedEntry> getRecentNewsFeedEntries(ContentSource contentSource, int pageSize) {
        try {
            pageSize = PageSize.validatePageSize(pageSize);
            List<NewsFeedEntry> newsFeedEntries = newsFeedEntryDao.getRecentNewsFeedEntries(contentSource, pageSize);
            filterAndEnrich(newsFeedEntries);

            return newsFeedEntries;
        } catch (Exception e) {
            NewsFeedEntryException nfee = new NewsFeedEntryException("Error getting recent news feed entries for content source with ID " + contentSource.getId(), e);
            logError(nfee);
            throw nfee;
        }
    }

    @Override
    public List<NewsFeedEntry> getRecentNewsFeedEntries(Collection<ContentSource> contentSources, int page, int pageSize) {
        try {
            if (contentSources == null || contentSources.isEmpty()) {
                return new LinkedList<>();
            }

            page = PageSize.validatePage(page);
            pageSize = PageSize.validatePageSize(pageSize);
            List<NewsFeedEntry> newsFeedEntries = newsFeedEntryDao.getRecentNewsFeedEntries(contentSources, page, pageSize);
            filterAndEnrich(newsFeedEntries);

            return newsFeedEntries;
        } catch (Exception e) {
            NewsFeedEntryException nfee = new NewsFeedEntryException("Error getting recent news feed entries for " + ContentSourceCollectionFormatter.format(contentSources), e);
            logError(nfee);
            throw nfee;
        }
    }

    @Override
    public long getNumberOfNewsFeedEntries(ContentSource contentSource, Date start, Date end) {
        try {
            return newsFeedEntryDao.getNumberOfNewsFeedEntries(contentSource, start, end);
        } catch (Exception e) {
            NewsFeedEntryException nfee = new NewsFeedEntryException("Error getting number of news feed entries for content source with ID " + contentSource.getId(), e);
            logError(nfee);
            throw nfee;
        }
    }

    @Override
    public long getNumberOfNewsFeedEntries() {
        try {
            return newsFeedEntryDao.getNumberOfNewsFeedEntries();
        } catch (Exception e) {
            NewsFeedEntryException nfee = new NewsFeedEntryException("Error getting number of news feed entries", e);
            logError(nfee);
            throw nfee;
        }
    }

    @Override
    public long getNumberOfNewsFeedEntries(Collection<ContentSource> contentSources) {
        try {
            return newsFeedEntryDao.getNumberOfNewsFeedEntries(contentSources);
        } catch (Exception e) {
            NewsFeedEntryException nfee = new NewsFeedEntryException("Error getting number of news feed entries for " + ContentSourceCollectionFormatter.format(contentSources), e);
            logError(nfee);
            throw nfee;
        }
    }

    @Override
    public void storeNewsFeedEntries(Collection<NewsFeedEntry> newsFeedEntries) {
        try {
            newsFeedEntryDao.storeNewsFeedEntries(newsFeedEntries);
        } catch (Exception e) {
            NewsFeedEntryException nfee = new NewsFeedEntryException("Error saving recent news feed entries", e);
            logError(nfee);
            throw nfee;
        }
    }

    @Override
    public void removeNewsFeedEntry(String url) {
        try {
            newsFeedEntryDao.removeNewsFeedEntry(url);
        } catch (Exception e) {
            NewsFeedEntryException nfee = new NewsFeedEntryException("Error removing news feed entry with URL " + url, e);
            logError(nfee);
            throw nfee;
        }
    }

    private void filterAndEnrich(List<NewsFeedEntry> newsFeedEntries) {
        ContentItemFilter<NewsFeedEntry> filter = new ContentItemFilter<>();
        filter.filter(newsFeedEntries, contentSourceComponent, false);
    }

}
