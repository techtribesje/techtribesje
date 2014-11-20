package je.techtribes;

import je.techtribes.component.activityupdater.ActivityUpdater;
import je.techtribes.component.newsfeedconnector.NewsFeedConnector;

public abstract class AbstractUpdaterComponentTestsBase extends AbstractComponentTestsBase {

    protected static ActivityUpdater getActivityUpdater() {
        return (ActivityUpdater)applicationContext.getBean("activityUpdater");
    }

    protected static NewsFeedConnector getNewsFeedConnector() {
        return (NewsFeedConnector)applicationContext.getBean("newsFeedConnector");
    }

}
