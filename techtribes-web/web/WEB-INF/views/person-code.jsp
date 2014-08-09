<div class="section codeSection">
    <div class="sectionHeading">
        <div class="container">
            <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
            <h1><a href="<techtribesje:goto contentSource="${person}"/>"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" /></a> ${person.name}</h1>
        </div>
    </div>

    <div class="container">
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