package je.techtribes.util.comparator;

import je.techtribes.domain.Activity;

import java.util.Comparator;

/**
 * Compares Activity objects by their score, highest first. If their scores are equal, they are compared by last
 * activity date (most recent first) instead.
 */
public class ActivityByScoreComparator implements Comparator<Activity> {

    @Override
    public int compare(Activity activity1, Activity activity2) {
        int result = new Long(activity2.getScore()).compareTo(activity1.getScore());
        if (result != 0) {
            return result;
        } else {
            return activity2.getLastActivityDate().compareTo(activity1.getLastActivityDate());
        }
    }

}
