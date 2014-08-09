<div class="section contentSection">
    <div class="sectionHeading">
        <div class="container">
            <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
            <h1><a href="<techtribesje:goto contentSource="${person}"/>"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" /></a> ${person.name}</h1>
        </div>
    </div>

    <div class="container">
        <c:choose>
        <c:when test="${not empty newsFeedEntries}">
        <%@ include file="/WEB-INF/fragments/newsFeedEntries.jspf" %>

        <div class="pagingLinks">
            <c:if test="${currentPage > 1}">
                <a href="/people/${person.shortName}/content/${currentPage-1}">&lt; Newer</a> |
            </c:if>
            Page ${currentPage}
            <c:if test="${currentPage < maxPage}">
                | <a href="/people/${person.shortName}/content/${currentPage+1}">Older &gt;</a>
            </c:if>
        </div>
        </c:when>
        <c:otherwise>
            <p>
                We couldn't find any content by this person. :-(
            </p>
        </c:otherwise>
        </c:choose>
    </div>
</div>