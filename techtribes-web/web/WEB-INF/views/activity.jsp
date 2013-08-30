<%@ page import="je.techtribes.domain.Activity" %>
<div class="sectionHeading">Activity</div>

<p>
    Here's a summary of the most active people and tribes over the past 7 days. Points are awarded for the following:
</p>
<ul>
    <li><%= Activity.INTERNATIONAL_TALK_SCORE %> points for an international talk (lots of points because this takes some effort).</li>
    <li><%= Activity.LOCAL_TALK_SCORE%> points for each local talk (much less time in an aeroplane needed).</li>
    <li><%= Activity.CONTENT_SCORE%> points for each piece of content (e.g. blog entry or news item).</li>
    <li>1 point for each tweet, but only for the first <%= Activity.MAXIMUM_TWEET_SCORE %> tweets per person or tribe.</li>
    <li>Community tribes will also get <%= Activity.EVENT_SCORE%> points for each event that they run.</li>
</ul>

<h4>Why are the points for tweets capped?</h4>
<p>
    Twitter can seem noisy at times and although we want people to engage through it, we think there's more value in people
    sharing their knowledge and experience through blogs and talks. Capping tweets at <%= Activity.MAXIMUM_TWEET_SCORE %>
    points allows anybody blogging to sneak ahead in the leaderboard.
</p>

<h4>How are business tribes scored?</h4>
<p>
    The score for business tribes is simply (the score for all members + the score for the tribe).
    Again, there's a tweet cap of 25.
</p>

<hr />

<h3>Badges</h3>
<p>
    <a href="/badges">Badges</a> are awarded to people or tribes based upon their activity. These lucky monkeys have done something recently to earn themselves a badge.
</p>
<p>
    <c:forEach var="awardedBadge" items="${recentAwardedBadges}">
        <a href="<techtribesje:goto contentSource="${awardedBadge.contentSource}" />"><img src="${awardedBadge.contentSource.profileImageUrl}" alt="${awardedBadge.contentSource.name}" class="profileImageSmall" /></a>
        <a href="/badges#${awardedBadge.badge.id}"><techtribesje:badge id="${awardedBadge.badge.id}" size="24" /></a>
        &nbsp;
    </c:forEach>
</p>

<hr />

<div class="row">
    <div class="span3">
        <span class="badge">Position</span>
        <img src="/static/img/default-profile-image.png" alt="Profile image" class="profileImageSmall" />
        Name
        <span style="font-size: 11px; color: gray;">(members)</span>
    </div>
    <div class="span8">
        <div class="progress" style="margin-bottom: 16px;">
            <div class="bar bar-talks" style="width: 25%">Talks</div>
            <div class="bar bar-content" style="width: 25%">Content</div>
            <div class="bar bar-tweets" style="width: 25%">Tweets</div>
            <div class="bar bar-events" style="width: 25%">Events</div>
        </div>
    </div>
    <div class="span1">
        <span class="badge badge-inverse">Score</span>
    </div>
</div>

<hr />

<a name="people"></a><h3>People</h3>
<p>
    Here are the most active people.
</p>
<br />
<c:set var="activityList" value="${activityListForPeople}" />
<c:set var="topScore" value="${topScoreForPeople}" />
<%@ include file="/WEB-INF/fragments/activity.jspf" %>

<hr />

<a name="business"></a><h3>Business tribes</h3>
<p>
    These are the most active business tribes, based upon the average of all activity by them and their members.
</p>
<br />
<c:set var="activityList" value="${activityListForBusinessTribes}" />
<c:set var="topScore" value="${topScoreForBusinessTribes}" />
<%@ include file="/WEB-INF/fragments/activity.jspf" %>

<hr />

<a name="community"></a><h3>Community tribes</h3>
<p>
    And finally these are the most active community tribes, based upon their activity.
</p>
<br />
<c:set var="activityList" value="${activityListForCommunityTribes}" />
<c:set var="topScore" value="${topScoreForCommunityTribes}" />
<%@ include file="/WEB-INF/fragments/activity.jspf" %>