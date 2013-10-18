<div class="section contentSection">
    <div class="sectionHeading">
        <div class="container">
            <h1>Blog entries, etc</h1>
        </div>
    </div>

    <div class="container">
        <%@ include file="/WEB-INF/fragments/newsFeedEntries.jspf" %>

        <div class="pagingLinks">
            <c:if test="${currentPage > 1}">
                <a href="/content/${currentPage-1}">&lt; Newer</a> |
            </c:if>
            Page ${currentPage}
            <c:if test="${currentPage < maxPage}">
                | <a href="/content/${currentPage+1}">Older &gt;</a>
            </c:if>
        </div>
    </div>
</div>