package je.techtribes.component.newsfeedconnector;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.NewsFeed;

import java.util.List;

@Component
public interface NewsFeedConnector {

    List<NewsFeedEntry> loadNewsFeedEntries(NewsFeed feed);

}
