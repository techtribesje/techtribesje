<%@ page import="je.techtribes.domain.Activity" %>
<div class="section">
    <div class="container">

        <h1>Activity</h1>
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

        <h2>Badges</h2>
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

        <br /><br />

        <a name="people"></a><h2>People</h2>
        <p>
            Here are the most active people.
        </p>
        <%@ include file="/WEB-INF/fragments/activity-key.jspf" %>
        <c:set var="activityList" value="${activityListForPeople}" />
        <c:set var="topScore" value="${topScoreForPeople}" />
        <%@ include file="/WEB-INF/fragments/activity.jspf" %>

        <br /><br />

        <a name="business"></a><h2>Business tribes</h2>
        <p>
            These are the most active business tribes, based upon the average of all activity by them and their members.
        </p>
        <%@ include file="/WEB-INF/fragments/activity-key.jspf" %>
        <c:set var="activityList" value="${activityListForBusinessTribes}" />
        <c:set var="topScore" value="${topScoreForBusinessTribes}" />
        <%@ include file="/WEB-INF/fragments/activity.jspf" %>

        <br /><br />

        <a name="community"></a><h2>Community tribes</h2>
        <p>
            And finally these are the most active community tribes, based upon their activity.
        </p>
        <%@ include file="/WEB-INF/fragments/activity-key.jspf" %>
        <c:set var="activityList" value="${activityListForCommunityTribes}" />
        <c:set var="topScore" value="${topScoreForCommunityTribes}" />
        <%@ include file="/WEB-INF/fragments/activity.jspf" %>

    </div>
</div>
