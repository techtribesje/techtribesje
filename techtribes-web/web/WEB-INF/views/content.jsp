<div class="section contentSection">
    <div class="container">
        <h1>Blog posts, etc</h1>

        <%@ include file="/WEB-INF/fragments/newsFeedEntries.jspf" %>

        <div class="pagingLinks">
        <c:choose>
            <c:when test="${showPagingLinks ne 'false'}">
                <c:if test="${currentPage > 1}">
                    <a href="/content/${currentPage-1}">&lt; Newer</a> |
                </c:if>
                Page ${currentPage}
                <c:if test="${currentPage < maxPage}">
                    | <a href="/content/${currentPage+1}">Older &gt;</a>
                </c:if>
            </c:when>
            <c:otherwise>
                <a href="/content/">More...</a>
            </c:otherwise>
        </c:choose>
        </div>
    </div>
</div>