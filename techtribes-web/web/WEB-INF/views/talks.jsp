<div class="section talksSection">
    <div class="container">
        <div style="float: right;">
            <ul class="nav nav-pills">
                <c:choose>
                    <c:when test="${activeNav eq '2014'}">
                        <li class="active"><a href="/talks/year/2014">2014</a></li>
                        <li><a href="/talks/year/2013">2013</a></li>
                        <li><a href="/talks/year/2012">2012</a></li>
                    </c:when>
                    <c:when test="${activeNav eq '2013'}">
                        <li><a href="/talks/year/2014">2014</a></li>
                        <li class="active"><a href="/talks/year/2013">2013</a></li>
                        <li><a href="/talks/year/2012">2012</a></li>
                    </c:when>
                    <c:when test="${activeNav eq '2012'}">
                        <li><a href="/talks/year/2014">2014</a></li>
                        <li><a href="/talks/year/2013">2013</a></li>
                        <li class="active"><a href="/talks/year/2012">2012</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="/talks/year/2014">2014</a></li>
                        <li><a href="/talks/year/2013">2013</a></li>
                        <li><a href="/talks/year/2012">2012</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
        <h1>Talks by local speakers</h1>

        <c:if test="${not empty countries}">
        <p style="text-align: center;">
            <c:forEach var="country" items="${countries}"><img src="<techtribesje:flag name="${country}" />" alt="${country}" title="${country}" /> </c:forEach>
            (talks ${fn:length(talks)}, countries ${fn:length(countries)})
        </p>
        </c:if>

        <%@ include file="/WEB-INF/fragments/talks.jspf" %>
    </div>
</div>