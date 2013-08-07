package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Tweet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TweeterBadge extends AbstractBadge implements TwitterBadge {

    public TweeterBadge() {
        super(Badges.TWEETER_ID, "Tweeter", "This badge is awarded to those who tweet.");
    }

    @Override
    public Set<ContentSource> findEligibleContentSources(Collection<Tweet> tweets) {
        Set<ContentSource> contentSources = new HashSet<>();

        if (tweets != null) {
            for (Tweet tweet : tweets) {
                contentSources.add(tweet.getContentSource());
            }
        }

        return contentSources;
    }

}
