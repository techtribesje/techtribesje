<div class="section codeSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
        <h1><a href="<techtribesje:goto contentSource="${tribe}"/>"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" /></a> Code</h1>

        <c:choose>
        <c:when test="${not empty tribe.gitHubId}">
            <%@ include file="/WEB-INF/fragments/code.jspf" %>
        </c:when>
        <c:otherwise>
            <p>
            We couldn't find any code by this tribe. :-(
            </p>
        </c:otherwise>
        </c:choose>
    </div>
</div>