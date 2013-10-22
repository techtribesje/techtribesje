package je.techtribes.domain.badge;

import je.techtribes.domain.Activity;
import je.techtribes.domain.ContentSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FrequentBloggerBadge extends AbstractBadge implements ActivityBadge {

    public FrequentBloggerBadge() {
        super(Badges.FREQUENT_BLOGGER_ID, "Frequent Blogger", "This badge is earned by blogging more than once in a 7 day period.");
    }

    @Override
    public Set<ContentSource> findEligibleContentSources(List<Activity> activityList) {
        Set<ContentSource> contentSources = new HashSet<>();

        if (activityList != null) {
            for (Activity activity : activityList) {
                if (activity.getNumberOfNewsFeedEntries() > 1) {
                    contentSources.add(activity.getContentSource());
                }
            }
        }

        return contentSources;
    }

}
