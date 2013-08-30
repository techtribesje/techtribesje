<div class="row hidden-phone">
    <div class="span4" style="text-align: center;">
        <h4><a href="/activity#people">Most active people</a></h4>
        <c:forEach var="activity" items="${activityListForPeople}" end="2">
            <a href="<techtribesje:goto contentSource="${activity.contentSource}" />"><img src="${activity.contentSource.profileImageUrl}" alt="Profile image" title="${activity.contentSource.name} (${activity.score} points)" class="profileImage" /></a>
        </c:forEach>
        <br /><br />
        <c:forEach var="activity" items="${activityListForPeople}" begin="3" end="9">
            <a href="<techtribesje:goto contentSource="${activity.contentSource}" />"><img src="${activity.contentSource.profileImageUrl}" alt="Profile image" title="${activity.contentSource.name} (${activity.score} points)" class="profileImageSmall" /></a>
        </c:forEach>
    </div>
    <div class="span4" style="text-align: center;">
        <h4><a href="/activity#business">Most active business tribes</a></h4>
        <c:forEach var="activity" items="${activityListForBusinessTribes}" end="2">
            <a href="<techtribesje:goto contentSource="${activity.contentSource}" />"><img src="${activity.contentSource.profileImageUrl}" alt="Profile image" title="${activity.contentSource.name} (${activity.score} points)" class="profileImage" /></a>
        </c:forEach>
        <br /><br />
        <c:forEach var="activity" items="${activityListForBusinessTribes}" begin="3" end="9">
            <a href="<techtribesje:goto contentSource="${activity.contentSource}" />"><img src="${activity.contentSource.profileImageUrl}" alt="Profile image" title="${activity.contentSource.name} (${activity.score} points)" class="profileImageSmall" /></a>
        </c:forEach>
    </div>
    <div class="span4" style="text-align: center;">
        <h4><a href="/activity#community">Most active community tribes</a></h4>
        <c:forEach var="activity" items="${activityListForCommunityTribes}" end="2">
            <a href="<techtribesje:goto contentSource="${activity.contentSource}" />"><img src="${activity.contentSource.profileImageUrl}" alt="Profile image" title="${activity.contentSource.name} (${activity.score} points)" class="profileImage" /></a>
        </c:forEach>
        <br /><br />
        <c:forEach var="activity" items="${activityListForCommunityTribes}" begin="3" end="9">
            <a href="<techtribesje:goto contentSource="${activity.contentSource}" />"><img src="${activity.contentSource.profileImageUrl}" alt="Profile image" title="${activity.contentSource.name} (${activity.score} points)" class="profileImageSmall" /></a>
        </c:forEach>
    </div>
</div>

<c:if test="${not empty newsEntries}">
<div class="newsSection">
    <div class="sectionHeading">News</div>
    <%@ include file="/WEB-INF/fragments/news.jspf" %>

    <div class="pagingLinks">
        <a href="/news">More...</a>
    </div>
</div>
</c:if>

<c:if test="${not empty events}">
<div class="eventsSection">
    <div class="sectionHeading">Upcoming local events</div>
    <%@ include file="/WEB-INF/fragments/events.jspf" %>

    <div class="pagingLinks">
        <a href="/events">More...</a>
    </div>
</div>
</c:if>

<c:if test="${not empty talks}">
<div class="talksSection">
    <div class="sectionHeading">Recent talks by local speakers</div>
    <%@ include file="/WEB-INF/fragments/talks.jspf" %>

    <div class="pagingLinks">
        <a href="/talks">More...</a>
    </div>
</div>
</c:if>

<div class="contentSection">
    <div class="sectionHeading">Blog entries, etc</div>
    <%@ include file="/WEB-INF/fragments/newsFeedEntries.jspf" %>

    <div class="pagingLinks">
        <a href="/content">More...</a>
    </div>
</div>

<div class="tweetsSection">
    <div class="sectionHeading">Tweets</div>
    <%@ include file="/WEB-INF/fragments/tweets.jspf" %>

    <div class="pagingLinks">
        <a href="/tweets">More...</a>
    </div>
</div>

<c:if test="${not empty jobs}">
<div class="jobsSection">
    <div class="sectionHeading">Jobs</div>
    <%@ include file="/WEB-INF/fragments/jobs.jspf" %>

    <div class="pagingLinks">
        <a href="/jobs">More...</a>
    </div>
</div>
</c:if>

<script src="/static/js/highlight-content-by-same-author.js"></script>