package je.techtribes.component.newsfeedconnector;

import je.techtribes.component.log.LoggingComponent;
import je.techtribes.domain.NewsFeed;
import je.techtribes.domain.NewsFeedEntry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

abstract class NewsFeedInterface {

    protected LoggingComponent loggingComponent;

    @Autowired
    public void setLoggingComponent(LoggingComponent loggingComponent) {
        this.loggingComponent = loggingComponent;
    }

    abstract List<NewsFeedEntry> loadNewsFeedEntries(NewsFeed feed);

}
