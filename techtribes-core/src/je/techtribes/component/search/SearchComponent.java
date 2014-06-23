package je.techtribes.component.search;

import com.structurizr.annotation.Component;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Tweet;

import java.util.Collection;
import java.util.List;

@Component(description = "Search facilities for news feed entries and tweets.")
public interface SearchComponent {

    void addNewsFeedEntries(Collection<NewsFeedEntry> newsFeedEntries);

    void addTweets(Collection<Tweet> tweets);

    void deleteTweet(long id);

    void removeNewsFeedEntry(String url);

    List<SearchResult> searchForNewsFeedEntries(String query, int hitsPerPage, int page);

    List<SearchResult> searchForTweets(String query, int hitsPerPage, int page);

    void clearSearchIndex();

}
