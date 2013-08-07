package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Tweet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CommunityMentionerBadge extends AbstractBadge implements TwitterBadge {

    public CommunityMentionerBadge() {
        super(Badges.COMMUNITY_MENTIONER_ID, "Community Mentioner", "Tweeters will get this badge if they mention one of the local communities that we're looking out for. <span style=\"color: gray;\">@WhichCommunity?</span>");
    }

    @Override
    public Set<ContentSource> findEligibleContentSources(Collection<Tweet> tweets) {
        Set<ContentSource> contentSources = new HashSet<>();

        if (tweets != null) {
            for (Tweet tweet : tweets) {
                String body = tweet.getBody().toLowerCase();
                if (    body.contains("@digitaljersey") ||
                        body.contains("@bcsjersey") ||
                        body.contains("@techtribesje")
                        ) {
                    contentSources.add(tweet.getContentSource());
                }
            }
        }

        return contentSources;
    }

}
