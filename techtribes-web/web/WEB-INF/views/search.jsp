<div class="sectionHeading">Search for <c:out value="${query}" escapeXml="true" /></div>

<p>
    Our team of monkeys has gathered the following for you. Enjoy!
</p>

<hr />
<%@ include file="/WEB-INF/fragments/content-source-statistics.jspf" %>
<hr />

<c:choose>
    <c:when test="${searchType eq 'Tweet'}">
        <%@ include file="/WEB-INF/fragments/tweets.jspf" %>
    </c:when>
    <c:when test="${searchType eq 'NewsFeedEntry'}">
        <%@ include file="/WEB-INF/fragments/newsFeedEntries.jspf" %>
    </c:when>
</c:choose>

<c:if test="${not empty currentPage}">
<div class="pagingLinks">
    <c:if test="${currentPage > 1}">
        <a href="/search/${currentPage-1}?q=${encodedQuery}">&lt; Previous</a> |
    </c:if>
    Page ${currentPage}
    <c:if test="${currentPage < maxPage}">
        | <a href="/search/${currentPage+1}?q=${encodedQuery}">Next &gt;</a>
    </c:if>
</div>
</c:if>

<script src="/static/js/highlight-content-by-same-author.js"></script>