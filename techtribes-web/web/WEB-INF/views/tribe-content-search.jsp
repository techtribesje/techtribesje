<div class="section contentSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
        <%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

        <hr />

        <%@ include file="/WEB-INF/fragments/newsFeedEntries.jspf" %>

        <c:if test="${not empty currentPage}">
        <div class="pagingLinks">
            <c:if test="${currentPage > 1}">
                <a href="/tribes/${tribe.shortName}/content/${currentPage-1}">&lt; Previous</a> |
            </c:if>
            Page ${currentPage}
            <c:if test="${currentPage < maxPage}">
                | <a href="/tribes/${tribe.shortName}/content/${currentPage+1}">Next &gt;</a>
            </c:if>
        </div>
        </c:if>
    </div>
</div>