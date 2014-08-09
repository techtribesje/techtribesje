<div class="section tweetsSection">
    <div class="sectionHeading">
        <div class="container">
            <div style="float: right;">
                <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
            </div>
            <h1><a href="<techtribesje:goto contentSource="${tribe}"/>"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" /></a> ${tribe.name}</h1>
        </div>
    </div>

    <div class="container">
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
                <p>
                We couldn't find any tweets by this tribe. :-(
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</div>