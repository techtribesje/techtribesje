package je.techtribes.component.badgeawarder;

import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.component.badge.BadgeComponent;
import je.techtribes.component.badge.BadgeException;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.component.tweet.TweetComponent;
import je.techtribes.domain.*;
import je.techtribes.domain.badge.*;
import je.techtribes.util.AbstractComponent;

import java.util.List;
import java.util.Set;

class BadgeAwarderImpl extends AbstractComponent implements BadgeAwarder {

    private ActivityComponent activityComponent;
    private BadgeComponent badgeComponent;
    private ContentSourceComponent contentSourceComponent;
    private NewsFeedEntryComponent newsFeedEntryComponent;
    private TalkComponent talkComponent;
    private TweetComponent tweetComponent;

    BadgeAwarderImpl(ActivityComponent activityComponent, BadgeComponent badgeComponent, ContentSourceComponent contentSourceComponent, NewsFeedEntryComponent newsFeedEntryComponent, TalkComponent talkComponent, TweetComponent tweetComponent) {
        this.activityComponent = activityComponent;
        this.badgeComponent = badgeComponent;
        this.contentSourceComponent = contentSourceComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
        this.talkComponent = talkComponent;
        this.tweetComponent = tweetComponent;
    }

    @Override
    public void awardBadgesForActivity() {
        activityComponent.refreshRecentActivity();
        awardBadgesForActivity(activityComponent.getActivityListForPeople());
        awardBadgesForActivity(activityComponent.getActivityListForBusinessTribes());
        awardBadgesForActivity(activityComponent.getActivityListForCommunityTribes());
    }

    private void awardBadgesForActivity(List<Activity> activityList) {
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<ActivityBadge> badges = Badges.getActivityBadges();
        for (ActivityBadge badge : badges) {
            Set<ContentSource> contentSources = badge.findEligibleContentSources(activityList);

            for (ContentSource contentSource : contentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    @Override
    public void awardBadgesForTalks() {
        List<Talk> talks = talkComponent.getRecentTalks();
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<TalkBadge> badges = Badges.getTalkBadges();
        for (TalkBadge badge : badges) {
            Set<ContentSource> contentSources = badge.findEligibleContentSources(talks);

            for (ContentSource contentSource : contentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    @Override
    public void awardBadgesForContent() {
        List<NewsFeedEntry> newsFeedEntries = newsFeedEntryComponent.getRecentNewsFeedEntries(1, 100);
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<ContentBadge> badges = Badges.getContentBadges();
        for (ContentBadge badge : badges) {
            Set<ContentSource> contentSources = badge.findEligibleContentSources(newsFeedEntries);

            for (ContentSource contentSource : contentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    @Override
    public void awardBadgesForTweets() {
        List<Tweet> tweets = tweetComponent.getRecentTweets(1, 100);
        List<AwardedBadge> previouslyAwardedBadges = badgeComponent.getAwardedBadges();
        List<TwitterBadge> badges = Badges.getTwitterBadges();
        for (TwitterBadge badge : badges) {
            Set<ContentSource> contentSources = badge.findEligibleContentSources(tweets);

            for (ContentSource contentSource : contentSources) {
                awardBadge(badge, contentSource, previouslyAwardedBadges);
            }
        }
    }

    @Override
    public void awardBadgesForContentSource() {
        List<ContentSource> contentSources = contentSourceComponent.getPeopleAndTribes();
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
                logInfo("Awarding '" + badge.getName() + "' to " + contentSource.getName());

                AwardedBadge awardedBadge = new AwardedBadge(badge, contentSource);
                badgeComponent.add(awardedBadge);
            }
        } catch (Exception e) {
            BadgeException be = new BadgeException("Error while awarding badge '" + badge.getName() + "' to " + contentSource.getName(), e);
            logError(be);
            throw be;
        }
    }

    private boolean badgeAlreadyAwarded(Badge badge, ContentSource contentSource, List<AwardedBadge> previouslyAwardedBadges) {
        return previouslyAwardedBadges.contains(new AwardedBadge(badge, contentSource));
    }

}
