<div class="section jobsSection">
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