package je.techtribes.component.search;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Tweet;

import java.util.Collection;
import java.util.List;

@Component(responsibility = "Search facilities for news feed entries and tweets.")
public interface SearchComponent {

    void addNewsFeedEntries(Collection<NewsFeedEntry> newsFeedEntries);

    void addTweets(Collection<Tweet> tweets);

    void deleteTweet(long id);

    List<SearchResult> searchForNewsFeedEntries(String query, int hitsPerPage, int page);

    List<SearchResult> searchForTweets(String query, int hitsPerPage, int page);

    void clearSearchIndex();

}
