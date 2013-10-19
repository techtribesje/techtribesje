<div class="section">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
        <%@ include file="/WEB-INF/fragments/person-profile.jspf" %>

        <c:if test="${not empty activity && activity.score > 0}">
        <br /><br />
        <h2>Activity</h2>
        <p>
            Here's a summary of this person's activity over the past 7 days.
            <a href="/activity"><span class="badge"><fmt:formatNumber value="${activity.score}" minFractionDigits="1" maxFractionDigits="1" /> points</span></a>
        </p>
        <%@ include file="/WEB-INF/fragments/activity-summary.jspf" %>
        </c:if>

        <c:if test="${not empty badges}">
        <br /><br />
        <h2>Badges</h2>
        <c:set var="contentSource" value="${person}" />
        <%@ include file="/WEB-INF/fragments/badges.jspf" %>
        </c:if>

    </div>
</div>