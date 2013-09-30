<%@ include file="/WEB-INF/fragments/person-profile.jspf" %>

<div class="talksSection">
    <c:choose>
        <c:when test="${not empty talks}">
            <div class="subSectionHeading">${fn:length(talks)} talks in ${fn:length(countries)} countries</div>
            <p style="text-align: center;">
                <c:forEach var="country" items="${countries}"><img src="<techtribesje:flag name="${country}" />" alt="${country}" title="${country}" /> </c:forEach>
            </p>
            <hr />

            <%@ include file="/WEB-INF/fragments/talks.jspf" %>
        </c:when>
        <c:otherwise>
            <div class="subSectionHeading">Talks</div>
            The monkeys couldn't find any talks by this person. :-(
        </c:otherwise>
    </c:choose>
</div>