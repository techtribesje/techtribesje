package je.techtribes.component.scheduledcontentupdater;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.search.SearchResult;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.Tribe;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TechTribeMembershipUpdater {

    private final ScheduledContentUpdater scheduledContentUpdater;

    private final ContentSourceComponent contentSourceComponent;
    private final SearchComponent searchComponent;

    TechTribeMembershipUpdater(ScheduledContentUpdater scheduledContentUpdater, ContentSourceComponent contentSourceComponent, SearchComponent searchComponent) {
        this.scheduledContentUpdater = scheduledContentUpdater;
        this.contentSourceComponent = contentSourceComponent;
        this.searchComponent = searchComponent;
    }

    void update() {
        List<ContentSource> contentSources = contentSourceComponent.getContentSources(ContentSourceType.Tech);
        for (ContentSource cs : contentSources) {
            Tribe techTribe = (Tribe)cs;
            scheduledContentUpdater.logDebug("Determining membership for " + techTribe.getName() + " tech tribe");
            Map<ContentSource,Integer> searchResultCount = new HashMap<>();
            boolean hasMorePages = true;
            int page = 1;
            while (hasMorePages) {
                List<SearchResult> searchResults = searchComponent.searchForNewsFeedEntries(techTribe.getSearchTerms(), 100, page);
                for (SearchResult searchResult : searchResults) {
                    Integer count = 0;
                    if (searchResultCount.containsKey(searchResult.getContentSource())) {
                        count = searchResultCount.get(searchResult.getContentSource());
                    }
                    count++;
                    searchResultCount.put(searchResult.getContentSource(), count);
                }
                hasMorePages = (searchResults.size() == 100);
                page++;
            }

            // now update the tribe membership to include people with more than 10 search results
            List<Integer> personIds = new LinkedList<>();
            for (ContentSource contentSource : searchResultCount.keySet()) {
                if (contentSource.isPerson() && searchResultCount.get(contentSource) >= 5) {
                    personIds.add(contentSource.getId());
                }
            }
            contentSourceComponent.updateTribeMembers(techTribe, personIds);
        }
    }

}
