<div class="section talksSection">
    <div class="sectionHeading">
        <div class="container">
            <div style="float: right;">
                <ul class="nav nav-pills">
                    <c:forEach var="year" items="${years}">
                        <li <c:if test="${activeNav eq year}">class="active"</c:if>><a href="/talks/year/${year}">${year}</a></li>
                    </c:forEach>
                    <li <c:if test="${activeNav eq 'videos'}">class="active"</c:if>><a href="/talks/videos">Videos</a></li>
                </ul>
            </div>
            <h1>Talks by local speakers</h1>

            <c:if test="${not empty countries}">
            <p style="text-align: center;">
                <c:forEach var="country" items="${countries}"><img src="<techtribesje:flag name="${country}" />" alt="${country}" title="${country}" /> </c:forEach>
                (${fn:length(talks)} talks in ${fn:length(countries)} countries)
            </p>
            </c:if>
        </div>
    </div>

    <div class="container">
        <%@ include file="/WEB-INF/fragments/talks.jspf" %>
    </div>
</div>