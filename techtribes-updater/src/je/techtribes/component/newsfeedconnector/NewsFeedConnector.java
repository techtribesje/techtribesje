package je.techtribes.component.newsfeedconnector;

import com.structurizr.element.Component;
import com.structurizr.element.Integration;
import com.structurizr.element.IntegrationType;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.NewsFeed;

import java.util.List;

@Component(responsibility = "Retrieves news feed entries from RSS and Atom feeds.")
@Integration(type = IntegrationType.External, target = "Blogs via RSS/Atom", responsibility = "Gets blog/news posts from")
public interface NewsFeedConnector {

    List<NewsFeedEntry> loadNewsFeedEntries(NewsFeed feed);

}
