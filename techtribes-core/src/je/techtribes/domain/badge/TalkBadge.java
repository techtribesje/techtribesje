package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Talk;

import java.util.Collection;
import java.util.Set;

public interface TalkBadge extends Badge {

    public Set<ContentSource> findEligibleContentSources(Collection<Talk> talks);

}
