<div class="section eventsSection">
    <div class="sectionHeading">
        <div class="container">
            <div style="float: right;">
                <ul class="nav nav-pills">
                    <c:forEach var="year" items="${years}">
                        <li <c:if test="${activeNav eq year}">class="active"</c:if>><a href="/events/year/${year}">${year}</a></li>
                    </c:forEach>
                </ul>
            </div>
            <h1>Local events</h1>
        </div>
    </div>

    <div class="container">
        <%@ include file="/WEB-INF/fragments/events.jspf" %>
    </div>
</div>