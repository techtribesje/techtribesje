package je.techtribes.component.cache;

import com.structurizr.annotation.UsesComponent;
import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.util.AbstractComponent;
import org.springframework.scheduling.annotation.Scheduled;

class CacheComponentImpl extends AbstractComponent implements CacheComponent {

    @UsesComponent(description = "Refreshes content sources using")
    private ContentSourceComponent contentSourceComponent;

    @UsesComponent(description = "Refreshes activity information using")
    private ActivityComponent activityComponent;

    CacheComponentImpl(ContentSourceComponent contentSourceComponent, ActivityComponent activityComponent) {
        this.contentSourceComponent = contentSourceComponent;
        this.activityComponent = activityComponent;
    }

    @Scheduled(cron="0 */5 * * * ?")
    public synchronized void refreshContentSourcesAndActivity() {
        contentSourceComponent.refreshContentSources();
        activityComponent.refreshRecentActivity();
    }

}
