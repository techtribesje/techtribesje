<div class="section contentSection">
    <div class="sectionHeading">
        <div class="container">
            <div style="float: right;">
                <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
            </div>
            <h1><a href="<techtribesje:goto contentSource="${tribe}"/>"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" /></a> ${tribe.name}</h1>
        </div>
    </div>

    <div class="container">
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