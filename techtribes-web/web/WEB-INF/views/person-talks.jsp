<div class="section talksSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/person-navigation.jspf" %>
        <h1><a href="/people/${person.shortName}"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" /></a> Talks</h1>

        <c:choose>
        <c:when test="${not empty talks}">
            <p style="text-align: center;">
                <c:forEach var="country" items="${countries}"><img src="<techtribesje:flag name="${country}" />" alt="${country}" title="${country}" /> </c:forEach>
            </p>

            <%@ include file="/WEB-INF/fragments/talks.jspf" %>
        </c:when>
        <c:otherwise>
            <h2>Talks</h2>
            The monkeys couldn't find any talks by this person. :-(
        </c:otherwise>
    </c:choose>
</div>