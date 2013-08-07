package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Talk;

import java.util.*;

public abstract class AbstractJetsetterBadge extends AbstractBadge implements TalkBadge {

    public AbstractJetsetterBadge(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public final Set<ContentSource> findEligibleContentSources(Collection<Talk> talks) {
        Map<ContentSource, Integer> talkCount = new HashMap<>();

        if (talks != null) {
            for (Talk talk : talks) {
                if (!talk.isLocal() && talk.isInPast()) {
                    int count = 0;
                    if (talkCount.containsKey(talk.getContentSource())) {
                        count = talkCount.get(talk.getContentSource());
                    }
                    count++;
                    talkCount.put(talk.getContentSource(), count);
                }
            }
        }

        Set<ContentSource> contentSources = new HashSet<>();
        for (ContentSource contentSource : talkCount.keySet()) {
            int count = talkCount.get(contentSource);
            if (count >= getThreshold()) {
                contentSources.add(contentSource);
            }
        }
        return contentSources;
    }

    protected abstract int getThreshold();

}
