package je.techtribes.component.cache;

import com.structurizr.annotation.Component;
import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.util.AbstractComponent;
import org.springframework.scheduling.annotation.Scheduled;

@Component(description = "Refreshes the cache of content sources and their activity every five minutes.")
public interface CacheComponent {

    void refreshContentSourcesAndActivity();

}
