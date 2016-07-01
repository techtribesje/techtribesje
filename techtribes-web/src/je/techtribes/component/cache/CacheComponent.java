package je.techtribes.component.cache;

import com.structurizr.annotation.Component;

@Component(description = "Refreshes the cache of content sources and their activity every five minutes.")
public interface CacheComponent {

    void refreshContentSourcesAndActivity();

}
