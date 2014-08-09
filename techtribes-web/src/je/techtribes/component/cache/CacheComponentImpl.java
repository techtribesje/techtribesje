package je.techtribes.component.cache;

import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.util.AbstractComponent;
import org.springframework.scheduling.annotation.Scheduled;

class CacheComponentImpl extends AbstractComponent {

    private ContentSourceComponent contentSourceComponent;
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
