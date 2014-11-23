<div class="section">
    <div class="sectionHeading">
        <div class="container">
            <div style="float: right;">
                <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
            </div>
            <h1><a href="<techtribesje:goto contentSource="${tribe}"/>"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" /></a> ${tribe.name}</h1>
        </div>
    </div>

    <div class="container">
        <%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

        <c:if test="${tribe.contentAggregated}">
            <%--<c:if test="${tribe.type ne 'Tech' && not empty activity && activity.score > 0}">--%>
            <%--<br /><br />--%>
            <%--<h2>Activity</h2>--%>
                <%--<p>--%>
                <%--<c:choose>--%>
                    <%--<c:when test="${tribe.type == 'Community'}">--%>
                            <%--Here's a summary of the tribe's activity over the past 7 days.--%>
                    <%--</c:when>--%>
                    <%--<c:otherwise>--%>
                            <%--Here's a summary of the activity over the past 7 days, for the tribe and its members.--%>
                    <%--</c:otherwise>--%>
                <%--</c:choose>--%>
                <%--<a href="/activity"><span class="badge"><fmt:formatNumber value="${activity.score}" maxFractionDigits="0" /> points</span></a>--%>
                <%--</p>--%>
            <%--<%@ include file="/WEB-INF/fragments/activity-summary.jspf" %>--%>
            <%--</c:if>--%>

            <c:if test="${tribe.type ne 'Tech' && not empty badges}">
            <br /><br />
            <h2>Badges</h2>
            <c:set var="contentSource" value="${tribe}" />
            <%@ include file="/WEB-INF/fragments/badges.jspf" %>
            </c:if>
        </c:if>
    </div>
</div>
