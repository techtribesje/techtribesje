<%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

<div class="tweetsSection">
    <div class="subSectionHeading">Tweets by the tribe and its members</div>

    <c:choose>
        <c:when test="${not empty tweets}">
            <%@ include file="/WEB-INF/fragments/tweets.jspf" %>

            <div class="pagingLinks">
                <c:if test="${currentPage > 1}">
                    <a href="/tribes/${tribe.shortName}/tweets/${currentPage-1}">&lt; Newer</a> |
                </c:if>
                Page ${currentPage}
                <c:if test="${currentPage < maxPage}">
                    | <a href="/tribes/${tribe.shortName}/tweets/${currentPage+1}">Older &gt;</a>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            The monkeys couldn't find any tweets by this tribe. :-(
        </c:otherwise>
    </c:choose>
</div>