package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Island;
import je.techtribes.domain.Talk;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RockhopperBadge extends AbstractBadge implements TalkBadge {

    public RockhopperBadge() {
        super(Badges.ROCKHOPPER_ID, "Rockhopper", "You'll earn this badge if you do a talk in the \"other\" island.");
    }

    @Override
    public Set<ContentSource> findEligibleContentSources(Collection<Talk> talks) {
        Set<ContentSource> contentSources = new HashSet<>();

        if (talks != null) {
            for (Talk talk : talks) {
                if (talk.isInPast()) {
                    if (talk.getCountry().equals("Jersey") && talk.getContentSource().getIsland() == Island.Guernsey) {
                        contentSources.add(talk.getContentSource());
                    } else if (talk.getCountry().equals("Guernsey") && talk.getContentSource().getIsland() == Island.Jersey) {
                        contentSources.add(talk.getContentSource());
                    }
                }
            }
        }

        return contentSources;
    }

}
