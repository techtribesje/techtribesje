<div class="sectionHeading">Search for <c:out value="${query}" escapeXml="true" /></div>

<p>
    Our team of monkeys has gathered the following for you. Enjoy!
</p>

<hr />
<%@ include file="/WEB-INF/fragments/content-source-statistics.jspf" %>
<hr />

<%@ include file="/WEB-INF/fragments/searchResults.jspf" %>

<c:if test="${not empty currentPage}">
<div class="pagingLinks">
    <c:if test="${currentPage > 1}">
        <a href="/search/${currentPage-1}?q=${query}">&lt; Previous</a> |
    </c:if>
    Page ${currentPage}
    <c:if test="${currentPage < maxPage}">
        | <a href="/search/${currentPage+1}?q=${query}">Next &gt;</a>
    </c:if>
</div>
</c:if>

<script src="/static/js/highlight-content-by-same-author.js"></script>