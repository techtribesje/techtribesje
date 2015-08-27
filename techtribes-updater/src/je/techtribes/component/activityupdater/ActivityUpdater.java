package je.techtribes.component.activityupdater;

import com.structurizr.annotation.Component;

/**
 * Updates the recent activity rankings.
 */
@Component
public interface ActivityUpdater {

    void calculateActivityForLastSevenDays();

}
