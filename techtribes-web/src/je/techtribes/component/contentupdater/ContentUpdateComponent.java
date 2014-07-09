package je.techtribes.component.contentupdater;

import com.structurizr.annotation.Component;
import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.util.AbstractComponent;
import org.springframework.scheduling.annotation.Scheduled;

@Component(description = "Refreshes the (cached) list of content sources and activity rankings on the website every 5 minutes.")
public class ContentUpdateComponent extends AbstractComponent {

    private ContentSourceComponent contentSourceComponent;
    private ActivityComponent activityComponent;

    public ContentUpdateComponent(ContentSourceComponent contentSourceComponent, ActivityComponent activityComponent) {
        this.contentSourceComponent = contentSourceComponent;
        this.activityComponent = activityComponent;
    }

    public void start() {
        refresh();
    }

    @Scheduled(cron="0 */5 * * * ?")
    public void refresh() {
        try {
            contentSourceComponent.refreshContentSources();
            activityComponent.refreshRecentActivity();
        } catch (Exception e) {
            logError(new ContentUpdateComponentException("Error while refreshing content sources and activity", e));
        }
    }

}