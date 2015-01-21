<div class="section eventsSection">
    <div class="sectionHeading">
        <div class="container">
            <div style="float: right;">
                <ul class="nav nav-pills">
                    <c:set var="currentYear" value="2015"/>
                    <c:set var="startYear" value="2012"/>
                    <c:forEach var="i" begin="0" end="${currentYear-startYear}">
                        <c:set var="year" value="${currentYear - i}" />
                        <li <c:if test="${year eq activeNav}">class="active"</c:if>><a href="/events/year/${year}">${year}</a></li>
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