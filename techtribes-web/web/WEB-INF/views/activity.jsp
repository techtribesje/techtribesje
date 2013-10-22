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

        <hr />

        <%@ include file="/WEB-INF/fragments/activity-key.jspf" %>

        <a name="people"></a><h2>People</h2>
        <p>
            Here are the most active people.
        </p>
        <c:set var="activityList" value="${activityListForPeople}" />
        <c:set var="topScore" value="${topScoreForPeople}" />
        <%@ include file="/WEB-INF/fragments/activity.jspf" %>

        <br /><br />

        <a name="business"></a><h2>Business tribes</h2>
        <p>
            These are the most active business tribes, based upon their activity.
        </p>
        <c:set var="activityList" value="${activityListForBusinessTribes}" />
        <c:set var="topScore" value="${topScoreForBusinessTribes}" />
        <%@ include file="/WEB-INF/fragments/activity.jspf" %>

        <br /><br />

        <a name="community"></a><h2>Community tribes</h2>
        <p>
            And finally these are the most active community tribes, based upon their activity.
        </p>
        <c:set var="activityList" value="${activityListForCommunityTribes}" />
        <c:set var="topScore" value="${topScoreForCommunityTribes}" />
        <%@ include file="/WEB-INF/fragments/activity.jspf" %>

    </div>
</div>
