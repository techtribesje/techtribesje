<div class="section talksSection">
    <div class="sectionHeading">
        <div class="container">
            <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
            <h1><a href="<techtribesje:goto contentSource="${person}"/>"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" /></a> ${person.name}</h1>
        </div>
    </div>

    <div class="container">
        <c:choose>
            <c:when test="${not empty talks}">
                <p style="text-align: center;">
                    <c:forEach var="country" items="${countries}"><img src="<techtribesje:flag name="${country}" />" alt="${country}" title="${country}" /> </c:forEach>
                    (talks ${fn:length(talks)}, countries ${fn:length(countries)})
                </p>

                <%@ include file="/WEB-INF/fragments/talks.jspf" %>
            </c:when>
            <c:otherwise>
                <p>
                    We couldn't find any talks by this person. :-(
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</div>