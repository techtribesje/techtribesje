package je.techtribes.component.activity;

import com.codingthearchitecture.seos.element.Component;
import je.techtribes.domain.Activity;
import je.techtribes.domain.ContentSource;

import java.util.List;

@Component(responsibility = "Provides access to information about, and calculates, the recent activity rankings.")
public interface ActivityComponent {

    void calculateActivityForLastSevenDays();

    List<Activity> getActivityListForPeople();

    List<Activity> getActivityListForBusinessTribes();

    List<Activity> getActivityListForCommunityTribes();

    Activity getActivity(ContentSource contentSource);

    void refreshRecentActivity();

}
