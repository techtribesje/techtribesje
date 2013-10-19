<div class="section">
    <div class="container">
        <div style="float: right;">
            <ul class="nav nav-pills">
                <c:choose>
                    <c:when test="${sort eq 'followers'}">
                        <li><a href="/people">Sort by name</a></li>
                        <li class="active"><a href="/people?sort=followers">Sort by followers</a></li>
                        <li><a href="/people?sort=activity">Sort by activity</a></li>
                    </c:when>
                    <c:when test="${sort eq 'activity'}">
                        <li><a href="/people">Sort by name</a></li>
                        <li><a href="/people?sort=followers">Sort by followers</a></li>
                        <li class="active"><a href="/people?sort=activity">Sort by activity</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="active"><a href="/people">Sort by name</a></li>
                        <li><a href="/people?sort=followers">Sort by followers</a></li>
                        <li><a href="/people?sort=activity">Sort by activity</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
        <h1>People</h1>

        <c:forEach var="person" items="${people}" varStatus="status">
            <c:if test="${status.index % 3 == 0}">
                <div class="row">
            </c:if>
                    <div class="span4">
                        <%@ include file="/WEB-INF/fragments/person-summary.jspf" %>
                    </div>
            <c:if test="${status.index % 3 == 2}">
                </div>
            </c:if>

            <c:if test="${status.last and status.index % 3 ne 2}">
                <c:forEach begin="1" end="${2-(status.index % 3)}">
                    <div class="span4">&nbsp;</div>
                </c:forEach>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>