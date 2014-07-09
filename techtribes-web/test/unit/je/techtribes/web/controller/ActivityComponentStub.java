package je.techtribes.web.controller;

import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.domain.Activity;
import je.techtribes.domain.ContentSource;

import java.util.List;

public class ActivityComponentStub implements ActivityComponent {

    @Override
    public List<Activity> getActivityListForPeople() {
        return null;
    }

    @Override
    public List<Activity> getActivityListForBusinessTribes() {
        return null;
    }

    @Override
    public List<Activity> getActivityListForCommunityTribes() {
        return null;
    }

    @Override
    public Activity getActivity(ContentSource contentSource) {
        return null;
    }

    @Override
    public void refreshRecentActivity() {
    }

}
