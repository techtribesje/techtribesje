<%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

<div class="jobsSection">
    <div class="subSectionHeading">Jobs</div>
    <c:choose>
    <c:when test="${not empty jobs}">
    <%@ include file="/WEB-INF/fragments/jobs.jspf" %>
    </c:when>
    <c:otherwise>
    The monkeys couldn't find any jobs being offered by this tribe. :-(
    </c:otherwise>
    </c:choose>
</div>