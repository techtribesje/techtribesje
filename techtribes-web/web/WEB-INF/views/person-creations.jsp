<div class="section creationsSection">
    <div class="sectionHeading">
        <div class="container">
            <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
            <h1><a href="<techtribesje:goto contentSource="${person}"/>"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" /></a> ${person.name}</h1>
        </div>
    </div>

    <div class="container">
        <c:choose>
        <c:when test="${not empty creations}">
            <%@ include file="/WEB-INF/fragments/creations.jspf" %>
        </c:when>
        <c:otherwise>
            <p>
            We couldn't find any creations by this person. :-(
            </p>
        </c:otherwise>
        </c:choose>
    </div>
</div>