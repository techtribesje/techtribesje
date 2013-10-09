package je.techtribes.component.scheduledcontentupdater;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.search.SearchComponent;
import je.techtribes.component.search.SearchResult;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.Tribe;

import java.util.*;

class TechTribeMembershipUpdater {

    private static final int PAGE_SIZE = 100;

    private final ScheduledContentUpdaterImpl scheduledContentUpdater;
    private final ContentSourceComponent contentSourceComponent;
    private final SearchComponent searchComponent;

    TechTribeMembershipUpdater(ScheduledContentUpdaterImpl scheduledContentUpdater, ContentSourceComponent contentSourceComponent, SearchComponent searchComponent) {
        this.scheduledContentUpdater = scheduledContentUpdater;
        this.contentSourceComponent = contentSourceComponent;
        this.searchComponent = searchComponent;
    }

    void update() {
        List<ContentSource> contentSources = contentSourceComponent.getContentSources(ContentSourceType.Tech);
        for (ContentSource cs : contentSources) {
            final Tribe techTribe = (Tribe)cs;
            scheduledContentUpdater.logDebug("Determining membership for " + techTribe.getName() + " tech tribe");

            Set<Integer> personIds = getContentSourceIds(5, new Searcher() {
                @Override
                public List<SearchResult> search(int page) {
                    return searchComponent.searchForNewsFeedEntries(techTribe.getSearchTerms(), PAGE_SIZE, page);
                }
            });
            personIds.addAll(getContentSourceIds(25, new Searcher() {
                @Override
                public List<SearchResult> search(int page) {
                    return searchComponent.searchForTweets(techTribe.getSearchTerms(), PAGE_SIZE, page);
                }
            }));

            contentSourceComponent.updateTribeMembers(techTribe, personIds);
        }

    }

    private Set<Integer> getContentSourceIds(int threshold, Searcher searcher) {
        Map<ContentSource,Integer> searchResultCount = new HashMap<>();
        boolean hasMorePages = true;
        int page = 1;
        while (hasMorePages) {
            List<SearchResult> searchResults = searcher.search(page);
            for (SearchResult searchResult : searchResults) {
                Integer count = 0;
                if (searchResultCount.containsKey(searchResult.getContentSource())) {
                    count = searchResultCount.get(searchResult.getContentSource());
                }
                count++;
                searchResultCount.put(searchResult.getContentSource(), count);
            }
            hasMorePages = (searchResults.size() == PAGE_SIZE);
            page++;
        }

        // find people with more than X search results
        Set<Integer> personIds = new HashSet<>();
        for (ContentSource contentSource : searchResultCount.keySet()) {
            if (contentSource.isPerson() && contentSource.isContentAggregated() && searchResultCount.get(contentSource) >= threshold) {
                personIds.add(contentSource.getId());
            }
        }

        return personIds;
    }

    interface Searcher {
        List<SearchResult> search(int page);
    }

}
