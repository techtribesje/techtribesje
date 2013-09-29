package je.techtribes.component.search;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Tweet;

import java.util.List;

/**
 * Search facilities for news feed entries and tweets.
 */
@Component
public interface SearchComponent {

    void add(NewsFeedEntry newsFeedEntry);

    void add(Tweet tweet);

    void deleteTweet(long id);

    List<SearchResult> searchForNewsFeedEntries(String query, int hitsPerPage, int page);

    List<SearchResult> searchForTweets(String query, int hitsPerPage);

    List<SearchResult> searchForAll(String query, int hitsPerPage);

    void clearSearchIndex();

}
