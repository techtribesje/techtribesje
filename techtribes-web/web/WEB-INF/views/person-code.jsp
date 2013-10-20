<div class="section codeSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
        <h1><a href="<techtribesje:goto contentSource="${person}"/>"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" /></a> Code</h1>

        <c:choose>
        <c:when test="${not empty person.gitHubId}">
            <%@ include file="/WEB-INF/fragments/code.jspf" %>
        </c:when>
        <c:otherwise>
            <p>
            We couldn't find any code by this person. :-(
            </p>
        </c:otherwise>
        </c:choose>
    </div>
</div>