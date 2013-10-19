<div class="section contentSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
        <h1><a href="<techtribesje:goto contentSource="${tribe}"/>"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" /></a> Blog posts, etc</h1>

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
            <p>
            We couldn't find any content by this tribe. :-(
            </p>
        </c:otherwise>
        </c:choose>
    </div>
</div>