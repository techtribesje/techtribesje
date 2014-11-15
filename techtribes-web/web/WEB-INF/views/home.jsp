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

<c:if test="${not empty jobs}">
    <%@ include file="/WEB-INF/views/jobs.jsp" %>
</c:if>

<div class="section booksSection hidden-phone">
    <div class="container">
        <h1>Books by local authors</h1>
        <div class="row">
            <c:forEach var="book" items="${books}">
                <div class="span1 <c:if test="${not book.published}">faded</c:if>">
                    <a href="${book.url}" target="_blank"><img src="/static/img/books/${book.id}.png" alt="${book.title}" class="thumbnail bookCover" /></a>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

