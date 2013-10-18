<%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

<div class="contentSection">
    <div class="subSectionHeading">Blog entries, etc by the tribe and its members</div>

    <c:choose>
    <c:when test="${not empty newsFeedEntries}">
    <%@ include file="/WEB-INF/fragments/newsFeedEntries.jspf" %>

    <div class="pagingLinks">
        <c:if test="${currentPage > 1}">
            <a href="/tribes/${tribe.shortName}/content/${currentPage-1}">&lt; Newer</a> |
        </c:if>
        Page ${currentPage}
        <c:if test="${currentPage < maxPage}">
            | <a href="/tribes/${tribe.shortName}/content/${currentPage+1}">Older &gt;</a>
        </c:if>
    </div>
    </c:when>
    <c:otherwise>
    The monkeys couldn't find any content by this tribe. :-(
    </c:otherwise>
    </c:choose>
</div>