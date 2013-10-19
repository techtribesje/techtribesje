<div class="section jobsSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
        <h1><a href="<techtribesje:goto contentSource="${tribe}"/>"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" /></a> Jobs</h1>

        <c:choose>
        <c:when test="${not empty jobs}">
        <%@ include file="/WEB-INF/fragments/jobs.jspf" %>
        </c:when>
        <c:otherwise>
        <p>
        We couldn't find any jobs being offered by this tribe. :-(
        </p>
        </c:otherwise>
        </c:choose>
    </div>
</div>