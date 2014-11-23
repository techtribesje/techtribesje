<div class="section eventsSection">
    <div class="sectionHeading">
        <div class="container">
            <div style="float: right;">
                <ul class="nav nav-pills">
                    <c:choose>
                        <c:when test="${activeNav eq '2014'}">
                            <li class="active"><a href="/events/year/2014">2014</a></li>
                            <li><a href="/events/year/2013">2013</a></li>
                            <li><a href="/events/year/2012">2012</a></li>
                        </c:when>
                        <c:when test="${activeNav eq '2013'}">
                            <li><a href="/events/year/2014">2014</a></li>
                            <li class="active"><a href="/events/year/2013">2013</a></li>
                            <li><a href="/events/year/2012">2012</a></li>
                        </c:when>
                        <c:when test="${activeNav eq '2012'}">
                            <li><a href="/events/year/2014">2014</a></li>
                            <li><a href="/events/year/2013">2013</a></li>
                            <li class="active"><a href="/events/year/2012">2012</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="/events/year/2014">2014</a></li>
                            <li><a href="/events/year/2013">2013</a></li>
                            <li><a href="/events/year/2012">2012</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
            <h1>Upcoming events</h1>
        </div>
    </div>

    <div class="container">
        <%@ include file="/WEB-INF/fragments/events.jspf" %>
    </div>
</div>