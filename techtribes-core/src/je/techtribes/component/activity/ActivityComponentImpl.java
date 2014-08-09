package je.techtribes.component.activity;

import com.structurizr.annotation.ContainerDependency;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.domain.Activity;
import je.techtribes.domain.ContentSource;
import je.techtribes.util.AbstractComponent;
import je.techtribes.util.JdbcDatabaseConfiguration;
import je.techtribes.util.comparator.ActivityByScoreComparator;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@ContainerDependency(target="Relational Database", description = "Reads from and writes to")
class ActivityComponentImpl extends AbstractComponent implements ActivityComponent {

    private ContentSourceComponent contentSourceComponent;

    private JdbcActivityDao activityDao;

    private Map<ContentSource, Activity> activitiesByContentSource = new HashMap<>();
    private List<Activity> activityListForPeople = new LinkedList<>();
    private List<Activity> activityListForBusinessTribes = new LinkedList<>();
    private List<Activity> activityListForCommunityTribes = new LinkedList<>();

    ActivityComponentImpl(ContentSourceComponent contentSourceComponent, JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.contentSourceComponent = contentSourceComponent;
        this.activityDao = new JdbcActivityDao(jdbcDatabaseConfiguration);
    }

    public void init() {
        try {
            refreshRecentActivity();
        } catch (Exception e) {
            logError(new ActivityException("Could not initialise component", e));
        }
    }

    public List<Activity> getActivityListForPeople() {
        return activityListForPeople;
    }

    public List<Activity> getActivityListForBusinessTribes() {
        return activityListForBusinessTribes;
    }

    public List<Activity> getActivityListForCommunityTribes() {
        return activityListForCommunityTribes;
    }

    @Override
    public Activity getActivity(ContentSource contentSource) {
        if (activitiesByContentSource.containsKey(contentSource)) {
            return activitiesByContentSource.get(contentSource);
        } else {
            return null;
        }
    }

    @Override
    public synchronized void refreshRecentActivity() {
        try {
            List<Activity> activityListForPeople = new LinkedList<>();
            List<Activity> activityListForBusinessTribes = new LinkedList<>();
            List<Activity> activityListForMediaTribes = new LinkedList<>();
            List<Activity> activityListForCommunityTribes = new LinkedList<>();

            Collection<Activity> activityCollection = activityDao.getRecentActivity();
            filterAndEnrich(activityCollection);

            Map<ContentSource,Activity> activityMap = toMap(activityCollection);

            // mark content sources as active or not
            for (Activity activity : activityCollection) {
                if (activity.getScore() > 0) {
                    activity.getContentSource().setActive(true);
                }

                switch (activity.getContentSource().getType()) {
                    case Person:
                        activityListForPeople.add(activity);
                        break;
                    case Business:
                        activityListForBusinessTribes.add(activity);
                        break;
                    case Media:
                        activityListForMediaTribes.add(activity);
                        break;
                    case Community:
                        activityListForCommunityTribes.add(activity);
                        break;
                }
            }

            Collections.sort(activityListForPeople, new ActivityByScoreComparator());
            Collections.sort(activityListForBusinessTribes, new ActivityByScoreComparator());
            Collections.sort(activityListForCommunityTribes, new ActivityByScoreComparator());
            Collections.sort(activityListForMediaTribes, new ActivityByScoreComparator());

            this.activitiesByContentSource = activityMap;
            this.activityListForPeople = activityListForPeople;
            this.activityListForBusinessTribes = activityListForBusinessTribes;
            this.activityListForCommunityTribes = activityListForCommunityTribes;
        } catch (Exception e) {
            ActivityException ae = new ActivityException("Error refreshing recent activity", e);
            logError(ae);
            throw ae;
        }
    }

    private void filterAndEnrich(Collection<Activity> activityCollection) {
        Iterator<Activity> it = activityCollection.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            ContentSource contentSource = contentSourceComponent.findById(activity.getContentSourceId());
            if (contentSource != null && contentSource.isContentAggregated()) {
                activity.setContentSource(contentSource);
            } else {
                LoggingComponentFactory.create().debug(this, "Filtering activity associated with content source ID " + activity.getContentSourceId());
                it.remove();
            }
        }
    }

    private Map<ContentSource,Activity> toMap(Collection<Activity> activityCollection) {
        Map<ContentSource, Activity> activityMapByContentSource = new HashMap<>();

        for (Activity activity : activityCollection) {
            activityMapByContentSource.put(activity.getContentSource(), activity);
        }

        return activityMapByContentSource;
    }

}