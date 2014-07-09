package je.techtribes.component.activityupdater;

import com.structurizr.annotation.Component;

@Component(description = "Updates the recent activity rankings.")
public interface ActivityUpdaterComponent {

    void calculateActivityForLastSevenDays();

}
