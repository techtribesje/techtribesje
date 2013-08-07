package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Tweet;

import java.util.Collection;
import java.util.Set;

public interface TwitterBadge extends Badge {

    public Set<ContentSource> findEligibleContentSources(Collection<Tweet> tweets);

}
