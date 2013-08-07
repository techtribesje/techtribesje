package je.techtribes.component.scheduledcontentupdater;

import je.techtribes.component.badge.BadgeComponent;
import je.techtribes.component.badge.BadgeException;
import je.techtribes.domain.Talk;
import je.techtribes.domain.Tweet;
import je.techtribes.domain.*;
import je.techtribes.domain.badge.*;

import java.util.List;
import java.util.Set;

public class BadgeAwarder {

    private final ScheduledContentUpdater scheduledContentUpdater;

    private BadgeComponent badgeComponent;

    public BadgeAwarder(ScheduledContentUpdater scheduledContentUpdater, BadgeComponent badgeComponent) {
        this.scheduledContentUpdater = scheduledContentUpdater;
        this.badgeComponent = badgeComponent;
    }

    public void awardBadgesForActivity(List<Activity> activityList) {
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<ActivityBadge> badges = Badges.getActivityBadges();
        for (ActivityBadge badge : badges) {
            Set<ContentSource> contentSources = badge.findEligibleContentSources(activityList);

            for (ContentSource contentSource : contentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    public void awardBadgesForTalks(List<Talk> talks) {
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<TalkBadge> badges = Badges.getTalkBadges();
        for (TalkBadge badge : badges) {
            Set<ContentSource> contentSources = badge.findEligibleContentSources(talks);

            for (ContentSource contentSource : contentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    public void awardBadgesForContent(List<NewsFeedEntry> newsFeedEntries) {
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<ContentBadge> badges = Badges.getContentBadges();
        for (ContentBadge badge : badges) {
            Set<ContentSource> contentSources = badge.findEligibleContentSources(newsFeedEntries);

            for (ContentSource contentSource : contentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    public void awardBadgesForTweets(List<Tweet> tweets) {
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<TwitterBadge> badges = Badges.getTwitterBadges();
        for (TwitterBadge badge : badges) {
            Set<ContentSource> contentSources = badge.findEligibleContentSources(tweets);

            for (ContentSource contentSource : contentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    public void awardBadgesForContentSource(List<ContentSource> contentSources) {
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<ContentSourceBadge> badges = Badges.getContentSourceBadges();
        for (ContentSourceBadge badge : badges) {
            Set<ContentSource> eligibleContentSources = badge.findEligibleContentSources(contentSources);

            for (ContentSource contentSource : eligibleContentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    private void awardBadge(Badge badge, ContentSource contentSource, List<AwardedBadge> previouslyAwardedBadges) {
        try {
            if (!badgeAlreadyAwarded(badge, contentSource, previouslyAwardedBadges)) {
                scheduledContentUpdater.logInfo("Awarding '" + badge.getName() + "' to " + contentSource.getName());

                AwardedBadge awardedBadge = new AwardedBadge(badge, contentSource);
                badgeComponent.add(awardedBadge);
            }
        } catch (Exception e) {
            BadgeException be = new BadgeException("Error while awarding badge '" + badge.getName() + "' to " + contentSource.getName(), e);
            scheduledContentUpdater.logError(be);
            throw be;
        }
    }

    private boolean badgeAlreadyAwarded(Badge badge, ContentSource contentSource, List<AwardedBadge> previouslyAwardedBadges) {
        return previouslyAwardedBadges.contains(new AwardedBadge(badge, contentSource));
    }

}
