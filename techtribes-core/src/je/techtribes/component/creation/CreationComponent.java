package je.techtribes.component.creation;

import com.structurizr.annotation.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Creation;

import java.util.List;

@Component(description = "Provides access to information about creations by local people/businesses.")
public interface CreationComponent {

    /**
     * Gets a list of creations by local people/businesses.
     */
    List<Creation> getCreations();

    /**
     * Gets a list of creations for a specific person/business.
     */
    List<Creation> getCreations(ContentSource contentSource);

}
