<c:if test="${not empty newsEntries}">
    <%@ include file="/WEB-INF/views/news.jsp" %>
</c:if>

<c:if test="${not empty events}">
    <%@ include file="/WEB-INF/views/events.jsp" %>
</c:if>

<c:if test="${not empty talks}">
    <%@ include file="/WEB-INF/views/talks.jsp" %>
</c:if>

<div class="contentSection">
    <%@ include file="/WEB-INF/views/content.jsp" %>
</div>

<div class="tweetsSection">
    <%@ include file="/WEB-INF/views/tweets.jsp" %>
</div>

<c:if test="${not empty jobs}">
<div class="jobsSection">
    <%@ include file="/WEB-INF/views/jobs.jsp" %>
</div>
</c:if>