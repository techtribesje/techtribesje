<%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

<hr />
<%@ include file="/WEB-INF/fragments/content-source-statistics.jspf" %>
<hr />

<div class="tweetsSection">
    <div class="subSectionHeading">Tweets</div>
    <%@ include file="/WEB-INF/fragments/tweets.jspf" %>
    </div>

    <c:if test="${not empty currentPage}">
    <div class="pagingLinks">
        <c:if test="${currentPage > 1}">
            <a href="/tribes/${tribe.shortName}/tweets/${currentPage-1}">&lt; Previous</a> |
        </c:if>
        Page ${currentPage}
        <c:if test="${currentPage < maxPage}">
            | <a href="/tribes/${tribe.shortName}/tweets/${currentPage+1}">Next &gt;</a>
        </c:if>
    </div>
    </c:if>
</div>