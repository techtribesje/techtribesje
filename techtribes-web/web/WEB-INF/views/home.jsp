<c:set var="showPagingLinks" value="false" />

<c:if test="${not empty newsEntries}">
    <%@ include file="/WEB-INF/views/news.jsp" %>
</c:if>

<c:if test="${not empty events}">
    <%@ include file="/WEB-INF/views/events.jsp" %>
</c:if>

<c:if test="${not empty talks}">
    <%@ include file="/WEB-INF/views/talks.jsp" %>
</c:if>

<%@ include file="/WEB-INF/views/content.jsp" %>


