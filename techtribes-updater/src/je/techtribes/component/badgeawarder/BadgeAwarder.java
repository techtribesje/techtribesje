package je.techtribes.component.badgeawarder;

import com.structurizr.annotation.Component;
import je.techtribes.domain.*;

import java.util.List;

/**
 * Awards badges to people and tribes based upon recent activity.
 */
@Component
public interface BadgeAwarder {

    public void awardBadgesForActivity();

    public void awardBadgesForTalks();

    public void awardBadgesForContent();

    public void awardBadgesForTweets();

    public void awardBadgesForContentSource();

}
