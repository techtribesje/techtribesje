<div class="section tweetsSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
        <h1><a href="/people/${person.shortName}"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" /></a> Tweets</h1>

        <c:choose>
        <c:when test="${not empty tweets}">
        <%@ include file="/WEB-INF/fragments/tweets.jspf" %>

        <div class="pagingLinks">
            <c:if test="${currentPage > 1}">
                <a href="/people/${person.shortName}/tweets/${currentPage-1}">&lt; Newer</a> |
            </c:if>
            Page ${currentPage}
            <c:if test="${currentPage < maxPage}">
                | <a href="/people/${person.shortName}/tweets/${currentPage+1}">Older &gt;</a>
            </c:if>
        </div>
        </c:when>
        <c:otherwise>
        We couldn't find any tweets by this person. :-(
        </c:otherwise>
        </c:choose>
    </div>
</div>