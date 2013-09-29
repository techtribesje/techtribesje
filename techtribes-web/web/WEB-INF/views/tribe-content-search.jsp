<%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

<hr />
<%@ include file="/WEB-INF/fragments/content-source-statistics.jspf" %>
<hr />

<div class="contentSection">
    <div class="subSectionHeading">Blog entries, etc</div>
    <%@ include file="/WEB-INF/fragments/searchResults.jspf" %>
    </div>

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

<script src="/static/js/highlight-content-by-same-author.js"></script>