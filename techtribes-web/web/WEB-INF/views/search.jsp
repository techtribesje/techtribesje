<c:choose>
    <c:when test="${searchType eq 'Tweet'}">
<div class="section tweetsSection">
    </c:when>
    <c:when test="${searchType eq 'NewsFeedEntry'}">
<div class="section contentSection">
    </c:when>
    <c:otherwise>
<div class="section">
    </c:otherwise>
</c:choose>
    <div class="container">
        <h1>Search for <c:out value="${query}" escapeXml="true" /></h1>

        <c:choose>
            <c:when test="${not empty tweets or not empty newsFeedEntries}">
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
            </c:when>
            <c:otherwise>
                <p>
                    We couldn't find anything for you, sorry!
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</div>