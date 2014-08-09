<div class="section">
    <div class="sectionHeading">
        <div class="container">
            <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
            <h1><a href="<techtribesje:goto contentSource="${person}"/>"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" /></a> ${person.name}</h1>
        </div>
    </div>

    <div class="container">
        <%@ include file="/WEB-INF/fragments/person-profile.jspf" %>

        <c:if test="${not empty activity && activity.score > 0}">
        <br /><br />
        <h2>Activity</h2>
        <p>
            Here's a summary of this person's activity over the past 7 days.
            <a href="/activity"><span class="badge"><fmt:formatNumber value="${activity.score}" maxFractionDigits="0" /> points</span></a>
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